package com.ibasco.ucgdisplay.core.events;

import com.ibasco.ucgdisplay.core.Event;
import com.ibasco.ucgdisplay.core.EventType;
import com.ibasco.ucgdisplay.core.system.InputDevice;
import com.ibasco.ucgdisplay.core.system.RawInputEvent;

import java.time.LocalDateTime;
import java.util.Objects;

public class InputEvent extends Event {
    public static final EventType<InputEvent> ANY = new EventType<>(Event.ANY, "INPUT_ANY");

    public static final EventType<InputEvent> RAW_INPUT = new EventType<>(ANY, "RAW_INPUT");

    private RawInputEvent rawInputEventData;

    public InputEvent(EventType<? extends Event> eventType, RawInputEvent rawInputData) {
        super(eventType);
        this.rawInputEventData = rawInputData;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EventType<? extends InputEvent> getEventType() {
        return (EventType<? extends InputEvent>) super.getEventType();
    }

    public LocalDateTime getInputEventDateTime() {
        return rawInputEventData.getDateTime();
    }

    public int getValue() {
        return rawInputEventData.getValue();
    }

    public int getInputEventCode() {
        return rawInputEventData.getCode();
    }

    public int getInputEventType() {
        return rawInputEventData.getType();
    }

    public InputDevice getInputDevice() {
        return rawInputEventData.getDevice();
    }

    public int getRepeatCount() {
        return rawInputEventData.getRepeatCount();
    }

    public RawInputEvent getInputEventData() {
        return rawInputEventData;
    }

    @Override
    public String toString() {
        return super.toString() + " [" + Objects.toString(rawInputEventData, "N/A") + "]";
    }
}
