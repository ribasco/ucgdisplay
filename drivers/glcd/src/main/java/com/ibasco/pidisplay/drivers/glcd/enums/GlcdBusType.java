package com.ibasco.pidisplay.drivers.glcd.enums;

public enum GlcdBusType {
    HARDWARE(0),
    SOFTWARE(1);

    private int value;

    GlcdBusType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
