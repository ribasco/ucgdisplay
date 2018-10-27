/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Character LCD driver
 * Filename: MCP23017GpioProviderExt.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
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
package com.ibasco.ucgdisplay.drivers.clcd.providers;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.lang.reflect.Field;

public class MCP23017GpioProviderExt extends com.pi4j.gpio.extension.mcp.MCP23017GpioProvider {
    private static final int REGISTER_GPIO_A = 0x12;
    private I2CDevice device;

    public MCP23017GpioProviderExt(int busNumber, int address) throws I2CFactory.UnsupportedBusNumberException, IOException {
        this(busNumber, address, DEFAULT_POLLING_TIME);
    }

    public MCP23017GpioProviderExt(int busNumber, int address, int pollingTime) throws IOException, I2CFactory.UnsupportedBusNumberException {
        this(I2CFactory.getInstance(busNumber), address, pollingTime);
    }

    public MCP23017GpioProviderExt(I2CBus bus, int address) throws IOException {
        this(bus, address, DEFAULT_POLLING_TIME);
    }

    public MCP23017GpioProviderExt(I2CBus bus, int address, int pollingTime) throws IOException {
        super(bus, address, pollingTime);
        try {
            Field deviceField = MCP23017GpioProviderExt.class.getSuperclass().getDeclaredField("device");
            deviceField.setAccessible(true);
            this.device = (I2CDevice) deviceField.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IOException("Unable to retrieve device instance from super-class");
        }
    }

    public void setState(short state) throws IOException {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) (state & 0xFF);
        buffer[1] = (byte) ((state >> 8) & 0xFF);
        device.write(REGISTER_GPIO_A, buffer);
    }

    public short getState() throws IOException {
        short state = 0;
        byte[] buffer = new byte[2];
        if (device.read(REGISTER_GPIO_A, buffer, 0, buffer.length) > 0)
            state = (short) ((buffer[1] << 8) + (buffer[0] & 0xff));
        return state;
    }
}
