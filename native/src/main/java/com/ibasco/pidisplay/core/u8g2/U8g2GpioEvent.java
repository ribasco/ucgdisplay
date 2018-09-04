package com.ibasco.pidisplay.core.u8g2;

public class U8g2GpioEvent extends U8g2MessageEvent {
    public U8g2GpioEvent(byte msg, byte value) {
        super(msg, value);
    }

    public U8g2GpioEvent(int msg, int value) {
        super(msg, value);
    }

    public U8g2GpioEvent(U8g2Message message, int value) {
        super(message, value);
    }
}
