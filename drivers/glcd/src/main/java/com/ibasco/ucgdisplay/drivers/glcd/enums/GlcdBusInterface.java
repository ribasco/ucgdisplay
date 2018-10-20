/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD Driver
 * Filename: GlcdBusInterface.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
package com.ibasco.ucgdisplay.drivers.glcd.enums;

import static com.ibasco.ucgdisplay.core.u8g2.U8g2Graphics.*;

/**
 * Enumeration of supported bus interfaces of a controller
 *
 * @author Rafael Ibasco
 */
public enum GlcdBusInterface {
    /**
     * 4-Wire SPI (Hardware Implementation)
     */
    SPI_HW_4WIRE(COM_4WSPI, GlcdBusType.HARDWARE, "4-Wire HW SPI"),

    /**
     * 4-Wire SPI (Software Implementation)
     */
    SPI_SW_4WIRE(COM_4WSPI, GlcdBusType.SOFTWARE, "4-Wire SW SPI"),

    /**
     * 4-Wire SPI (Hardware Implementation)
     * <p>
     * Note: mostly identical to {@link #SPI_HW_4WIRE}, but does not use DC
     * </p>
     */
    SPI_HW_4WIRE_ST7920(COM_ST7920SPI, GlcdBusType.HARDWARE, "4-Wire HW SPI (ST7920)", SPI_HW_4WIRE),

    /**
     * 4-Wire SPI (Software Implementation)
     * <p>
     * Note: mostly identical to {@link #SPI_SW_4WIRE}, but does not use DC
     * </p>
     */
    SPI_SW_4WIRE_ST7920(COM_ST7920SPI, GlcdBusType.SOFTWARE, "4-Wire SW SPI (ST7920)", SPI_SW_4WIRE),

    /**
     * 3-Wire SPI (Software Implementation)
     */
    SPI_SW_3WIRE(COM_3WSPI, GlcdBusType.SOFTWARE, "3-Wire SW SPI"),

    /**
     * I2C Protocol (Software Implementation)
     */
    I2C_SW(COM_I2C, GlcdBusType.SOFTWARE, "I2C SW"),

    /**
     * I2C Hardware (Hardware Implementation)
     */
    I2C_HW(COM_I2C, GlcdBusType.HARDWARE, "I2C HW"),

    /**
     * Serial communication (Hardware Implementation)
     */
    SERIAL_HW(COM_UART, GlcdBusType.HARDWARE, "Serial HW"),

    /**
     * Serial communication (Software Implementation)
     */
    SERIAL_SW(COM_UART, GlcdBusType.SOFTWARE, "Serial SW"),

    /**
     * Parallel Communication: 8080 protocol
     */
    PARALLEL_8080(COM_8080, GlcdBusType.SOFTWARE, "Parallel 8080 (Intel)"),

    /**
     * Parallel Communication: 6800 protocol
     */
    PARALLEL_6800(COM_6800, GlcdBusType.SOFTWARE, "Parallel 6800 (Motorola)"),

    /**
     * Special KS0108 protocol
     *
     * <p>
     * Note: Mostly identical to {@link #PARALLEL_6800}, but has more chip select lines
     * </p>
     */
    PARALLEL_6800_KS0108(COM_KS0108, GlcdBusType.SOFTWARE, "Parallel 6800 (KS0108)", PARALLEL_6800),

    /**
     * Special SED1520 protocol
     */
    SED1520(COM_SED1520, GlcdBusType.SOFTWARE, "SED1520");

    private GlcdBusType type;

    private int value;

    private GlcdBusInterface parent;

    private String description;

    GlcdBusInterface(int value, GlcdBusType type, String description) {
        this(value, type, description, null);
    }

    GlcdBusInterface(int value, GlcdBusType type, String description, GlcdBusInterface parent) {
        this.value = value;
        this.type = type;
        this.parent = parent;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public GlcdBusInterface getParent() {
        return parent;
    }

    public GlcdBusType getBusType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
