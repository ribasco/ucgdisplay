package com.ibasco.ucgdisplay.drivers.clcd.enums;

/**
 * Enumeration for the Operation Type of the LCD Interface (4-bit or 8-bit Mode)
 */
public enum LcdOperationMode {
    /**
     * LCD 4-Bit Operating Mode. Only 4 Data Pins are used for LCD data transfer.
     */
    FOURBIT((byte) 0x0),
    /**
     * LCD 8-Bit Operating Mode. All 8 Data Pins are used for LCD data transfer.
     */
    EIGHTBIT((byte) 0x10);

    private byte value;

    LcdOperationMode(byte value) {
        this.value = value;
    }

    /**
     * Returns the value of the operation mode enumeration
     *
     * @return The byte value representing the lcd operation mode
     */
    public byte getValue() {
        return value;
    }
}
