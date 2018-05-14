package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.beans.InputEventData;

import java.util.Objects;

public class RawInputEvent extends Event {
    public static final EventType<RawInputEvent> ANY = new EventType<>(Event.ANY, "INPUT_ANY");

    public static final EventType<RawInputEvent> RAW_INPUT = new EventType<>(ANY, "RAW_INPUT");

    public static final EventType<RawInputEvent> KEY_PRESS = new EventType<>(ANY, "KEY_PRESS");

    public static final EventType<RawInputEvent> MOUSE_DOWN = new EventType<>(ANY, "MOUSE_DOWN");

    public static final EventType<RawInputEvent> MOUSE_UP = new EventType<>(ANY, "MOUSE_UP");

    public static final EventType<RawInputEvent> MOUSE_PRESS = new EventType<>(ANY, "MOUSE_PRESS");

    private InputEventData rawInputEventData;

    public RawInputEvent(EventType<? extends Event> eventType, InputEventData rawInputData) {
        super(eventType);
        this.rawInputEventData = rawInputData;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EventType<? extends RawInputEvent> getEventType() {
        return (EventType<? extends RawInputEvent>) super.getEventType();
    }

    public InputEventData getRawInputEventData() {
        return rawInputEventData;
    }

    @Override
    public String toString() {
        return super.toString() + " [" + Objects.toString(rawInputEventData, "N/A") + "]";
    }
}
