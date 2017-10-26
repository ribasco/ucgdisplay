package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;

public class InvocationEvent extends Event {

    public static final EventType<InvocationEvent> ANY = new EventType<>(Event.ANY, "INVOKE_ANY");

    public static final EventType<InvocationEvent> INVOKE_ONCE = new EventType<>(ANY, "INVOKE_ONCE");

    public static final EventType<InvocationEvent> INVOKE_REPEAT_START = new EventType<>(ANY, "INVOKE_REPEAT_START");

    public static final EventType<InvocationEvent> INVOKE_REPEAT_END = new EventType<>(ANY, "INVOKE_REPEAT_END");

    private final Runnable[] runnable;

    public InvocationEvent(EventType<? extends Event> eventType, final Runnable... runnable) {
        super(eventType);
        this.runnable = runnable;
    }

    public final Runnable[] getRunnable() {
        return runnable;
    }

    @Override
    public EventType<InvocationEvent> getEventType() {
        //noinspection unchecked
        return (EventType<InvocationEvent>) super.getEventType();
    }
}
