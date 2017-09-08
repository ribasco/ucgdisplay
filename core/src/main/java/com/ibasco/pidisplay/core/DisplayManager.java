package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ChangeListener;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.components.DisplayGroup;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.EventDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.ibasco.pidisplay.core.events.EventDispatcher.addHandler;
import static com.ibasco.pidisplay.core.events.EventDispatcher.dispatch;

@SuppressWarnings("unused")
abstract public class DisplayManager<T extends Graphics> {

    private static final Logger log = LoggerFactory.getLogger(DisplayManager.class);

    private T graphics;

    private ObservableProperty<Display<T>> display = new ObservableProperty<>();

    protected DisplayManager(T graphics) {
        Objects.requireNonNull(graphics, "Graphics must not be null");
        this.graphics = graphics;
        addHandler(DisplayEvent.DISPLAY_DRAW, this::drawEventHandler);
    }

    public void setOnDisplayChange(ChangeListener<Display<T>> handler) {
        display.addListener(handler);
    }

    /**
     * Returns the current display
     *
     * @return The current display on the Screen
     */
    public Display<T> getDisplay() {
        return display.get();
    }

    public void setDisplay(DisplayGroup<T> display) {
        log.debug("Setting current display: {}", display);
        DisplayComponent c;
        this.display.set(display);
        display.setWidth(graphics.getWidth());
        display.setHeight(graphics.getHeight());
        display.setVisible(true);
        dispatch(new DisplayEvent(DisplayEvent.DISPLAY_DRAW, display));
    }

    /**
     * Clear the current display
     */
    public void clear() {
        graphics.clear();
    }

    private void drawEventHandler(DisplayEvent event) {
        //Make sure this method is called from the event dispatch thread
        EventDispatcher.checkEventDispatchThread();
        log.debug("Display Event Received");
        if (event.getEventType() == DisplayEvent.DISPLAY_DRAW)
            drawDisplay(this.display.get(event.getDisplay()));
        else
            throw new RuntimeException("Unsupported event type: " + event.getEventType().getName());
    }

    private void drawDisplay(Display<T> display) {
        if (display instanceof DisplayComponent) {
            drawDisplayTree((DisplayComponent<T>) display, this.graphics, 0);
        } else
            display.draw(this.graphics);
    }

    private void drawDisplayTree(DisplayComponent<T> component, T graphics, int depth) {
        for (DisplayComponent<T> node : component.getChildren()) {
            node.draw(graphics);
            drawDisplayTree(node, graphics, depth + 1);
        }
    }
}
