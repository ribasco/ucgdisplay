package com.ibasco.pidisplay.core.system;

/**
 * Contains information regarding Input Device State (e.g. Device plugged/unplugged into/from the system).
 *
 * @author Rafael Ibasco
 */
public class DeviceStateEvent {
    private InputDevice device;
    private String action;

    public DeviceStateEvent(InputDevice device, String action) {
        this.device = device;
        this.action = action;
    }

    public InputDevice getDevice() {
        return device;
    }

    public String getAction() {
        return action;
    }
}
