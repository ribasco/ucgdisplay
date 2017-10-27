package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.components.DisplayDialog;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.InvocationEvent;
import com.ibasco.pidisplay.core.exceptions.NotOnUIThreadException;
import com.ibasco.pidisplay.core.services.InputMonitorService;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Base {@link Controller} class providing the basic functionality needed for controlling the flow of display
 * components.
 *
 * @param <T>
 *         The underlying type of the {@link Graphics} implementation for this controller
 *
 * @author Rafael Ibasco
 */
abstract public class Controller<T extends Graphics> implements EventTarget {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    //region Properties/Fields
    private T graphics;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    final Lock readLock = readWriteLock.readLock();

    final Lock writeLock = readWriteLock.writeLock();

    private Deque<DisplayParent<T>> displayStack = new ArrayDeque<>();

    private ObservableProperty<EventDispatcher> eventDispatcher;

    private EventHandlerManager eventHandlerManager;

    private final EventHandler<DisplayEvent<? extends T>> DISPLAY_EVENT_HANDLER = this::displayEventHandler;

    private static final AtomicInteger nextId = new AtomicInteger(0);

    private final int id = nextId.incrementAndGet();

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private final Runnable displayRenderer = this::renderDisplayUI;
    //endregion

    protected Controller(T graphics) {
        this.graphics = Objects.requireNonNull(graphics, "Graphics must not be null");
        startRepeatInvoke(displayRenderer);
        if (LoggerFactory.getLogger(EventDispatchQueue.class).isDebugEnabled()) {
            EventDispatchQueue dispatchQueue = getEventDispatchQueue();
            startRepeatInvoke(new EventQueueMonitor(dispatchQueue, 20));
        }
    }

    final Runnable getDisplayRenderer() {
        return displayRenderer;
    }

    public final boolean isUIThread() {
        return getEventDispatchQueue().isDispatchThread();
    }

    /**
     * Handles the rendering of the display. This method is only meant to be called from within the event/UI thread
     */
    private void renderDisplayUI() {
        checkEventDispatchThread();
        //try to aquire the lock but do not block the event loop!
        if (!displayStack.isEmpty() && readLock.tryLock()) {
            try {
                DisplayNode<T> activeDisplay = displayStack.peekFirst();
                if (activeDisplay != null && activeDisplay.isActive() && activeDisplay.isAttached()) {
                    activeDisplay.draw(graphics);
                    graphics.flush();
                }
            } finally {
                readLock.unlock();
            }
        }
        ThreadUtils.sleep(5);
    }

