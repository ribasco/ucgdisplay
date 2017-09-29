package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.beans.PropertyChangeListener;
import com.ibasco.pidisplay.core.components.DisplayDialog;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventHandler;
import com.ibasco.pidisplay.core.events.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Base {@link DisplayController} class providing the basic functionality needed for controlling the flow of display
 * components.
 *
 * @param <T>
 *         The underlying type of the {@link Graphics} implementation for this controller
 *
 * @author Rafael Ibasco
 */
abstract public class DisplayController<T extends Graphics> {

    private static final Logger log = LoggerFactory.getLogger(DisplayController.class);

    private T graphics;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private Deque<DisplayNode<T>> displayStack = new ArrayDeque<>();

    private Deque<DisplayNode<T>> hiddenDisplayStack = new ArrayDeque<>();

    private EventManager eventManager = new DefaultEventManager();

    private EventDispatcher eventDispatcher;

    private final PropertyChangeListener changeListener = (a, b, c) -> drawDisplay();

    /**
     * Creates a new instance of {@link DisplayController} using the {@link Graphics} implementation provided
     *
     * @param graphics
     *         The underlying {@link Graphics} implementation used for rendering
     */
    protected DisplayController(T graphics) {
        this(graphics, null);
    }

    /**
     * Creates a new instance of {@link DisplayController} using the {@link Graphics} implementation provided. If {@link
     * ExecutorService} is specified,
     *
     * @param graphics
     *         The underlying {@link Graphics} implementation used for rendering
     * @param executorService
     *         The {@link ExecutorService} that will be used by the event dispatcher.
     */
    protected DisplayController(T graphics, ExecutorService executorService) {
        this.graphics = Objects.requireNonNull(graphics, "Graphics must not be null");
        this.eventDispatcher = new DefaultEventDispatcher(eventManager, executorService);
        //Add handlers for these specific events
        eventManager.register(DisplayEvent.DISPLAY_DRAW, this::drawEventHandler);
    }

    /**
     * Display the dialog and return the result
     *
     * @param displayDialog
     *         The {@link DisplayDialog} to be shown
     * @param <A>
     *         The return type of the {@link DisplayDialog}
     *
     * @return An {@link Optional} type containing the result of the dialog
     */
    public <A> Optional<A> showDialog(DisplayDialog<T, A> displayDialog) {
        return displayDialog.getResult();
    }

    /**
     * Show the display to the top of the screen
     *
     * @param nextDisplay
     *         The {@link DisplayNode}  to display
     */
    @SuppressWarnings("unchecked")
    public void show(DisplayNode<T> nextDisplay) {
        if (nextDisplay == null)
            return;

        //Do not proceed if the specified display is already the active display
        if (!displayStack.isEmpty() && displayStack.peekFirst().equals(nextDisplay)) {
            drawDisplay();
            return;
        }

        //Get the current active display
        DisplayNode<T> prevDisplay = getDisplay();

        //De-activate the previous display
        deactivateDisplay(prevDisplay, true);

        //Remove the display from the stack (if exists)
        if (!nextDisplay.isInitialized() || !displayStack.removeIf(d -> d.getId() == nextDisplay.getId())) {
            //If this is a display that didn't exist on the stack initialize it's properties
            //Initialize node properties recursively
            initDisplayPropDefaults(nextDisplay, true);
        }

        //Push the display to the head of the stack
        displayStack.push(nextDisplay);

        clear();

        //Draw the new display
        activateDisplay(nextDisplay, true);
        drawDisplay(nextDisplay);

        log.debug("DISPLAY_SHOW => From {} to {} (Is Active: {}, Stack Size: {})", prevDisplay, nextDisplay, nextDisplay.isActive(), displayStack.size());
    }

    /**
     * Hides the active display. This basically places the active display on the tail of the display stack. The previous
     * display on the stack will be displayed (if available).
     */
    //TODO: Should we just have a separate stack for hidden windows? Having it to automatically re-appear
    //TODO: Contd: after all displays have been hidden defeats the purpose of hiding it in the first place. User should be
    //TODO: Cont: the one to control when and how it should be re-displayed
    public void hide() {
        DisplayNode<T> prevDisplay = getDisplay(true);

        if (prevDisplay != null) {
            //#1 Deactivate the active display
            deactivateDisplay(prevDisplay, true);
            try {
                //#2 Place it on the end of the stack
                writeLock.lock();
                this.displayStack.addLast(prevDisplay);
            } finally {
                writeLock.unlock();
            }
        }

        clear();

        //Do we have a display remaining in the stack?
        if (!displayStack.isEmpty()) {
            DisplayNode<T> newDisplay = getDisplay();
            activateDisplay(newDisplay, true);
            drawDisplay(newDisplay);
        }

        log.debug("DISPLAY_HIDE => {}", prevDisplay);
    }

