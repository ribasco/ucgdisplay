package com.ibasco.pidisplay.core.events;

public class ValueChangeEvent<T> extends Event {
    public static final EventType<ValueChangeEvent> VALUE_CHANGED_EVENT = new EventType<>("VALUE_CHANGED_EVENT");

    private T oldValue;
    private T newValue;

    public ValueChangeEvent(EventType<? extends Event> eventType, T oldValue, T newValue) {
        super(eventType);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
