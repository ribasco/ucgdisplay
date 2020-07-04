/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
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
 * Enumeration of communication protocols of a display controller
 *
 * @author Rafael Ibasco
 */
public enum GlcdCommProtocol {
    /**
     * 4-Wire SPI (Hardware Implementation)
     */
    SPI_HW_4WIRE(COM_4WSPI, GlcdCommType.HARDWARE, "4-Wire Hardware SPI"),

    /**
     * 4-Wire SPI (Hardware Implementation / If supported, second 4-wire hardware SPI)
     */
    SPI_HW_4WIRE_2ND(COM_4WSPI, GlcdCommType.HARDWARE, "Second 4-Wire Hardware SPI", SPI_HW_4WIRE),

    /**
     * 4-Wire SPI (Software Implementation)
     */
    SPI_SW_4WIRE(COM_4WSPI, GlcdCommType.SOFTWARE, "4-Wire Software SPI"),

    /**
     * 4-Wire SPI (Hardware Implementation)
     * <p>
     * Note: mostly identical to {@link #SPI_HW_4WIRE}, but does not use DC
     * </p>
     */
    SPI_HW_4WIRE_ST7920(COM_ST7920SPI, GlcdCommType.HARDWARE, "4-Wire Hardware SPI (ST7920)", SPI_HW_4WIRE),

    /**
     * 4-Wire SPI (Hardware Implementation)
     * <p>
     * Note: mostly identical to {@link #SPI_HW_4WIRE}, but does not use DC
     * </p>
     */
    SPI_HW_ST7920_2ND(COM_ST7920SPI, GlcdCommType.HARDWARE, "Second Hardware SPI (ST7920)", SPI_HW_4WIRE),

    /**
     * 4-Wire SPI (Software Implementation)
     * <p>
     * Note: mostly identical to {@link #SPI_SW_4WIRE}, but does not use DC
     * </p>
     */
    SPI_SW_4WIRE_ST7920(COM_ST7920SPI, GlcdCommType.SOFTWARE, "4-Wire Software SPI (ST7920)", SPI_SW_4WIRE),

    /**
     * 3-Wire SPI (Software Implementation)
     */
    SPI_SW_3WIRE(COM_3WSPI, GlcdCommType.SOFTWARE, "3-Wire Software SPI"),

    /**
     * I2C Protocol (Software Implementation)
     */
    I2C_SW(COM_I2C, GlcdCommType.SOFTWARE, "I2C Software"),

    /**
     * I2C Hardware (Hardware Implementation)
     */
    I2C_HW(COM_I2C, GlcdCommType.HARDWARE, "I2C Hardware"),

    /**
     * I2C Protocol (Hardware Implementation / If supported, use second hardware I2C)
     */
    I2C_HW_2ND(COM_I2C, GlcdCommType.HARDWARE, "I2C Hardware (Second Interface)", I2C_HW),

    /**
     * Serial communication (Hardware Implementation)
     */
    SERIAL_HW(COM_UART, GlcdCommType.HARDWARE, "Serial Hardware"),

    /**
     * Serial communication (Software Implementation)
     */
    SERIAL_SW(COM_UART, GlcdCommType.SOFTWARE, "Serial Software"),

    /**
     * Parallel Communication: 8080 protocol
     */
    PARALLEL_8080(COM_8080, GlcdCommType.SOFTWARE, "Parallel 8080 (Intel)"),

    /**
     * Parallel Communication: 6800 protocol
     */
    PARALLEL_6800(COM_6800, GlcdCommType.SOFTWARE, "Parallel 6800 (Motorola)"),

    /**
     * Special KS0108 protocol
     *
     * <p>
     * Note: Mostly identical to {@link #PARALLEL_6800}, but has more chip select lines
     * </p>
     */
    PARALLEL_6800_KS0108(COM_KS0108, GlcdCommType.SOFTWARE, "Parallel 6800 (KS0108)", PARALLEL_6800),

    /**
     * Special SED1520 protocol
     */
    SED1520(COM_SED1520, GlcdCommType.SOFTWARE, "SED1520");

    private final GlcdCommType type;

    private final int value;

    private final GlcdCommProtocol parent;

    private final String description;

    GlcdCommProtocol(int value, GlcdCommType type, String description) {
        this(value, type, description, null);
    }

    GlcdCommProtocol(int value, GlcdCommType type, String description, GlcdCommProtocol parent) {
        this.value = value;
        this.type = type;
        this.parent = parent;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public GlcdCommProtocol getParent() {
        return parent;
    }

    public GlcdCommType getBusType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
