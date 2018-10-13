package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.system.DeviceStateEvent;

public class InputDeviceEvent extends Event {

    public static final EventType<InputDeviceEvent> ANY = new EventType<>(Event.ANY, "DEVICE_ANY");

    public static final EventType<InputDeviceEvent> DEVICE_ADDED = new EventType<>(ANY, "DEVICE_ADDED");

    public static final EventType<InputDeviceEvent> DEVICE_REMOVED = new EventType<>(ANY, "DEVICE_REMOVED");

    private DeviceStateEvent deviceStateEvent;

    public InputDeviceEvent(EventType<? extends Event> eventType, DeviceStateEvent deviceStateEvent) {
        super(eventType);

        this.deviceStateEvent = deviceStateEvent;
    }

    public DeviceStateEvent getDeviceEventData() {
        return deviceStateEvent;
    }
}
