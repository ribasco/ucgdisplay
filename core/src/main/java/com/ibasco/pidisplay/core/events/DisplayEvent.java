package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.Graphics;

public class DisplayEvent<T extends Graphics> extends Event {

    public static final EventType<DisplayEvent> ANY = new EventType<>(Event.ANY, "ANY");

    public static final EventType<DisplayEvent> DISPLAY_SHOWING = new EventType<>(ANY, "DISPLAY_SHOWING");

    public static final EventType<DisplayEvent> DISPLAY_SHOWN = new EventType<>(ANY, "DISPLAY_SHOWN");

    public static final EventType<DisplayEvent> DISPLAY_HIDING = new EventType<>(ANY, "DISPLAY_HIDING");

    public static final EventType<DisplayEvent> DISPLAY_HIDDEN = new EventType<>(ANY, "DISPLAY_HIDDEN");

    public static final EventType<DisplayEvent> DISPLAY_DRAW = new EventType<>(ANY, "DISPLAY_DRAW");

    public static final EventType<DisplayEvent> DISPLAY_CLOSING = new EventType<>(ANY, "DISPLAY_CLOSING");

    public static final EventType<DisplayEvent> DISPLAY_CLOSED = new EventType<>(ANY, "DISPLAY_CLOSED");

    private DisplayNode<T> display;

    public DisplayEvent(EventType<? extends Event> eventType, DisplayNode<T> display) {
        super(eventType);
        this.display = display;
    }

    public DisplayNode<T> getDisplay() {
        return display;
    }
}
