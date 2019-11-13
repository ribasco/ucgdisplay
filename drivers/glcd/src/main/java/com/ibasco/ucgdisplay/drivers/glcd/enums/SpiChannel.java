/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: SpiChannel.java
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
 * Enumeration of all available SPI Channels (Chip-Selects) on the SoC device (Raspberry Pi)
 *
 * @author Rafael Ibasco
 */
public enum SpiChannel implements GlcdOptionValue<Integer>, GlcdUserDefinedOption<SpiChannel, Integer> {
    /**
     * CE0 / Chip Select 0 / Channel 0
     */
    CHANNEL_0(0),
    /**
     * CE1 / Chip Select 1 / Channel 1
     */
    CHANNEL_1(1),
    /**
     * CE2 / Chip Select 2 / Channel 2
     */
    CHANNEL_2(2),

    /**
     * A custom chip select channel. Use this if your device has an SPI channel &gt; 2.
     * <br />
     * <br />
     * Example:
     * <pre>
     *     SpiChannel channel = SpiChannel.OTHER.value(3);
     * </pre>
     */
    OTHER(-1);

    private int channel;

    SpiChannel(int channel) {
        this.channel = channel;
    }

    @Override
    public SpiChannel value(Integer value) {
        SpiChannel spiChannel = OTHER;
        spiChannel.channel = value;
        return spiChannel;
    }

    @Override
    public Integer toValue() {
        return channel;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
