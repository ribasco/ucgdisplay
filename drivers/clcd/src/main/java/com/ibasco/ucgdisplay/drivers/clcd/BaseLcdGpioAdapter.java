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
import com.ibasco.ucgdisplay.drivers.clcd.exceptions.InvalidPinMappingException;
import com.pi4j.io.gpio.Pin;

/**
 * Base class for {@link LcdGpioAdapter}
 *
 * @author Rafael Ibasco
 */
abstract public class BaseLcdGpioAdapter implements LcdGpioAdapter {
    private LcdPinMapConfig pinMapConfig;

    public BaseLcdGpioAdapter(LcdPinMapConfig pinMapConfig) {
        this.pinMapConfig = pinMapConfig;
        validate(this.pinMapConfig);
    }

    /**
     * Verify that we have the required lcd pins mapped
     *
     * @throws IllegalArgumentException
     *         Thrown if one of the required LCD Pins are not mapped
     */
    abstract protected void validate(LcdPinMapConfig pinMapConfig) throws InvalidPinMappingException;

    /**
     * Check if the {@link LcdPin} is mapped to a {@link Pin}
     *
     * @param pin
     *         The {@link LcdPin} to check
     *
     * @return Returns {@code true} if the {@link LcdPin} is mapped
     */
    protected boolean isMapped(LcdPin pin) {
        return pinMapConfig.isMapped(pin);
    }

    /**
     * Retrieve the mapped {@link LcdPin}
     *
     * @param pin
     *         The {@link LcdPin} to use for lookup
     *
     * @return Returns the mapped {@link Pin} instance
     */
    protected Pin getMappedPin(LcdPin pin) {
        return pinMapConfig.getMappedPin(pin);
    }
}
