package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.components.DisplayGroup;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.EventDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ibasco.pidisplay.core.events.EventDispatcher.addHandler;
import static com.ibasco.pidisplay.core.events.EventDispatcher.dispatch;

abstract public class DisplayManager<T extends Graphics> {

    private static final Logger log = LoggerFactory.getLogger(DisplayManager.class);

    private T graphics;

    private ObservableProperty<DisplayGroup<T>> display = new ObservableProperty<>();

    static {
        log.debug("Display Manager Yo");
    }

    protected DisplayManager(T graphics) {
        this.graphics = graphics;
        log.debug("Adding DisplayManager to handle draw events");
        addHandler(DisplayEvent.DISPLAY_DRAW, this::drawDisplay);
    }

    public DisplayGroup<T> getDisplay() {
        return display.get();
    }

    public void setDisplay(DisplayGroup<T> display) {
        log.debug("Setting current display: {}", display);
        this.display.set(display);
        dispatch(new DisplayEvent(DisplayEvent.DISPLAY_DRAW, display));
    }

    public void clear() {
        graphics.clear();
    }

    protected void drawDisplay(DisplayEvent event) {
        EventDispatcher.checkEventDispatchThread();
        log.debug("Display Event Received");
        if (event.getEventType() == DisplayEvent.DISPLAY_DRAW) {
            Display<T> display = event.getDisplay();
            log.debug("Drawing Display: {}", display);
            display.draw(this.graphics);
        } else {
            throw new RuntimeException("Unsupported event type: " + event.getEventType().getName());
        }
    }
}
