package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;

public class InvocationEvent extends Event {

    public static final EventType<InvocationEvent> ANY = new EventType<>(Event.ANY, "ANY");

    public static final EventType<InvocationEvent> INVOKE_IMMEDIATELY = new EventType<>(ANY, "INVOKE_IMMEDIATELY");

    public static final EventType<InvocationEvent> INVOKE_LATER = new EventType<>(ANY, "INVOKE_LATER");

    private Runnable runnable;

    public InvocationEvent(EventType<? extends Event> eventType, Runnable runnable) {
        super(eventType);
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
