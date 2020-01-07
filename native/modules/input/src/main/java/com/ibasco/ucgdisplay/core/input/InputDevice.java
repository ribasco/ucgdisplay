/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Input
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
package com.ibasco.ucgdisplay.core.input;

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
