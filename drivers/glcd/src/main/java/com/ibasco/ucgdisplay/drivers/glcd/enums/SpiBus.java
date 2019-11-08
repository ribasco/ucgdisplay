/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: SpiBus.java
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

import com.ibasco.ucgdisplay.drivers.glcd.GlcdOptionValue;
import com.ibasco.ucgdisplay.drivers.glcd.GlcdUserDefinedOption;

/**
 * <p>The enumeration of all available SPI peripherals of the SoC (e.g. Raspberry Pi)</p>
 *
 * @author Rafael Ibasco
 */
public enum SpiBus implements GlcdOptionValue<Integer>, GlcdUserDefinedOption<SpiBus, Integer> {
    /**
     * <p>Main SPI Channel</p>
     *
     * <pre>
     * MISO = 9
     * MOSI = 10
     * SCLK = 11
     * CE0 = 8
     * CE1 = 7
     * CE2 = N/A (avaialble in Auxillary)
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
    AUXILLARY(1),


    OTHER(-1);

    private int channel;

    SpiBus(int channel) {
        this.channel = channel;
    }

    @Override
    public Integer toValue() {
        return channel;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public SpiBus value(Integer value) {
        SpiBus v = OTHER;
        v.channel = value;
        return v;
    }
}
