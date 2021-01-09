/*-
 * ========================START=================================
 * UCGDisplay :: Character LCD driver
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
package com.ibasco.ucgdisplay.drivers.clcd;

import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdPin;
import com.pi4j.io.gpio.Pin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A utility class to create mapping between {@link LcdPin} and {@link Pin}
 *
 * @author Rafael Ibasco
 */
public class LcdPinMapConfig {
    private HashMap<LcdPin, Pin> pinMap;

    public LcdPinMapConfig() {
        this.pinMap = new HashMap<>();
    }

    public LcdPinMapConfig map(LcdPin lcdPin, Pin pin) {
        pinMap.put(lcdPin, pin);
        return this;
    }

    public Set<Map.Entry<LcdPin, Pin>> getAllPins() {
        return this.pinMap.entrySet();
    }

    boolean isMapped(LcdPin pin) {
        return pinMap.containsKey(pin);
    }

    Pin getMappedPin(LcdPin pin) {
        return pinMap.get(pin);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LcdPin, Pin> pinEntry : pinMap.entrySet()) {
            sb.append(pinEntry.getKey());
            sb.append("=");
            sb.append(pinEntry.getValue());
            sb.append(", ");
        }
        return sb.toString();
    }
}
