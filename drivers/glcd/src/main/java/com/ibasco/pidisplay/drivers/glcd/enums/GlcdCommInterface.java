package com.ibasco.pidisplay.drivers.glcd.enums;

import static com.ibasco.pidisplay.core.u8g2.U8g2Graphics.*;
import static com.ibasco.pidisplay.drivers.glcd.enums.GlcdCommType.HARDWARE;
import static com.ibasco.pidisplay.drivers.glcd.enums.GlcdCommType.SOFTWARE;

/**
 * List of supported Communication Methods
 *
 * @author Rafael Ibasco
 */
public enum GlcdCommInterface {
    /**
     * 4-Wire SPI (Hardware Implementation)
     */
    SPI_HW_4WIRE(COM_4WSPI, HARDWARE),

    /**
     * 4-Wire SPI (Software Implementation)
     */
    SPI_SW_4WIRE(COM_4WSPI, SOFTWARE),

    /**
     * 4-Wire SPI (Hardware Implementation)
     * <p>
     * Note: mostly identical to {@link #SPI_HW_4WIRE}, but does not use DC
     * </p>
     */
    SPI_HW_4WIRE_ST7920(COM_ST7920SPI, HARDWARE, SPI_HW_4WIRE),

    /**
     * 4-Wire SPI (Software Implementation)
     * <p>
     * Note: mostly identical to {@link #SPI_SW_4WIRE}, but does not use DC
     * </p>
     */
    SPI_SW_4WIRE_ST7920(COM_ST7920SPI, SOFTWARE, SPI_SW_4WIRE),

    /**
     * 3-Wire SPI (Software Implementation)
     */
    SPI_SW_3WIRE(COM_3WSPI, SOFTWARE),

    /**
     * I2C Protocol (Software Implementation)
     */
    I2C_SW(COM_I2C, SOFTWARE),

    /**
     * I2C Hardware (Hardware Implementation)
     */
    I2C_HW(COM_I2C, HARDWARE),

    /**
     * Serial communication (Hardware Implementation)
     */
    SERIAL_HW(COM_UART, HARDWARE),

    /**
     * Serial communication (Software Implementation)
     */
    SERIAL_SW(COM_UART, SOFTWARE),

    /**
     * Parallel Communication: 8080 protocol
     */
    PARALLEL_8080(COM_8080, SOFTWARE),

    /**
     * Parallel Communication: 6800 protocol
     */
    PARALLEL_6800(COM_6800, SOFTWARE),

    /**
     * Special KS0108 protocol
     *
     * <p>
     * Note: Mostly identical to {@link #PARALLEL_6800}, but has more chip select lines
     * </p>
     */
    PARALLEL_6800_KS0108(COM_KS0108, SOFTWARE, PARALLEL_6800),

    /**
     * Special SED1520 protocol
     */
    SED1520(COM_SED1520, SOFTWARE);

    private GlcdCommType type;

    private int value;

    private GlcdCommInterface parent;

    GlcdCommInterface(int value, GlcdCommType type) {
        this(value, type, null);
    }

    GlcdCommInterface(int value, GlcdCommType type, GlcdCommInterface parent) {
        this.value = value;
        this.type = type;
        this.parent = parent;
    }

    public GlcdCommInterface getParent() {
        return parent;
    }

    public GlcdCommType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
