package com.ibasco.pidisplay.core;


public class Event {

    public static final EventType<Event> ANY = EventType.ROOT;

    private EventType<? extends Event> eventType;

    private boolean consumed = false;

    public Event(EventType<? extends Event> eventType) {
        this.eventType = eventType;
    }

    public EventType<? extends Event> getEventType() {
        return eventType;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        this.consumed = true;
    }

}
