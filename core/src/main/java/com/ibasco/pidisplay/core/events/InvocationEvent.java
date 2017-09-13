package com.ibasco.pidisplay.core.events;

public class InvocationEvent extends Event {

    public static final EventType<InvocationEvent> INVOKE_IMMEDIATELY = new EventType<>("INVOKE_IMMEDIATELY");

    public static final EventType<InvocationEvent> INVOKE_LATER = new EventType<>("INVOKE_LATER");

    private Runnable runnable;

    public InvocationEvent(EventType<? extends Event> eventType, Runnable runnable) {
        super(eventType);
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
