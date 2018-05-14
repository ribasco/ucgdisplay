package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;

public class DisplayInputEvent extends Event {
    public static final EventType<DisplayInputEvent> ENTER_FOCUS = new EventType<>("ENTER_FOCUS");

    public static final EventType<DisplayInputEvent> EXIT_FOCUS = new EventType<>("EXIT_FOCUS");

    public DisplayInputEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
