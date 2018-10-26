package com.ibasco.ucgdisplay.core.events;

import com.ibasco.ucgdisplay.core.Event;
import com.ibasco.ucgdisplay.core.EventType;
import com.ibasco.ucgdisplay.core.input.RawInputEvent;

public class MouseEvent extends InputEvent {

    public static final EventType<MouseEvent> ANY = new EventType<>(InputEvent.ANY, "MOUSE_ANY");

    public static final EventType<MouseEvent> MOUSE_MOVE = new EventType<>(ANY, "MOUSE_MOVE");

    public static final EventType<MouseEvent> MOUSE_PRESS = new EventType<>(ANY, "MOUSE_PRESS");

    public static final EventType<MouseEvent> MOUSE_RELEASED = new EventType<>(ANY, "MOUSE_RELEASED");

    public MouseEvent(EventType<? extends Event> eventType, RawInputEvent rawInputData) {
        super(eventType, rawInputData);
    }
}
