package com.ibasco.pidisplay.core.system;

/**
 * Contains information regarding Input Device State (e.g. Device plugged/unplugged into/from the system).
 *
 * @author Rafael Ibasco
 */
public class DeviceStateEvent {
    private InputDevice device;
    private String action;

    public static final String DEVICE_ACTION_ADDED = "added";

    public static final String DEVICE_ACTION_REMOVED = "removed";

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeviceStateEvent{");
        sb.append("device=").append(device);
        sb.append(", action='").append(action).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
