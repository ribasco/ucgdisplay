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
