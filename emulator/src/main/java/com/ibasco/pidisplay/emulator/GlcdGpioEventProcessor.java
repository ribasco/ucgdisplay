package com.ibasco.pidisplay.emulator;

import com.ibasco.pidisplay.core.gpio.GpioEvent;

abstract public class GlcdGpioEventProcessor {

    protected static final int U8X8_MSG_DELAY_NANO = 0x2c;
    protected static final int U8X8_MSG_GPIO_D0 = 0x40;
    protected static final int U8X8_MSG_GPIO_D1 = 0x41;
    protected static final int U8X8_MSG_GPIO_D2 = 0x42;
    protected static final int U8X8_MSG_GPIO_D3 = 0x43;
    protected static final int U8X8_MSG_GPIO_D4 = 0x44;
    protected static final int U8X8_MSG_GPIO_D5 = 0x45;
    protected static final int U8X8_MSG_GPIO_D6 = 0x46;
    protected static final int U8X8_MSG_GPIO_D7 = 0x47;
    protected static final int U8X8_MSG_GPIO_CS = 0x49;

    protected static final int U8X8_MSG_GPIO_E = 72;
    protected static final int U8X8_MSG_GPIO_CS1 = 78;
    protected static final int U8X8_MSG_GPIO_CS2 = 79;
    protected static final int U8X8_MSG_GPIO_AND_DELAY_INIT = 40;
    protected static final int U8X8_MSG_GPIO_RESET = 75;
    protected static final int U8X8_MSG_GPIO_DC = 74;
    protected static final int U8X8_MSG_DELAY_MILLI = 41;
    protected static final int U8X8_MSG_BYTE_INIT = 20;
    protected static final int U8X8_MSG_BYTE_SEND = 23;
    protected static final int U8X8_MSG_BYTE_START_TRANSFER = 24;
    protected static final int U8X8_MSG_BYTE_END_TRANSFER = 25;
    protected static final int U8X8_MSG_BYTE_SET_DC = 32;
    protected static final int U8X8_MSG_GPIO_I2C_DATA = 77;
    protected static final int U8X8_MSG_GPIO_I2C_CLOCK = 76;
    protected static final int U8X8_MSG_DELAY_I2C = 45;

    private ByteEventHandler byteEventHandler;

    @FunctionalInterface
    public interface ByteEventHandler {
        void onByteEvent(int data);
    }

    public void setByteEventHandler(ByteEventHandler byteHandler) {
        this.byteEventHandler = byteHandler;
    }

    abstract public void processGpioEvent(GpioEvent event);

    protected void onByteEvent(byte[] data) {
        for (byte b : data)
            onByteEvent(b);
    }

    protected void onByteEvent(int data) {
        if (byteEventHandler != null) {
            byteEventHandler.onByteEvent(data);
        }
    }
}
