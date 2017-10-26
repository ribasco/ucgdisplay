package com.ibasco.pidisplay.core;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.WeakHashMap;

public final class EventType<T extends Event> implements Serializable {

    public static final EventType<Event> ALL = new EventType<>("EVENT", null);

    private WeakHashMap<EventType<? extends T>, Void> subTypes;

    private final EventType<? super T> parentType;

    private final String name;

    public EventType(final String name) {
        this(ALL, name);
    }

    public EventType(final EventType<? super T> parentType) {
        this(parentType, null);
    }

    public EventType(final EventType<? super T> parentType, final String name) {
        this.parentType = Objects.requireNonNull(parentType, "You must specify a parent type for this instance");
        this.name = name;
        parentType.validateAndRegister(this);
    }

    private EventType(final String name, final EventType<? super T> parentType) {
        this.parentType = parentType;
        this.name = name;
        if (parentType != null) {
            if (parentType.subTypes != null) {
                for (Iterator i = parentType.subTypes.keySet().iterator(); i.hasNext(); ) {
                    EventType t = (EventType) i.next();
                    if (name == null && t.name == null || (name != null && name.equals(t.name))) {
                        i.remove();
                    }
                }
            }
            parentType.validateAndRegister(this);
        }
    }

    public final EventType<? super T> getParentType() {
        return parentType;
    }

    public final String getName() {
        return name;
    }

    @Override
    public String toString() {
        return (name != null) ? name : super.toString();
    }

    private void validateAndRegister(EventType<? extends T> newSubType) {
        //lazy init
        if (subTypes == null)
            subTypes = new WeakHashMap<>();

        for (EventType<? extends T> eventSubType : subTypes.keySet()) {
            if (((eventSubType.name == null && newSubType.name == null) || (eventSubType.name != null && eventSubType.name.equals(newSubType.name)))) {
                throw new IllegalArgumentException("EventType \"" + newSubType + "\"" + " with parent \"" + newSubType.getParentType() + "\" already exists");
            }
        }
        subTypes.put(newSubType, null);
    }
}
