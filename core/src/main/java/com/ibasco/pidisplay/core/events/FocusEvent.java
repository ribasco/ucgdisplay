package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventTarget;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.ui.Graphics;

public class FocusEvent extends Event {

    public static final EventType<FocusEvent> ANY = new EventType<>(Event.ANY, "FOCUS_ANY");

    public static final EventType<FocusEvent> ENTER_FOCUS = new EventType<>(ANY, "ENTER_FOCUS");

    public static final EventType<FocusEvent> EXIT_FOCUS = new EventType<>(ANY, "EXIT_FOCUS");

    private Graphics graphics;

    public FocusEvent(EventType<? extends Event> eventType, Graphics graphics) {
        this(null, null, eventType, graphics);
    }

    public FocusEvent(Object source, EventTarget target, EventType<? extends Event> eventType, Graphics graphics) {
        super(source, target, eventType);
        this.graphics = graphics;
    }

    public final Graphics getGraphics() {
        return graphics;
    }
}
