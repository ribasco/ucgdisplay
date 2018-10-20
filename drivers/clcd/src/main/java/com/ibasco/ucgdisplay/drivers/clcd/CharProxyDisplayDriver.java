/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Character LCD Driver
 * Filename: CharProxyDisplayDriver.java
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
package com.ibasco.ucgdisplay.drivers.clcd;

import com.ibasco.ucgdisplay.drivers.clcd.enums.ScrollDirection;
import com.ibasco.ucgdisplay.drivers.clcd.enums.TextDirection;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * <p>A proxy interface which allows control of the LCD via external microcontrollers (e.g. Arduino).</p>
 * <p>
 * Note: This class is NOT thread safe.
 *
 * @author Rafael Ibasco
 */
public class CharProxyDisplayDriver implements CharDisplayDriver {
    private static final Logger log = LoggerFactory.getLogger(CharProxyDisplayDriver.class);

    //Available I2C LCD Instructions
    private static final int CMD_HOME = 0x1;
    private static final int CMD_DISPLAY = 0x2;
    private static final int CMD_BLINK = 0x3;
    private static final int CMD_CURSOR = 0x4;
    private static final int CMD_AUTOSCROLL = 0x5;
    private static final int CMD_SCROLL_DISPLAY = 0x6;
    private static final int CMD_TEXT_DIRECTION = 0x7;
    private static final int CMD_CREATE_CHAR = 0x8;
    private static final int CMD_CLEAR = 0x9;
    private static final int CMD_SET_CURSOR = 0x10;
    private static final int CMD_WRITE = 0x11;

    private I2CDevice device;

    private I2CBus bus;

    private int width;

    private int height;

    private int address;

    public CharProxyDisplayDriver(int address, int width, int height) throws IOException {
        try {
            this.address = address;
            bus = I2CFactory.getInstance(1);
            this.width = width;
            this.height = height;
            initDevice();
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            throw new IOException("Invalid Bus Number", e);
        }
    }

    private void initDevice() throws IOException {
        this.device = bus.getDevice(address);
    }

    @Override
    public void home() {
        send(CMD_HOME, true);
    }

    @Override
    public void display(boolean state) {
        send(CMD_DISPLAY, state);
    }

    @Override
    public void blink(boolean state) {
        send(CMD_BLINK, state);
    }

    @Override
    public void cursor(boolean state) {
        send(CMD_CURSOR, state);
    }

    @Override
    public void autoscroll(boolean state) {
        send(CMD_AUTOSCROLL, state);
    }

    @Override
    public void scrollDisplay(ScrollDirection scrollDirection) {
        send(CMD_SCROLL_DISPLAY, scrollDirection == ScrollDirection.LEFT);
    }

    @Override
    public void textDirection(TextDirection textDirection) {
        send(CMD_TEXT_DIRECTION, textDirection == TextDirection.LEFT_TO_RIGHT);
    }

    @Override
    public void createChar(int num, byte[] charData) {
        if (charData == null)
            throw new NullPointerException("Char data must not be null");
        if (charData.length != 8)
            throw new IllegalArgumentException("Invalid char data size");
        if (num > 7)
            throw new IllegalArgumentException("Invalid character location. Number must be between 0 to 7");
        byte[] payload = new byte[9];
        payload[0] = (byte) num;
        System.arraycopy(charData, 0, payload, 1, 8);
        send(CMD_CREATE_CHAR, payload);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void clear() {
        send(CMD_CLEAR, true);
    }

    @Override
    public void setCursor(int x, int y) {
        send(CMD_SET_CURSOR, (byte) x, (byte) y);
    }

    @Override
    public void write(byte... data) {
        send(CMD_WRITE, data);
    }

    public I2CDevice getDevice() {
        return device;
    }

    private void send(int cmd, boolean value) {
        send(cmd, (byte) ((value) ? 1 : 0));
    }

    private void send(int cmd, byte... data) {
        try {
            //Write to buffer
            ByteBuffer buffer = ByteBuffer.allocate(5 + data.length).order(ByteOrder.LITTLE_ENDIAN);
            buffer.put((byte) cmd); //header (1 byte)
            buffer.put((byte) 0); //command flags
            buffer.putShort((short) data.length); //size (2 bytes)
            buffer.put(data);
            buffer.put((byte) 0x0A);
            buffer.flip();

            //Read the buffer
            byte[] out = new byte[buffer.remaining()];
            buffer.get(out);
            device.write(out);

            Thread.sleep(10);
        } catch (IOException e) {
            if ("Remote I/O error".equalsIgnoreCase(e.getMessage())) {
                try {
                    log.warn("An error has occured. Re-initializing I2C Device (Data discarded)");
                    initDevice();
                } catch (IOException e1) {
                    throw new RuntimeException("Error while sending data to I2C Slave", e1);
                }
            }
        } catch (InterruptedException e) {
            log.error("Interrupted", e);
        }
    }
}
