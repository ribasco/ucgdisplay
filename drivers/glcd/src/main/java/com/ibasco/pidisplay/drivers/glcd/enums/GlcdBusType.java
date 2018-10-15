package com.ibasco.pidisplay.drivers.glcd.enums;

import com.ibasco.pidisplay.core.u8g2.U8g2Graphics;

/**
 * Enumeration of a bus interface data transport proedure types
 *
 * @author Rafael Ibasco
 */
public enum GlcdBusType {
    /**
     * Hardware specific features are used for data-transport between devices
     */
    HARDWARE(U8g2Graphics.BUS_HARDWARE),

    /**
     * Software bit-banging procedures are used for data-transport between devices
     */
    SOFTWARE(U8g2Graphics.BUS_SOFTWARE);

    private int value;

    GlcdBusType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
