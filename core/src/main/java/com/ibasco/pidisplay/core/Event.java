package com.ibasco.pidisplay.core;

import java.util.Objects;

@SuppressWarnings("all")
public class Event implements Cloneable {

    protected Object source;

    public static final EventTarget NO_TARGET_SOURCE = new EventTarget() {
        @Override
        public EventDispatchChain buildEventTargetPath(EventDispatchChain tail) {
            return tail;
        }

        @Override
        public EventDispatchQueue getEventDispatchQueue() {
            return null;
        }
    };

    public static final EventType<Event> ANY = EventType.ALL;

    protected EventType<? extends Event> eventType;

    protected EventTarget target;

    protected boolean consumed;

    public Event(final EventType<? extends Event> eventType) {
        this(null, null, eventType);
    }

    public Event(final Object source, final EventTarget target, final EventType<? extends Event> eventType) {
        this.source = (source != null) ? source : NO_TARGET_SOURCE;
        this.target = (target != null) ? target : NO_TARGET_SOURCE;
        this.eventType = eventType;
    }

    public Object getSource() {
        return source;
    }

    public EventTarget getTarget() {
        return target;
    }

    public EventType<? extends Event> getEventType() {
        return eventType;
    }

    public Event copyEvent(final Object newSource, final EventTarget newTarget) {
        final Event newEvent = (Event) clone();
        newEvent.source = (newSource != null) ? newSource : NO_TARGET_SOURCE;
        newEvent.target = (newTarget != null) ? newTarget : NO_TARGET_SOURCE;
        newEvent.consumed = false;
        return newEvent;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void consume() {
        consumed = true;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new RuntimeException("Unable to clone event", e);
        }
    }

    public static void fireEvent(EventTarget eventTarget, Event event) {
        EventUtil.fireEvent(
                Objects.requireNonNull(eventTarget, "Event target cannot be null"),
                Objects.requireNonNull(event, "Event cannot be null")
        );
    }

    @Override
    public String toString() {
        return "Event{" +
                "class=" + getClass().getSimpleName() +
                ", eventType=" + eventType +
                '}';
    }
}
