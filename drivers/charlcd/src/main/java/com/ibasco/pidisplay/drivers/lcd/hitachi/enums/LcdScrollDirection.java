package com.ibasco.pidisplay.drivers.lcd.hitachi.enums;

public enum LcdScrollDirection {
    LEFT((byte) 0x00),
    RIGHT((byte) 0x04);

    private byte value;

    LcdScrollDirection(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
