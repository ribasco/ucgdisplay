package com.ibasco.pidisplay.core;

@SuppressWarnings("WeakerAccess")
public final class EventType<T extends Event> {

    public static final EventType<Event> ROOT = new EventType<>(null, "EVENT");

    private final String name;

    private final EventType<? super T> superType;

    public EventType(String name) {
        this(ROOT, name);
    }

    public EventType(final EventType<? super T> superType, final String name) {
        this.name = name;
        this.superType = superType;
    }

    public EventType<? super T> getSuperType() {
        return superType;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType<T> eventType = (EventType<T>) o;

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
