/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Character LCD driver
 * Filename: InvalidPinMappingException.java
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
package com.ibasco.ucgdisplay.drivers.clcd.exceptions;


import com.ibasco.ucgdisplay.drivers.clcd.LcdPinMapConfig;

/**
 * Thrown to indicate an invalid pin mappingConfig configuration for the LCD Interface
 *
 * @author Rafael Ibasco
 */
public class InvalidPinMappingException extends RuntimeException {

    private final LcdPinMapConfig mappingConfig;

    public InvalidPinMappingException(String message, LcdPinMapConfig mappingConfig) {
        super(message);
        this.mappingConfig = mappingConfig;
    }

    public InvalidPinMappingException(LcdPinMapConfig mappingConfig) {
        super(String.format("Invalid pin mapping configuration found. Make sure you have the right " +
                "pins for the Provider you specified (Pins: %s)", mappingConfig.toString()));
        this.mappingConfig = mappingConfig;
    }

    public LcdPinMapConfig getMappingConfig() {
        return mappingConfig;
    }
}
