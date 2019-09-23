/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Character LCD driver
 * Filename: LcdTemplates.java
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
package com.ibasco.ucgdisplay.drivers.clcd;

import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdPin;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;

/**
 * Provided as a convenience for popular lcd modules. This contains their pin mapping configuration
 *
 * @author Rafael Ibasco
 */
public class LcdTemplates {
    /**
     * Pin mapping configuration for the popular adafruit LCD I2C using MCP23017
     */
    public static final LcdPinMapConfig ADAFRUIT_I2C_RGBLCD_MCP23017 = new LcdPinMapConfig()
            .map(LcdPin.RS, MCP23017Pin.GPIO_B7)
            .map(LcdPin.EN, MCP23017Pin.GPIO_B5)
            .map(LcdPin.DATA_4, MCP23017Pin.GPIO_B4)
            .map(LcdPin.DATA_5, MCP23017Pin.GPIO_B3)
            .map(LcdPin.DATA_6, MCP23017Pin.GPIO_B2)
            .map(LcdPin.DATA_7, MCP23017Pin.GPIO_B1)
            .map(LcdPin.BACKLIGHT, MCP23017Pin.GPIO_B6);
            /*.map(LcdPin.BUTTON_1, MCP23017Pin.GPIO_A0)
            .map(LcdPin.BUTTON_2, MCP23017Pin.GPIO_A1)
            .map(LcdPin.BUTTON_3, MCP23017Pin.GPIO_A2)
            .map(LcdPin.BUTTON_4, MCP23017Pin.GPIO_A3)*/
}
