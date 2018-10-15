package com.ibasco.pidisplay.drivers.glcd.enums;

public enum GlcdDrawColor {
    CLEAR(0),
    SOLID(1),
    XOR(2);

    private int value;

    GlcdDrawColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
