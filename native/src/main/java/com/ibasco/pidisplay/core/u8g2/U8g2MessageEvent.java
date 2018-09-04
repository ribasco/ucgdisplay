package com.ibasco.pidisplay.core.u8g2;

abstract public class U8g2MessageEvent {
    private final U8g2Message message;
    private final int value;

    public U8g2MessageEvent(byte msg, byte value) {
        this(Byte.toUnsignedInt(msg), Byte.toUnsignedInt(value));
    }

    public U8g2MessageEvent(int msg, int value) {
        this(U8g2Message.valueOf(msg), value);
    }

    public U8g2MessageEvent(U8g2Message message, int value) {
        this.message = message;
        this.value = value;
    }

    public U8g2Message getMessage() {
        return message;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return message + " = " + Integer.toHexString(value).toUpperCase();
    }
}
