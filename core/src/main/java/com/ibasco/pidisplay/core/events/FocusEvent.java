package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventTarget;
import com.ibasco.pidisplay.core.EventType;

public class FocusEvent extends Event {
    public FocusEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public FocusEvent(Object source, EventTarget target, EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }
}
