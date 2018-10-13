package com.ibasco.pidisplay.drivers.lcd.hd44780.providers;

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
