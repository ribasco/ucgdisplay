package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.events.DisplayEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.ibasco.pidisplay.core.events.EventDispatcher.addHandler;
import static com.ibasco.pidisplay.core.events.EventDispatcher.dispatch;

abstract public class DisplayManager<T extends Graphics> {

    private static final Logger log = LoggerFactory.getLogger(DisplayManager.class);

    private T graphics;

    protected DisplayManager(T graphics) {
        this.graphics = graphics;
        log.debug("Adding DisplayManager to handle draw events");
        addHandler(DisplayEvent.DISPLAY_DRAW, this::drawDisplay);
    }

    public void show(DisplayContainer<? extends Graphics> container) {
        //check if we have a display that is currently showing, if it does, place the current
        // display on the stack and refresh the display with the new one
        Objects.requireNonNull(container, "Display cannot be null");
        dispatch(new DisplayEvent(DisplayEvent.DISPLAY_SHOW, container));
    }

    public void hide(DisplayContainer<? extends Graphics> display) {
        dispatch(new DisplayEvent(DisplayEvent.DISPLAY_HIDE,
                Objects.requireNonNull(display, "Display cannot be null")));
    }

    private void drawDisplay(DisplayEvent event) {
        if (event.getEventType() == DisplayEvent.DISPLAY_DRAW) {
            Display<T> display = event.getDisplay();
            log.debug("Drawing Display: {}", display);
            display.draw(this.graphics);
        } else {
            throw new RuntimeException("Unsupported event type: " + event.getEventType().getName());
        }
    }
}
