package com.ibasco.pidisplay.drivers.lcd.hd44780.enums;

/**
 * @deprecated Use {@link com.ibasco.pidisplay.core.enums.ScrollDirection} instead
 */
@Deprecated
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
