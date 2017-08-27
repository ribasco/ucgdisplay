package com.ibasco.pidisplay.drivers.lcd.enums;

public enum LcdCharSize {
    DOTS_5X10(0x04),
    DOTS_5X8(0x00);

    private byte value;

    LcdCharSize(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }
}
