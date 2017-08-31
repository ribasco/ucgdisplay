package com.ibasco.pidisplay.core.events;

public class ValueChangeEvent<T> extends Event {
    public static final EventType<ValueChangeEvent> VALUE_CHANGED_EVENT = new EventType<>("VALUE_CHANGED_EVENT");

    private T newValue;

    public ValueChangeEvent(EventType<? extends Event> eventType, T newValue) {
        super(eventType);
        this.newValue = newValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
