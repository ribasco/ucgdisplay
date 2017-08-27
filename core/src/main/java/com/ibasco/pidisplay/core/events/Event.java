package com.ibasco.pidisplay.core.events;


public class Event {

    public static final EventType<Event> ANY = EventType.ROOT;

    private EventType<? extends Event> eventType;

    public Event(EventType<? extends Event> eventType) {
        this.eventType = eventType;
    }

    public EventType<? extends Event> getEventType() {
        return eventType;
    }
}
