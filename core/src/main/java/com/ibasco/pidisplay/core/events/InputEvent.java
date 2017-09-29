package com.ibasco.pidisplay.core.events;

public class InputEvent extends Event {
    public static final EventType<InputEvent> ANY = new EventType<>(Event.ANY, "INPUT");

    public InputEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    @Override
    public EventType<? extends InputEvent> getEventType() {
        return (EventType<? extends InputEvent>) super.getEventType();
    }
}
