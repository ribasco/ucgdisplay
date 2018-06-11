package com.ibasco.pidisplay.core.system;

import java.util.List;
import java.util.Objects;

/**
 * A class containing most of the necessary information regarding an Input Device of the system.
 *
 * @author Rafael Ibasco
 */
public class InputDevice {
    private String name;
    private String devicePath;
    private String driverVersion;
    private InputDeviceId id;
    private List<InputEventType> eventTypes;

    public InputDevice(String name, String path, short[] ids, String driverVersion, List<InputEventType> eventTypes) {
        //super(fd);
        this.name = name;
        this.devicePath = path;
        this.driverVersion = driverVersion;
        this.id = new InputDeviceId(ids);
        this.eventTypes = eventTypes;
    }

    public String getName() {
        return name;
    }

    public InputDeviceId getId() {
        return id;
    }

    public String getDriverVersion() {
        return this.driverVersion;
    }

    public String getDevicePath() {
        return devicePath;
    }

    public List<InputEventType> getEventTypes() {
        return eventTypes;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("InputDevice{");
        sb.append("name='").append(name).append('\'');
        sb.append(", devicePath='").append(devicePath).append('\'');
        sb.append(", driverVersion='").append(driverVersion).append('\'');
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputDevice that = (InputDevice) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
