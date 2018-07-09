package com.ibasco.pidisplay.drivers.glcd.enums;

import static com.ibasco.pidisplay.core.ui.U8g2Interface.*;

/**
 * List of supported protocols
 *
 * @author Rafael Ibasco
 */
public enum GlcdProtocol {
    /**
     * 4-Wire SPI
     */
    SPI_4WIRE(COM_4WSPI),
    /**
     * 3-Wire SPI
     */
    SPI_3WIRE(COM_3WSPI),
    /**
     * Parallel Communication: 8080 protocol
     */
    PARALLEL_8080(COM_8080),
    /**
     * Parallel Communication: 6800 protocol
     */
    PARALLEL_6800(COM_6800),
    /**
     * I2C Protocol
     */
    I2C(COM_I2C),
    /**
     * Special ST7920 protocol for SPI communications
     */
    SPI_ST7920(COM_ST7920SPI),
    /**
     * Special KS0108 protocol
     */
    KS0108(COM_KS0108),
    /**
     * Special SED1520 protocol
     */
    SED1520(COM_SED1520),
    /**
     * UART/Serial protocol
     */
    UART(COM_UART);

    private int value;

    GlcdProtocol(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
