package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.events.DisplayEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

import static com.ibasco.pidisplay.core.events.EventDispatcher.addHandler;
import static com.ibasco.pidisplay.core.events.EventDispatcher.dispatch;

public class DisplayManager<T extends Graphics> {

    private static final Logger log = LoggerFactory.getLogger(DisplayManager.class);

    private T graphics;

    private Stack<Display<T>> displayStack = new Stack<>();

    static {
        log.debug("Display Manager Yo");
    }

    private DisplayManager(T graphics) {
        this.graphics = graphics;
        log.debug("Adding DisplayManager to handle draw events");
        addHandler(DisplayEvent.DISPLAY_DRAW, this::drawDisplay);
        addHandler(DisplayEvent.DISPLAY_SHOW, this::showDisplay);
    }

    protected void showDisplay(DisplayEvent event) {
        if (DisplayEvent.DISPLAY_SHOW.equals(event.getEventType())) {
            log.debug("Showing Display");
            dispatch(new DisplayEvent(DisplayEvent.DISPLAY_DRAW, event.getDisplay()));
        }
    }

    protected void drawDisplay(DisplayEvent event) {
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
