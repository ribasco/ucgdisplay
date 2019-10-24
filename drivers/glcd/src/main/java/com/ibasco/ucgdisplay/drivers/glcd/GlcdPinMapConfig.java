/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: GlcdPinMapConfig.java
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
package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdPinMappingException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for mapping a {@link GlcdPin} with device pin number
 *
 * @author Rafael Ibasco
 */
public class GlcdPinMapConfig {
    private Map<GlcdPin, Byte> pinMap;

    public GlcdPinMapConfig() {
        this.pinMap = new HashMap<>();
    }

    /**
     * Maps a {@link GlcdPin} to a device pin number
     *
     * @param glcdPin
     *         A {@link GlcdPin} to be mapped
     * @param value
     *         The gpio line number. Use the 'gpioinfo' utility of libgpiod to display the list of available gpio lines.
     *
     * @return GlcdPinMapConfig
     */
    public GlcdPinMapConfig map(GlcdPin glcdPin, int value) {
        if (value > 255)
            throw new GlcdPinMappingException("Invalid pin number", this);
        pinMap.put(glcdPin, (byte) value);
        return this;
    }

    /**
     * Check if the provided pin is mapped
     *
     * @param pin
     *         The {@link GlcdPin}
     *
     * @return <code>True</code> if the provided {@link GlcdPin} is mapped to a device pin
     */
    boolean isMapped(GlcdPin pin) {
        return pinMap.containsKey(pin);
    }

    /**
     * @return <code>True</code> if the pin map is empty
     */
    boolean isEmpty() {
        return pinMap.isEmpty();
    }

    /**
     * <p>Converts the internal pin map configuration to a primitive byte array which will
     * later be used by the native driver. Each index represents a known device pin.</p>
     *
     * @return A primitive byte array containing the pin mapping information to be used by the low level driver
     */
    byte[] toByteArray() {
        byte[] data = new byte[16];
        if (!pinMap.isEmpty()) {
            for (Map.Entry<GlcdPin, Byte> entry : pinMap.entrySet()) {
                GlcdPin pin = entry.getKey();
                Byte pNum = entry.getValue();
                data[pin.getIndex()] = pNum;
            }
        }
        return data;
    }

    /**
     * <p>Converts the internal pin map configuration to a primitive int array which will
     * later be used by the native driver. Each index represents a known device pin.</p>
     *
     * @return A primitive byte array containing the pin mapping information to be used by the low level driver
     */
    int[] toIntArray() {
        int[] data = new int[16];
        Arrays.fill(data, -1);

        if (!pinMap.isEmpty()) {
            for (Map.Entry<GlcdPin, Byte> entry : pinMap.entrySet()) {
                GlcdPin pin = entry.getKey();
                Byte pNum = entry.getValue();
                data[pin.getIndex()] = pNum;
            }
        }
        return data;
    }
}
