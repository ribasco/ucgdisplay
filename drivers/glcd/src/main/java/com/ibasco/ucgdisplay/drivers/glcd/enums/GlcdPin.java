/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2021 Universal Character/Graphics display library
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

import java.util.Arrays;
import java.util.List;

/**
 * <p>All possible PIN configurations that can be found on your Graphics LCD device</p>
 * <blockquote>
 * Note: Most of these pins are equivalent to the U8G2 constants found in it's header file:
 * </blockquote>
 *
 * <pre>
 *      U8X8_PIN_D0 0
 *      U8X8_PIN_SPI_CLOCK 0
 *      U8X8_PIN_D1 1
 *      U8X8_PIN_SPI_DATA 1
 *      U8X8_PIN_D2 2
 *      U8X8_PIN_D3 3
 *      U8X8_PIN_D4 4
 *      U8X8_PIN_D5 5
 *      U8X8_PIN_D6 6
 *      U8X8_PIN_D7 7
 *      U8X8_PIN_E 8
 *      U8X8_PIN_CS 9              //parallel, SPI
 *      U8X8_PIN_DC 10             // parallel, SPI
 *      U8X8_PIN_RESET 11          // parallel, SPI, I2C
 *      U8X8_PIN_I2C_CLOCK 12      // 1 = Input/high impedance, 0 = drive low
 *      U8X8_PIN_I2C_DATA 13       // 1 = Input/high impedance, 0 = drive low
 *      U8X8_PIN_CS1 14            // KS0108 extra chip select
 *      U8X8_PIN_CS2 15            //KS0108 extra chip select
 * </pre>
 *
 * @author Rafael Ibasco
 */
public enum GlcdPin {
    /**
     * Data pin 0 (also known as SPI CLOCK)
     */
    D0(0, "Data Pin 0"),
    /**
     * Data pin 1 (also known as SPI DATA)
     */
    D1(1, "Data Pin 1"),
    /**
     * Data Pin 2
     */
    D2(2, "Data Pin 2"),
    /**
     * Data Pin 3
     */
    D3(3, "Data Pin 3"),
    /**
     * Data Pin 4
     */
    D4(4, "Data Pin 4"),
    /**
     * Data Pin 5
     */
    D5(5, "Data Pin 5"),
    /**
     * Data Pin 6
     */
    D6(6, "Data Pin 6"),
    /**
     * Data Pin 7
     */
    D7(7, "Data Pin 7"),
    /**
     * Enable Pin
     */
    EN(8, "Enable/Clock Pin"),
    /**
     * Chip Select Pin
     */
    CS(9, "Chip Select Pin"),
    /**
     * Data/Command Pin
     */
    DC(10, "Data/Command Pin"),
    /**
     * Reset Pin
     */
    RESET(11, "Reset Pin"),
    /**
     * I2C Clock Pin (SCL)
     */
    I2C_CLOCK(12, "I2C Clock"),
    /**
     * I2C Data Pin (SDA)
     */
    I2C_DATA(13, "I2C Data"),
    /**
     * Chip Select 1 (KS0108 extra chip select)
     */
    CS1(14, "Chip Select 1 (KS0108)"),
    /**
     * Chip Select 2 (KS0108 extra chip select)
     */
    CS2(15, "Chip Select 2 (KS0108)"),
    /**
     * Alias for D0. Added for convenience
     */
    SPI_CLOCK(D0, "SPI Clock (Alias for D0)"),
    /**
     * Alias for D1. Added for convenience
     */
    SPI_MOSI(D1, "SPI MOSI (Alias for D1)"),
    /**
     * Switch between Parallel or Serial bus interface
     */
    PSB(20, "Parallel/Serial Select Pin");

    private final int index;

    private GlcdPin parent;

    private final String description;

    GlcdPin(int index, String description) {
        this.index = index;
        this.description = description;
    }

    GlcdPin(GlcdPin parent, String description) {
        this(parent.index, description);
        this.parent = parent;
    }

    /**
     * @return Name of the enumeration
     */
    public String getName() {
        return this.name();
    }

    /**
     * @return The pin description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The device pin index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return <code>True</code> if the enum is an alias of another {@link GlcdPin}
     */
    public boolean isAlias() {
        return this.parent != null;
    }

    /**
     * @return The parent {@link GlcdPin} instance
     */
    public GlcdPin getParent() {
        return this.parent;
    }

    /**
     * @return A {@link List} of the values of the {@link GlcdPin} enumeration
     */
    public static List<GlcdPin> getPins() {
        return Arrays.asList(GlcdPin.values());
    }
}
