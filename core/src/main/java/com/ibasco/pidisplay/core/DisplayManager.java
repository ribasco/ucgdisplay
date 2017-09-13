package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.beans.PropertyChangeListener;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.InvocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.ibasco.pidisplay.core.events.EventDispatcher.addHandler;
import static com.ibasco.pidisplay.core.events.EventDispatcher.dispatch;

@SuppressWarnings("unused")
abstract public class DisplayManager<T extends Graphics> {

    private static final Logger log = LoggerFactory.getLogger(DisplayManager.class);

    private T graphics;

    private ObservableProperty<DisplayNode<T>> displayProperty = new ObservableProperty<>(null);

    private final Object displayLock = new Object();

    protected DisplayManager(T graphics) {
        this.graphics = Objects.requireNonNull(graphics, "Graphics must not be null");
        this.displayProperty.addListener(this::onDisplayChange);
        addHandler(DisplayEvent.DISPLAY_DRAW, this::drawEventHandler);
        addHandler(InvocationEvent.INVOKE_LATER, this::invocationHandler);
    }

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

    public void setOnDisplayChange(PropertyChangeListener<DisplayNode<T>> handler) {
        displayProperty.addListener(handler);
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
        this.displayProperty.set(display);
    }

    public void show() {
        getDisplay().setVisible(true);
    }

    public void hide() {
        getDisplay().setVisible(false);
    }

    /**
     * Clear the current displayProperty
     */
    public void clear() {
        synchronized (displayLock) {
            log.debug("Clearing Display: {}", displayProperty.get());
            graphics.clear();
        }
    }

    private void invocationHandler(InvocationEvent t) {
        log.debug("Invocation Event: {} = {}", t.getEventType(), t.getRunnable());
    }

    private void drawEventHandler(DisplayEvent<T> event) {
        if (event.getEventType() == DisplayEvent.DISPLAY_DRAW) {
            if (displayProperty.isSet()) {
                log.debug("Drawing Display: {}", displayProperty.get());
                synchronized (displayLock) {
                    displayProperty.get().draw(this.graphics);
                    this.graphics.flush();
                }
            } else
                log.debug("No display set. Skipping Draw");
        } else
            throw new RuntimeException("Unsupported event type: " + event.getEventType().getName());
    }
}
