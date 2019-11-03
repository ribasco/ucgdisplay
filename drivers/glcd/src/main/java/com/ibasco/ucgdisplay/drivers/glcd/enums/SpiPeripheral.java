/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: SpiPeripheral.java
 * 
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 - 2019 Universal Character/Graphics display library
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

import com.ibasco.ucgdisplay.drivers.glcd.GlcdOptionValueInt;

/**
 * <p>The enumeration of all available SPI peripherals of the SoC (e.g. Raspberry Pi)</p>
 *
 * @author Rafael Ibasco
 */
public enum SpiPeripheral implements GlcdOptionValueInt {
    /**
     * <p>Main SPI Channel</p>
     *
     * <pre>
     * MISO = 9
     * MOSI = 10
     * SCLK = 11
     * CE0 = 8
     * CE1 = 7
     * CE2 = -
     * </pre>
     */
    MAIN(0),

    /**
     * <p>Auxillary SPI Channel</p>
     *
     * <pre>
     * MISO = 19
     * MOSI = 20
     * SCLK = 21
     * CE0 = 18
     * CE1 = 17
     * CE2 = 16
     * </pre>
     */
    AUXILLARY(1);

    private int channel;

    SpiPeripheral(int channel) {
        this.channel = channel;
    }

    @Override
    public int toValueInt() {
        return channel;
    }
}
