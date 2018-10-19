/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Character LCD Driver
 * Filename: LcdInstruction.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
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
package com.ibasco.ucgdisplay.drivers.clcd.enums;

public enum LcdInstruction {
    /**
     * Sets the core functions of the LCD Display (e.g. Setting to 4/8 bit mode etc)
     */
    DISPLAY_FUNCTION,
    /**
     * Sets the mode of display on the LCD (e.g. Setting autoscroll, Text flow direction etc)
     */
    DISPLAY_MODE,
    /**
     * Set Control functions such as (Setting Cursor On/Off etc)
     */
    DISPLAY_CONTROL
}
