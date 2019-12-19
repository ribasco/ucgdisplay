/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
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

/**
 * Enumeration for all available SPI modes
 *
 * @author Rafael Ibasco
 * @see <a href="https://www.allaboutcircuits.com/technical-articles/spi-serial-peripheral-interface/">Serial Peripheral Interface</a>
 */
public enum SpiMode implements GlcdOptionValue<Integer> {
    /**
     * Clock phase is configured such that data is sampled on the rising edge of the clock pulse and shifted out on
     * the falling edge of the clock pulse. Note that data must be available before the first rising edge of the clock.
     */
    MODE_0(0),
    /**
     * Clock phase is configured such that data is sampled on the falling edge of the clock pulse and shifted out on
     * the rising edge of the clock pulse.
     */
    MODE_1(1),
    /**
     * Clock phase is configured such that data is sampled on the falling edge of the clock pulse and shifted out on
     * the rising edge of the clock pulse. Note that data must be available before the first falling edge of the clock.
     */
    MODE_2(2),
    /**
     * Clock phase is configured such that data is sampled on the rising edge of the clock pulse and shifted out on
     * the falling edge of the clock pulse.
     */
    MODE_3(3);

    private int mode;

    SpiMode(int mode) {
        this.mode = mode;
    }

    @Override
    public Integer toValue() {
        return mode;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
