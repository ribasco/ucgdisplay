package com.ibasco.pidisplay.core;

import com.google.common.collect.Iterables;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.enums.InputEventCode;
import com.ibasco.pidisplay.core.events.*;
import com.ibasco.pidisplay.core.exceptions.NotOnUIThreadException;
import com.ibasco.pidisplay.core.services.InputMonitorService;
import com.ibasco.pidisplay.core.ui.Graphics;
import com.ibasco.pidisplay.core.ui.components.Dialog;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Base {@link Controller} class providing the basic functionality needed for controlling the flow of display
 * components.
 *
 * @param <T>
 *         The underlying type of the {@link Graphics} implementation for this controller
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings("UnusedReturnValue")
abstract public class Controller<T extends Graphics> implements EventTarget {

    //region Properties/Fields
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private static final AtomicInteger nextId = new AtomicInteger(0);

    private static final ThreadGroup threadGroup = new ThreadGroup("pid-ui");

    private T graphics;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    final Lock readLock = readWriteLock.readLock();

    final Lock writeLock = readWriteLock.writeLock();

    private Deque<DisplayParent<T>> displayStack = new ArrayDeque<>();

    private ObservableProperty<EventDispatcher> eventDispatcher;

    private ControllerEventDispatcher internalDispatcher;

    private final int id = nextId.incrementAndGet();

    private final Runnable displayRenderer = this::renderDisplayUI;

    private Thread uiThread;

    private final EventHandler<DisplayEvent> displayEventHandlerCallback = this::displayEventHandler;

    private InputMonitorService inputMonitorService;// = InputMonitorService.getInstance();

    private final ThreadFactory factory = r -> {
        if (uiThread == null) {
            log.debug("Creating UI Thread");
            uiThread = new Thread(threadGroup, r);
            uiThread.setName(String.format("pid-ui-%d", id));
            uiThread.setDaemon(true);
            return uiThread;
        }
        //throw new ControllerException("UI-Thread already exists", this);
        return new Thread(threadGroup, r);
    };

    private final ExecutorService uiService = Executors.newSingleThreadExecutor(factory);

    private AtomicBoolean shutdown = new AtomicBoolean(true);

    private EventDispatchQueue dispatchQueue;
    //endregion

    private Iterator<DisplayNode<T>> focusableNodes = null;

    private DisplayNode<T> focusCurrent = null;

    private Lock focusLock = new ReentrantLock();

    protected Controller(T graphics) {
        log.debug("Creating new Controller instance");
        this.graphics = Objects.requireNonNull(graphics, "Graphics must not be null");
        startUIThread();
        if (LoggerFactory.getLogger(EventDispatchQueue.class).isDebugEnabled()) {
            EventDispatchQueue dispatchQueue = getEventDispatchQueue();
            startRepeatInvoke(new EventQueueMonitor(dispatchQueue, 10));
        }
        addEventHandler(KeyEvent.ANY, this::onKeyEvent, CAPTURE);
    }

