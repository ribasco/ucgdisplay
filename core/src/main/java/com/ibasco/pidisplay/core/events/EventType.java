package com.ibasco.pidisplay.core.events;

public final class EventType<T extends Event> {

    public static final EventType<Event> ROOT = new EventType<>("EVENT");

    private final String name;

    public EventType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType<?> eventType = (EventType<?>) o;

        return name != null ? name.equals(eventType.name) : eventType.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public final String toString() {
        return this.name;
    }
}
