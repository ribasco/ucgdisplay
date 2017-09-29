package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

public class DisplayEvent<T extends Graphics> extends Event {

    public static final EventType<DisplayEvent> ANY = new EventType<>(Event.ANY, "ANY");

    public static final EventType<DisplayEvent> DISPLAY_SHOW = new EventType<>(ANY, "DISPLAY_SHOW");

    public static final EventType<DisplayEvent> DISPLAY_HIDE = new EventType<>(ANY, "DISPLAY_HIDE");

    public static final EventType<DisplayEvent> DISPLAY_DRAW = new EventType<>(ANY, "DISPLAY_DRAW");

    private DisplayNode<T> display;

    public DisplayEvent(EventType<? extends Event> eventType, DisplayNode<T> display) {
        super(eventType);
        this.display = display;
    }

    public DisplayNode<T> getDisplay() {
        return display;
    }
}