    private void onKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getInputEventCode() == InputEventCode.KEY_TAB) {
            log.debug("Focus next item");
            focusNext();
        }
    }

    public <A> Optional<A> showAndWait(Dialog<T, A> dialog) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            //Show dialog
            show(dialog);
            //Set event handler for DialogEvent#DIALOG_RESULT
            dialog.setOnDialogResult(event -> latch.countDown());
            //Block until we get a result
            latch.await();
            return dialog.getResult();
        } catch (InterruptedException e) {
            throw new RuntimeException("DIALOG_INTERRUPTED", e);
        } finally {
            dialog.setOnDialogResult(null);
            log.info("DIALOG_CLOSING => Closing...{}", dialog);
            close(dialog);
        }
    }

    @SuppressWarnings("unchecked")
    public <A> CompletableFuture<Optional<A>> showAndWaitAsync(Dialog<T, A> dialog) {
        CompletableFuture<Optional<A>> future = new CompletableFuture<>();
        show(dialog);
        dialog.setOnDialogResult((DialogEvent event) -> {
            Optional<A> r = Optional.of((A) event.getResult());
            future.complete(r);
        });
        return future;
    }

    private void displayEventHandler(DisplayEvent<T> displayEvent) {
        if (displayEvent.getEventType() == DisplayEvent.DISPLAY_SHOW) {
            if (displayEvent.getDisplay() instanceof DisplayParent)
                _show((DisplayParent<T>) displayEvent.getDisplay());
        }
    }

    public void show(DisplayParent<T> newDisplay) {
        _show(newDisplay);
    }

    private void _show(DisplayParent<T> newDisplay) {
        log.debug("DISPLAY_SHOW_START => Display: {} (Display Stack Empty: {})", newDisplay, displayStack.isEmpty());
        if (newDisplay == null)
            return;

        //#1) Hide the current display display
        final DisplayParent<T> previousDisplay = hide(false);

        //Attach the display to this controller before triggering the event
        attachDisplay(newDisplay);

        fireEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_SHOWING, newDisplay));
        setDisplayActive(newDisplay, true); //#3) Activate the new display
        fireEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_SHOWN, newDisplay));

        log.debug("DISPLAY_SHOW_END => From {} to {} (Is Active: {}, Stack Size: {})", previousDisplay, newDisplay, newDisplay.isActive(), displayStack.size());
    }

    public final DisplayParent<T> hide() {
        DisplayParent<T> previous = hide(true);
        show(getDisplay());
        return previous;
    }

    public final void close() {
        DisplayParent<T> node = getDisplay();
        close(node);
        log.debug("DISPLAY_CLOSE => {}", node);
    }

    public final void close(DisplayParent<T> display) {
        if (display == null)
            return;
        boolean removed;
        try {
            log.debug("Obtaining write lock for Display: {}", display);
            this.writeLock.lock();
            log.debug("Closing Display: {}", display);
            fireEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_CLOSING, display));

            setDisplayActive(display, false);
            clear();

            //Remove from the stack
            removed = displayStack.removeIf(display::equals);

            detachDisplay(display);
            fireEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_CLOSED, display));
        } finally {
            this.writeLock.unlock();
        }

        //Do we have a display remaining in the stack?
        if (removed && !displayStack.isEmpty()) {
            show(getDisplay());
        }

        log.debug("DISPLAY_CLOSE => {}", display);
    }

    public final void clear() {
        log.debug("DISPLAY_CLEAR => {}", getDisplay());
        graphics.clear();
    }

    public T getGraphics() {
        return graphics;
    }

    public final DisplayParent<T> getDisplay() {
        return getDisplay(false);
    }

    public final void shutdown() throws InterruptedException {
        log.debug("Shutting down display controller");
        getEventDispatchQueue().shutdown();
        shutdown.set(true);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "#" + this.getId();
    }

    private void refreshFocusableNodes() {
        DisplayParent<T> activeNode = getDisplay();
        if (activeNode != null && activeNode.hasChildren()) {
            try {
                focusLock.lock();
                List<DisplayNode<T>> focusableNodeList = activeNode.getChildren().stream()
                        .filter(p -> p.isActive() && p.isFocusable())
                        .collect(Collectors.toList());
                focusableNodes = Iterables.cycle(focusableNodeList).iterator();
                log.debug("FOCUS_REFRESH => Refreshed focusable nodes");
                focusNext();
            } finally {
                focusLock.unlock();
            }
        } else {
            log.debug("FOCUS_REFRESH => Skipping refresh. Node does not have any children.");
        }
    }

    public DisplayNode<T> focusCurrent() {
        try {
            focusLock.lock();
            if (focusCurrent == null && getDisplay() != null)
                focusCurrent = focusNext();
            return focusCurrent;
        } finally {
            focusLock.unlock();
        }
    }

    public DisplayNode<T> focusNext() {
        try {
            focusLock.lock();
            if (focusableNodes.hasNext()) {
                if (focusCurrent != null)
                    fireEvent(new FocusEvent(this, focusCurrent, FocusEvent.EXIT_FOCUS, this.graphics));
                focusCurrent = focusableNodes.next();
                fireEvent(new FocusEvent(this, focusCurrent, FocusEvent.ENTER_FOCUS, this.graphics));
                return focusCurrent;
            } else {
                if (focusCurrent != null) {
                    fireEvent(new FocusEvent(this, focusCurrent, FocusEvent.EXIT_FOCUS, this.graphics));
                    focusCurrent = null;
                }
            }
        } finally {
            focusLock.unlock();
        }
        return null;
    }

    //region Private Methods
    private DisplayParent<T> hide(boolean remove) {
        DisplayParent<T> prevDisplay = getDisplay(remove);
        hide(prevDisplay);
        return prevDisplay;
    }

    private void hide(DisplayParent<T> display) {
        if (display == null)
            return;
        log.debug("DISPLAY_HIDE => {}", display);
        fireEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_HIDING, display));

        //#1 Deactivate the display
        setDisplayActive(display, false);
        //#2 Clear the screen
        clear();

        fireEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_HIDDEN, display));
        log.debug("DISPLAY_HIDDEN => {}", display);
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
            return;
        }

        //Is the display already at the head of the stack?
        if (inHead(display)) {
            log.debug("DISPLAY_ACTIVATE => Display is already at the head of the stack : {}", display);
            if (!display.isActive())
                display.setActive(true);
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
                log.debug("DISPLAY_INIT => Initializing Display '{}' for the first time", display);
                //Initialize node properties recursively
                initializeDisplay(display);
            }

            //Update the active flag recursively
            display.setActive(true);

            //Put the display to the head of the stack
            displayStack.push(display);

            refreshFocusableNodes();
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Start UI Thread
     */
    private void startUIThread() {
        shutdown.set(false);
        uiService.execute(() -> {
            while (!shutdown.get()) {
                displayRenderer.run();
                ThreadUtils.sleep(10);
            }
        });
    }

    /**
     * Handles the rendering of the display.
     */
    private void renderDisplayUI() {
        //This method is only meant to be called from within the event/UI thread
        checkEventDispatchThread();
        //try to aquire the lock but do not block the event loop!
        if (readLock.tryLock()) {
            try {
                if (!displayStack.isEmpty()) {
                    DisplayParent<T> activeDisplay = displayStack.peekFirst();
                    if (activeDisplay != null && activeDisplay.isActive()) {
                        activeDisplay.draw(graphics);
                        if (graphics.hasChanges()) {
                            graphics.flush();
                            activeDisplay.postFlush(graphics);
                        }
                    }
                }
            } finally {
                readLock.unlock();
            }
        } else {
            ThreadUtils.sleep(100);
        }
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

    private boolean inHead(DisplayNode<T> display) {
        try {
            this.readLock.lock();
            return !displayStack.isEmpty() && displayStack.peekFirst().equals(display);
        } finally {
            this.readLock.unlock();
        }
    }

    private void initializeDisplay(DisplayParent<T> display) {
        display.doAction(DisplayNode::initDimensions, this.graphics);
        display.setInitialized(true);
    }

    private void attachDisplay(DisplayParent<T> display) {
        if (display != null && !display.isAttached()) {
            log.debug("DISPLAY_ATTACH => Attaching Controller '{}' to display '{}'", this, display);
            //Attach to display and child nodes
            display.addEventHandler(DisplayEvent.ANY, displayEventHandlerCallback, EventDispatchPhase.CAPTURE);
            display.doAction((node, arg) -> node.addEventHandler(DisplayEvent.ANY, arg, EventDispatchPhase.CAPTURE), displayEventHandlerCallback);
            display.setController(this);
        }
    }

    private void detachDisplay(DisplayParent<T> display) {
        if (display != null && display.isAttached()) {
            log.debug("DISPLAY_DETACH => Detached Controller '{}' from display '{}'", this, display);
            display.removeEventHandler(DisplayEvent.ANY, displayEventHandlerCallback, EventDispatchPhase.CAPTURE);
            display.doAction((node, arg) -> node.removeEventHandler(DisplayEvent.ANY, arg, EventDispatchPhase.CAPTURE), displayEventHandlerCallback);
            display.setController(null);
        }
    }
    //endregion

    //region Package-Private methods
    void checkEventDispatchThread() {
        /*if (!getEventDispatchQueue().isDispatchThread())
            throw new NotOnUIThreadException("Not currently in event dispatcher thread");*/
        if (!Thread.currentThread().equals(uiThread))
            throw new NotOnUIThreadException("Not currently in event dispatcher thread");
    }

    final Runnable getDisplayRenderer() {
        return displayRenderer;
    }

    final int getId() {
        return id;
    }
    //endregion

    //region Event Handling Operations
    public final void fireEvent(Event event) {
        getEventDispatchQueue().postEvent(event);
    }

    public final void fireEvent(EventTarget target, Event event) {
        Event evt = event.copyEvent(this, target);
        //EventUtil.fireEvent(target, event);
        getEventDispatchQueue().postEvent(evt);
    }

    public final <E extends Event> void addEventHandler(final EventType<E> eventType, final EventHandler<? super E> eventHandler, EventDispatchPhase dispatchType) {
        getEventHandlerManager().addEventHandler(eventType, eventHandler, dispatchType);
    }

    public final <E extends Event> void removeEventHandler(final EventType<E> eventType, final EventHandler<? super E> eventHandler, EventDispatchPhase dispatchType) {
        getEventHandlerManager().removeEventHandler(eventType, eventHandler, dispatchType);
    }

    public final EventHandlerManager getEventHandlerManager() {
        if (this.internalDispatcher == null)
            internalDispatcher = new ControllerEventDispatcher(this);
        eventDispatcherProperty();
        return internalDispatcher.getEventHandlerManager();
    }

    public final EventDispatcher getEventDispatcher() {
        return eventDispatcherProperty().get();
    }

    public final ObservableProperty<EventDispatcher> eventDispatcherProperty() {
        if (this.eventDispatcher == null) {
            this.eventDispatcher = new ObservableProperty<>(internalDispatcher);
        }
        return eventDispatcher;
    }

    @Override
    public EventDispatchChain buildEventTargetPath(EventDispatchChain tail) {
        DisplayNode activeNode = getDisplay();
        if (activeNode != null)
            tail = activeNode.buildEventTargetPath(tail);
        if (this.internalDispatcher != null)
            tail = tail.addFirst(this.internalDispatcher);
        log.debug("CONTROLLER_EVENT_PATH => {}", tail.toString());
        return tail;
    }

    public boolean isShutdown() {
        return shutdown.get();
    }

    @Override
    public final EventDispatchQueue getEventDispatchQueue() {
        if (dispatchQueue == null)
            dispatchQueue = EventUtil.getEventDispatchQueue(this);
        return dispatchQueue;
    }

    void setDispatchQueue(EventDispatchQueue dispatchQueue) {
        this.dispatchQueue = dispatchQueue;
    }

    //endregion

    //UI/Event Operations
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
}
