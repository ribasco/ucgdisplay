package com.ibasco.pidisplay.drivers.glcd.enums;

public enum GlcdCommType {
    HARDWARE(0),
    SOFTWARE(1);

    private int value;

    GlcdCommType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
