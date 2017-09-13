package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.beans.PropertyChangeListener;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.InvocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.ibasco.pidisplay.core.events.EventDispatcher.addHandler;
import static com.ibasco.pidisplay.core.events.EventDispatcher.dispatch;

//TODO: Ensure thread-safety access for this class
@SuppressWarnings("unused")
abstract public class DisplayManager<T extends Graphics> {

    private static final Logger log = LoggerFactory.getLogger(DisplayManager.class);

    private T graphics;

    private final ObservableProperty<DisplayNode<T>> displayProperty = new ObservableProperty<>(null);

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    protected DisplayManager(T graphics) {
        this.graphics = Objects.requireNonNull(graphics, "Graphics must not be null");
        this.displayProperty.addListener(this::onDisplayChange);
        addHandler(DisplayEvent.DISPLAY_DRAW, this::drawEventHandler);
        addHandler(InvocationEvent.INVOKE_LATER, this::invocationHandler);
    }

    /**
     * Add a listener for display change notifications
     *
     * @param listener
     *         A {@link PropertyChangeListener} for {@link DisplayNode} change events
     */
    public void setOnDisplayChange(PropertyChangeListener<DisplayNode<T>> listener) {
        displayProperty.addListener(listener);
    }

    /**
     * Returns the current displayProperty
     *
     * @return The current displayProperty on the Screen
     */
    public DisplayNode<T> getDisplay() {
        return displayProperty.get();
    }

    /**
     * Set the top level {@link DisplayNode} to be rendered on the screen
     *
     * @param display
     *         The top-level {@link DisplayNode} that will be rendered on the screen
     */
    public void setActiveDisplay(DisplayNode<T> display) {
        try {
            this.writeLock.lock();
            this.displayProperty.set(display);
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Clears the display screen
     */
    public void clear() {
        try {
            this.writeLock.lock();
            log.debug("Clearing Display: {}", displayProperty.get());
            graphics.clear();
        } finally {
            this.writeLock.unlock();
        }

    }

    private void invocationHandler(InvocationEvent t) {
        log.debug("Invocation Event: {} = {}", t.getEventType(), t.getRunnable());
    }

    /**
     * Event Handler for {@link DisplayEvent#DISPLAY_DRAW} events
     *
     * @param event
     *         The {@link DisplayEvent} object
     */
    private void drawEventHandler(DisplayEvent<T> event) {
        if (event.getEventType() == DisplayEvent.DISPLAY_DRAW) {
            if (displayProperty.isSet()) {
                try {
                    this.readLock.lock();
                    log.debug("Drawing Display: {}", displayProperty.get());
                    displayProperty.get().draw(this.graphics);
                } finally {
                    this.readLock.unlock();
                }
            } else
                log.debug("No display set. Skipping Draw");

        } else
            throw new RuntimeException("Unsupported event type: " + event.getEventType().getName());
    }

    /**
     * Initialize region property defaults
     *
     * @param display
     *         The {@link DisplayNode} to initialize
     */
    private void initDefaults(DisplayNode<T> display) {
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
     * Performs the necessary initialization for every new Display
     */
    private void onDisplayChange(ObservableProperty<DisplayNode<T>> property, DisplayNode<T> oldDisplay, DisplayNode<T> newDisplay) {
        log.debug("Display Changed From {} to {}", oldDisplay, newDisplay);

        //De-activate the previous display
        if (oldDisplay != null) {
            oldDisplay.setVisible(false);
            oldDisplay.setActive(false);
            oldDisplay.setEnabled(false);
        }

        //Clear before drawing the new display
        clear();

        //Initialize new Display
        if (newDisplay != null) {
            initDefaults(newDisplay);

            newDisplay.active.setValid(true);
            newDisplay.visible.setValid(true);
            newDisplay.enabled.setValid(true);

            //Perform a recursive property initialization on child nodes
            newDisplay.doAction((node, arg) -> {
                log.debug("Initializing Properties for Node: {}", node);
                node.active.setValid(true);
                node.visible.setValid(true);
                node.enabled.setValid(true);
            }, true);

            log.debug("Dispatching Draw Event");
            dispatch(new DisplayEvent<>(DisplayEvent.DISPLAY_DRAW, newDisplay));
        }
    }
}
