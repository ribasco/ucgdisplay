package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.system.RawInputEvent;

public class MouseEvent extends InputEvent {

    public static final EventType<MouseEvent> ANY = new EventType<>(InputEvent.ANY, "MOUSE_ANY");

    public static final EventType<MouseEvent> MOUSE_MOVE = new EventType<>(ANY, "MOUSE_MOVE");

    public static final EventType<MouseEvent> MOUSE_PRESS = new EventType<>(ANY, "MOUSE_PRESS");

    public static final EventType<MouseEvent> MOUSE_RELEASED = new EventType<>(ANY, "MOUSE_RELEASED");

    public MouseEvent(EventType<? extends Event> eventType, RawInputEvent rawInputData) {
        super(eventType, rawInputData);
    }
}