    /**
     * Closes the active display and removes it from the display stack. You will need to call {@link #show(DisplayNode)}
     * again to have it re-displayed.
     */
    public void close() {
        DisplayNode node = getDisplay();
        close(node);
        log.debug("DISPLAY_CLOSE => {}", node);
    }

    public void close(DisplayNode<T> node) {
        boolean removed;
        try {
            this.writeLock.lock();
            removed = displayStack.removeIf(d -> d.getId() == node.getId());
        } finally {
            this.writeLock.unlock();
        }
        if (removed) {
            clear();
            //Do we have a display remaining in the stack?
            if (!displayStack.isEmpty()) {
                DisplayNode<T> newDisplay = getDisplay();
                activateDisplay(newDisplay, true);
                drawDisplay(newDisplay);
            }
        }
        log.debug("DISPLAY_CLOSE => {}", node);
    }

    /**
     * Clears the display screen.
     */
    public void clear() {
        try {
            this.writeLock.lock();
            log.debug("DISPLAY_CLEAR => {}", getDisplay());
            graphics.clear();
        } finally {
            this.writeLock.unlock();
        }

    }

    /**
     * Register an {@link EventHandler}. This is the same as calling {@link DefaultEventManager#register(EventType,
     * EventHandler)}
     *
     * @param eventType
     *         The {@link EventType} to listen for events
     * @param handler
     *         The {@link EventHandler} implementation that will handle/consume the event
     * @param <E>
     *         The captured {@link Event} type
     */
    public <E extends Event> void registerEventHandler(final EventType<E> eventType, final EventHandler<? super E> handler) {
        this.eventManager.register(eventType, handler);
    }

    /**
     * @return Returns the underlying {@link EventDispatcher} of this controller.
     */
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * @return Returns the underlying {@link DefaultEventManager} of this controller.
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Shutdown this display and release all resources associated to it
     */
    public void shutdown() {
        this.eventDispatcher.shutdown();
    }

    /**
     * @return Returns the underlying {@link Graphics} driver used by this controller
     */
    public T getGraphics() {
        return graphics;
    }

    /**
     * Retrieves the top most display on the stack.
     *
     * Note: This does not remove the display from the stack.
     *
     * @return The active {@link DisplayNode} retrieved from the top of the stack
     */
    private DisplayNode<T> getDisplay() {
        return getDisplay(false);
    }

    /**
     * Get the {@link DisplayNode} instance from the stack by Id.
     *
     * @param displayId
     *         The display id number to search for
     * @param remove
     *         Set to {@code true} to remove the instance from the stack
     *
     * @return Returns the {@link DisplayNode} instance retrieved from the stack, otherwise {@code null} if no matching
     * display id is found within the stack
     */
    private DisplayNode<T> getDisplay(int displayId, boolean remove) {
        DisplayNode<T> node = this.displayStack.stream()
                .filter(d -> d.getId() == displayId)
                .findFirst()
                .orElse(null);
        if (node != null && remove) {
            try {
                this.writeLock.lock();
                displayStack.remove(node);
            } finally {
                this.writeLock.unlock();
            }
        }
        return node;
    }

    /**
     * Retrieves the top most display on the stack. This method is thread-safe
     *
     * @param remove
     *         Set to {@code true} if the active display will be removed from the stack.
     *
     * @return The active {@link DisplayNode} retrieved from the top of the stack
     */
    private DisplayNode<T> getDisplay(boolean remove) {
        try {
            this.readLock.lock();
            if (!this.displayStack.isEmpty()) {
                if (remove)
                    return this.displayStack.pop();
                //No locking is needed?
                return this.displayStack.peekFirst();
            }
        } finally {
            this.readLock.unlock();
        }
        return null;
    }

    /**
     * Event Handler for {@link DisplayEvent#DISPLAY_DRAW} events
     *
     * @param event
     *         The {@link DisplayEvent} object
     */
    private void drawEventHandler(DisplayEvent<T> event) {
        //Make sure this is running on the event dispatch thread.
        checkEventDispatchThread();
        if (event.getEventType() == DisplayEvent.DISPLAY_DRAW
                && event.getDisplay() != null) {
            try {
                this.readLock.lock();
                //TODO: Need to perform additional checks if the target display exists in the current node
                log.debug("DISPLAY_DRAW => {}", event.getDisplay());
                event.getDisplay().draw(this.graphics);
            } finally {
                event.consume();
                this.readLock.unlock();
            }
        } else
            throw new RuntimeException("Unsupported event type: " + event.getEventType().getName());
    }

    /**
     * Fires a {@link DisplayEvent#DISPLAY_DRAW} to the {@link DefaultEventDispatcher} for the current display
     */
    private void drawDisplay() {
        drawDisplay(getDisplay());
    }