    public <A> Optional<A> showAndWait(DisplayDialog<T, A> displayDialog) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            //Show dialog
            show(displayDialog);
            //Set event handler for DialogEvent#DIALOG_RESULT
            displayDialog.setOnDialogResult(event -> latch.countDown());
            //Block until we get a result
            latch.await();
            return displayDialog.getResult();
        } catch (InterruptedException e) {
            throw new RuntimeException("DIALOG_INTERRUPTED", e);
        } finally {
            displayDialog.setOnDialogResult(null);
            log.info("DIALOG_CLOSING => Closing...{}", displayDialog);
            close(displayDialog);
        }
    }

    public void show(DisplayParent<T> newDisplay) {
        log.debug("DISPLAY_SHOW_START => Display: {} (Display Stack Empty: {})", newDisplay, displayStack.isEmpty());
        if (newDisplay == null)
            return;

        //#1) Hide the current display display
        final DisplayNode<T> previousDisplay = hide(false);

        //Attach the display to this controller before triggering the event
        attachDisplay(newDisplay);

        fireEvent(newDisplay, new DisplayEvent<>(DisplayEvent.DISPLAY_SHOWING, newDisplay));
        setDisplayActive(newDisplay, true); //#3) Activate the new display
        fireEvent(newDisplay, new DisplayEvent<>(DisplayEvent.DISPLAY_SHOWN, newDisplay));

        log.debug("DISPLAY_SHOW_END => From {} to {} (Is Active: {}, Stack Size: {})", previousDisplay, newDisplay, newDisplay.isActive(), displayStack.size());
    }

    /**
     * Hides the current active display. The next display from the top of the stack will be shown.
     *
     * <blockquote>
     * Note: This deactivates and removes the current display from the internal stack but the display remains attached
     * to the underlying display controller.
     * </blockquote>
     *
     * @return The current {@link DisplayNode} instance that was hidden otherwise {@code null} if there was no previous
     * display available
     */
    @SuppressWarnings("UnusedReturnValue")
    public DisplayParent<T> hide() {
        DisplayParent<T> previous = hide(true);
        show(getDisplay());
        return previous;
    }

    private DisplayParent<T> hide(boolean remove) {
        DisplayParent<T> prevDisplay = getDisplay(remove);
        hide(prevDisplay);
        return prevDisplay;
    }

    private void hide(DisplayParent<T> display) {
        if (display == null)
            return;
        log.debug("DISPLAY_HIDE => {}", display);
        fireEvent(display, new DisplayEvent<>(DisplayEvent.DISPLAY_HIDING, display));

        //#1 Deactivate the display
        setDisplayActive(display, false);
        //#2 Clear the screen
        clear();

        fireEvent(display, new DisplayEvent<>(DisplayEvent.DISPLAY_HIDDEN, display));
        log.debug("DISPLAY_HIDDEN => {}", display);
    }

    public void close() {
        DisplayParent<T> node = getDisplay();
        close(node);
        log.debug("DISPLAY_CLOSE => {}", node);
    }

    public void close(DisplayParent<T> display) {
        if (display == null)
            return;
        boolean removed;
        try {
            log.debug("Obtaining write lock for Display: {}", display);
            this.writeLock.lock();
            log.debug("Closing Display: {}", display);
            fireEvent(display, new DisplayEvent<>(DisplayEvent.DISPLAY_CLOSING, display));

            setDisplayActive(display, false);
            clear();

            //Remove from the stack
            removed = displayStack.removeIf(display::equals);

            fireEvent(display, new DisplayEvent<>(DisplayEvent.DISPLAY_CLOSED, display));
            detachDisplay(display);
        } finally {
            this.writeLock.unlock();
        }

        //Do we have a display remaining in the stack?
        if (removed && !displayStack.isEmpty()) {
            //TODO: Use show(newDisplay) instead?
            show(getDisplay());
        }

        log.debug("DISPLAY_CLOSE => {}", display);
    }

    /**
     * Updates the {@link DisplayNode} active flag and it's children and move it at the head of the display stack.
     *
     * @param display
     *         The {@link DisplayNode} instance
     * @param activate
     *         Set to {@code true} to activate the {@link DisplayNode} instance
     */
    private void setDisplayActive(DisplayParent<T> display, boolean activate) {
        if (display == null)
            return;

        //If we are deactivating, just update the flag and return immediately
        if (!activate) {
            log.debug("DISPLAY_DEACTIVATE => Deactivating {} (No lock required)", display);
            display.setActive(false);
            display.doAction(DisplayNode::setActive, false); //TODO: Remove this
            return;
        }

        //Is the display already at the head of the stack?
        if (inHead(display)) {
            log.debug("DISPLAY_ACTIVATE => Display is already at the head of the stack : {}", display);
            if (!display.isActive()) {
                display.setActive(true);
                display.doAction(DisplayNode::setActive, true);
            }
            return;
        }

        //At this point, we can safely assume that either the display is not yet in the stack
        //or it is located at the lower end of the stack, so we need to push it back to the head
        try {
            this.writeLock.lock();
            //Clear the screen only after we obtain the lock
            clear();

            //Remove the display from the stack (if exists) and initialize
            if (!display.isInitialized() || !displayStack.removeIf(display::equals)) {
                log.debug("DISPLAY_INIT => Initializing Display for the first time");
                //Initialize node properties recursively
                initDefaultDimensions(display);
                initializeDisplay(display);
            }

            //Update the active flag recursively
            display.setActive(true);
            display.doAction(DisplayNode::setActive, true);

            //Put the display to the head of the stack
            displayStack.push(display);
        } finally {
            this.writeLock.unlock();
        }
    }

    private boolean inHead(DisplayNode<T> display) {
        try {
            this.readLock.lock();
            return !displayStack.isEmpty() && displayStack.peekFirst().equals(display);
        } finally {
            this.readLock.unlock();
        }
    }

    public void clear() {
        log.debug("DISPLAY_CLEAR => {}", getDisplay());
        graphics.clear();
    }

    public T getGraphics() {
        return graphics;
    }

    public DisplayParent<T> getDisplay() {
        return getDisplay(false);
    }

    private DisplayParent<T> getDisplay(boolean remove) {
        try {
            this.readLock.lock();
            if (!this.displayStack.isEmpty()) {
                if (remove)
                    return this.displayStack.pop();
                return this.displayStack.peekFirst();
            }
        } finally {
            this.readLock.unlock();
        }
        return null;
    }

    void checkEventDispatchThread() {
        if (!getEventDispatchQueue().isDispatchThread())
            throw new NotOnUIThreadException("Not currently in event dispatcher thread");
    }

    private void initDefaultDimensions(DisplayNode<T> display) {
        if (!display.maxWidth.isSet())
            display.maxWidth.setValid(graphics.getWidth());
        if (!display.maxHeight.isSet())
            display.maxHeight.setValid(graphics.getHeight());
        if (!display.width.isSet())
            display.width.setValid(graphics.getWidth());
        if (!display.height.isSet())
            display.height.setValid(graphics.getHeight());
    }

    private void initializeDisplay(DisplayParent<T> display) {
        initDefaultDimensions(display);
        display.setInitialized(true);
        display.doAction((node, arg) -> {
            initDefaultDimensions(node);
            node.setInitialized(true);
            log.debug("{}DISPLAY_PROP_INIT => {} (Is Child: {})", node.hasParent() ? "\t\t" : "", node, node.hasParent());
        });
    }

    private void attachDisplay(DisplayParent<T> display) {
        if (display != null && !display.isAttached()) {
            log.debug("DISPLAY_ATTACH => Attaching Controller '{}' to display '{}'", this, display);
            //Attach to display and child nodes
            //display.addEventHandler(DisplayEvent.ANY, DISPLAY_EVENT_HANDLER, EventDispatchType.BUBBLE);
            display.setController(this);
            //Attach on Child nodes
            display.doAction((node, controller) -> {
                log.debug("{}+ ATTACH => Attaching Controller '{}' to display '{}'", "\t", this, node);
                //node.addEventHandler(DisplayEvent.ANY, DISPLAY_EVENT_HANDLER, EventDispatchType.BUBBLE);
                node.setController(controller);
            }, this);
        } else
            log.debug("Display {} is already attached to a controller {}", display, display.getController());
    }

    private void detachDisplay(DisplayParent<T> display) {
        if (display != null && display.isAttached()) {
            log.debug("DISPLAY_DETACH => Detached Controller '{}' from display '{}'", this, display);
            //display.removeEventHandler(DisplayEvent.ANY, DISPLAY_EVENT_HANDLER, EventDispatchType.BUBBLE);
            display.setController(null);
            //Detach on Child nodes
            display.doAction((node, sArg) -> {
                log.debug("{}+ DETACH => Attaching Controller '{}' to display '{}'", "\t", this, node);
                //node.removeEventHandler(DisplayEvent.ANY, DISPLAY_EVENT_HANDLER, EventDispatchType.BUBBLE);
                node.setController(null);
            }, null);
        }
    }

    private void setDisplayVisible(DisplayParent<T> display, boolean arg) {
        if (display == null)
            return;
        log.trace("{}[{}] Setting visible property to '{}' for {}", StringUtils.repeat('\t', display.getDepth() * 2), display.getDepth(), arg, display);
        display.visible.setValid(arg);
        display.doAction((node, sArg) -> {
            log.trace("{}[{}] Setting visible property to '{}' for {}", StringUtils.repeat('\t', display.getDepth() * 2), node.getDepth(), sArg, node);
            node.visible.setValid(sArg);
        }, arg);
    }

    /**
     * Process Display Events
     *
     * @param displayEvent
     *         The {@link DisplayEvent} to process
     */
    private void displayEventHandler(DisplayEvent<? extends T> displayEvent) {
        //Make sure we are on the event dispatch thread
        checkEventDispatchThread();
        //Process display events here
    }

    //region Event Handling Operations
    public final void fireEvent(Event event) {
        //fireEvent(this, event);
        getEventDispatchQueue().postEvent(event);
    }

    public final void fireEvent(EventTarget target, Event event) {
        EventUtil.fireEvent(target, event);
    }

    public final <T extends Event> void addEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler, EventDispatchType dispatchType) {
        getEventHandlerManager().addEventHandler(eventType, eventHandler, dispatchType);
    }

    public final <T extends Event> void removeEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler, EventDispatchType dispatchType) {
        getEventHandlerManager().removeEventHandler(eventType, eventHandler, dispatchType);
    }

    public final EventHandlerManager getEventHandlerManager() {
        if (this.eventHandlerManager == null)
            eventHandlerManager = new EventHandlerManager(this);
        eventDispatcherProperty();
        return eventHandlerManager;
    }

    public final EventDispatcher getEventDispatcher() {
        return eventDispatcherProperty().get();
    }

    public final ObservableProperty<EventDispatcher> eventDispatcherProperty() {
        if (this.eventDispatcher == null) {
            this.eventDispatcher = new ObservableProperty<>(eventHandlerManager);
        }
        return eventDispatcher;
    }

    @Override
    public EventDispatchChain buildEventTargetPath(EventDispatchChain tail) {
        if (this.eventHandlerManager != null)
            tail = tail.addFirst(this.eventHandlerManager);

        //prepend event dispatcher of input listener
        if (InputMonitorService.getInstance().getEventDispatcher() != null) {
            tail.addFirst(InputMonitorService.getInstance().getEventDispatcher());
        }
        log.trace("CONTROLLER_EVENT_PATH => {}", tail.toString());
        return tail;
    }

    @Override
    public final EventDispatchQueue getEventDispatchQueue() {
        return EventUtil.getEventDispatchQueue(this);
    }
    //endregion

    final int getId() {
        return id;
    }

    //region UI/Event Loop Invocation
    public final void invokeOnce(final Runnable... runnable) {
        fireEvent(new InvocationEvent(InvocationEvent.INVOKE_ONCE, runnable));
    }

    public final void startRepeatInvoke(final Runnable... runnable) {
        fireEvent(new InvocationEvent(InvocationEvent.INVOKE_REPEAT_START, runnable));
    }

    public final void stopRepeatInvoke(final Runnable... runnable) {
        fireEvent(new InvocationEvent(InvocationEvent.INVOKE_REPEAT_END, runnable));
    }
    //endregion

    public void shutdown() throws InterruptedException {
        log.debug("Shutting down display controller");
        getEventDispatchQueue().shutdown();
        shutdown.set(true);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "#" + this.getId();
    }
}
