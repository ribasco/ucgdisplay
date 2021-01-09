/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
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
package com.ibasco.ucgdisplay.drivers.glcd.enums;

/**
 * Enumeration thaat defines the drawing direction of all strings or glyphs
 *
 * @author Rafael Ibasco
 */
public enum GlcdFontDirection {
    /**
     * 0 Degrees String Rotation
     */
    LEFT_TO_RIGHT(0),
    /**
     * 90 Degrees String Rotation
     */
    TOP_TO_DOWN(1),
    /**
     * 180 Degrees String Rotation
     */
    RIGHT_TO_LEFT(2),
    /**
     * 270 Degreees String Rotation
     */
    DOWN_TO_TOP(3);

    private int value;

    GlcdFontDirection(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