    /**
     * Fires a {@link DisplayEvent#DISPLAY_DRAW} to the {@link DefaultEventDispatcher} for the specified display
     *
     * @param display
     *         The target {@link DisplayNode} to be drawn
     */
    private void drawDisplay(DisplayNode<T> display) {
        if (display == null)
            return;
        eventDispatcher.dispatch(new DisplayEvent<>(DisplayEvent.DISPLAY_DRAW, display));
    }

    /**
     * A utility method that will throw an {@link IllegalStateException} if the calling thread is not the event dispatch
     * thread.
     */
    private void checkEventDispatchThread() {
        if (!eventDispatcher.isEventDispatchThread())
            throw new IllegalStateException("Not currently in event dispatcher thread");
    }

    /**
     * Initialize region property defaults
     *
     * @param display
     *         The {@link DisplayNode} to initialize
     */
    protected void initDefaultDimensions(DisplayNode<T> display) {
        if (!display.maxWidth.isSet())
            display.maxWidth.setValid(graphics.getWidth());
        if (!display.maxHeight.isSet())
            display.maxHeight.setValid(graphics.getHeight());
        if (!display.width.isSet())
            display.width.setValid(graphics.getWidth());
        if (!display.height.isSet())
            display.height.setValid(graphics.getHeight());
    }

    /**
     * @see #initDisplayPropDefaults(DisplayNode, boolean)
     */
    private void initDisplayPropDefaults(DisplayNode<T> display) {
        initDisplayPropDefaults(display, false);
    }

    /**
     * Initialize display properties of parent/child nodes
     *
     * @param display
     *         The root {@link DisplayNode} to initialize
     */
    private void initDisplayPropDefaults(DisplayNode<T> display, boolean recursive) {
        if (display == null)
            return;
        if (recursive) {
            initDisplayPropDefaults(display);
            display.doAction(this::initDisplayPropDefaults, true);
            return;
        }
        boolean success = false;
        try {
            //Inject event dispatcher instance
            if (display.eventDispatcher == null)
                display.eventDispatcher = this.eventDispatcher;
            //Initialize default dimensions
            initDefaultDimensions(display);
            success = true;
            log.debug("{}DISPLAY_PROP_INIT => {} (Is Child: {})", display.hasParent() ? "\t\t" : "", display, display.hasParent());
        } finally {
            display.setInitialized(success);
        }
    }

    /**
     * @see #activateDisplay(DisplayNode, boolean)
     */
    private void activateDisplay(DisplayNode<T> display) {
        activateDisplay(display, false);
    }

    private void activateDisplay(DisplayNode<T> display, boolean recursive) {
        if (display == null)
            return;
        if (recursive) {
            activateDisplay(display);
            display.doAction(this::activateDisplay, true);
            return;
        }
        //Properties to be set on activation
        display.active.setValid(true);
        display.visible.setValid(true);
        display.enabled.setValid(true);

        //Register change listeners
        for (ObservableProperty property : display.getChangeListeners()) {
            //noinspection unchecked
            property.addListener(changeListener);
        }
        log.debug("{}{} {} (Child: {}, Initialized: {}, Active: {}, Visible: {}, Enabled: {})",
                display.hasParent() ? "\t\t" : "",
                !display.hasParent() ? "DISPLAY_ACTIVATED =>" : "",
                display,
                display.hasParent(),
                display.isInitialized(),
                display.isActive(),
                display.isVisible(),
                display.isEnabled());
    }

    /**
     * Deactivates a display. Changes will not be propagated if you use this overloaded method
     *
     * @param display
     *         The {@link DisplayNode} to deactivate
     *
     * @see #deactivateDisplay(DisplayNode, boolean)
     */
    private void deactivateDisplay(DisplayNode<T> display) {
        deactivateDisplay(display, false);
    }

    private void deactivateDisplay(DisplayNode<T> display, boolean recursive) {
        if (display == null)
            return;
        if (recursive) {
            deactivateDisplay(display);
            display.doAction(this::deactivateDisplay, true);
            return;
        }
        //Properties to be set on deactivation
        display.active.setValid(false);
        display.visible.setValid(false);
        display.enabled.setValid(false);

        //Unregister change listeners
        for (ObservableProperty property : display.getChangeListeners()) {
            //noinspection unchecked
            property.removeListener(changeListener);
        }
        log.debug("{}{} {} (Child: {}, Initialized: {}, Active: {}, Visible: {}, Enabled: {})",
                display.hasParent() ? "\t\t" : "",
                !display.hasParent() ? "DISPLAY_DEACTIVATED =>" : "",
                display,
                display.hasParent(),
                display.isInitialized(),
                display.isActive(),
                display.isVisible(),
                display.isEnabled());
    }

    private int createId() {
        return -1;
    }
}
