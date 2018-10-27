/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Character LCD driver
 * Filename: Chars.java
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
package com.ibasco.ucgdisplay.drivers.clcd;

/**
 * A collection of pre-definied custom lcd characters
 *
 * @author Rafael Ibasco
 */
public class Chars {
    public static final CharData LEFT_ARROW = new CharData("CD_LEFT_ARROW", "CLA", new byte[]{0x00, 0x04, 0x08, 0x1F, 0x08, 0x04, 0x00, 0x00});
    public static final CharData RIGHT_ARROW = new CharData("CD_RIGHT_ARROW", "CRA", new byte[]{0x00, 0x04, 0x02, 0x1F, 0x02, 0x04, 0x00, 0x00});
    public static final CharData UP_ARROW = new CharData("CD_UP_ARROW", "CUA", new byte[]{0x04, 0x0E, 0x15, 0x04, 0x04, 0x04, 0x04, 0x04});
    public static final CharData DOWN_ARROW = new CharData("CD_DOWN_ARROW", "CDA", new byte[]{0x04, 0x04, 0x04, 0x04, 0x04, 0x15, 0x0E, 0x04});
    public static final CharData BULLET_POINT = new CharData("CD_BULLET_POINT", "CBP", new byte[]{0x00, 0x00, 0x0E, 0x0E, 0x0E, 0x0E, 0x00, 0x00});
    public static final CharData BULLET_POINT_OPEN = new CharData("CD_BULLET_POINT_OPEN", "CBPO", new byte[]{0x1F, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x1F});
    public static final CharData BULLET_POINT_FOCUSED = new CharData("CD_BULLET_POINT_FOCUSED", "CBPF", new byte[]{0x1F, 0x11, 0x1F, 0x1F, 0x1F, 0x1F, 0x11, 0x1F});
    public static final CharData BULLET_POINT_SELECTED = new CharData("CD_BULLET_POINT_SELECTED", "CBPS", new byte[]{0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F, 0x1F});


    public static final CharData BF_BAR1 = new CharData("BIG_FONT_BAR1", "BFB1", new byte[]{0B11100,
            0B11110,
            0B11110,
            0B11110,
            0B11110,
            0B11110,
            0B11110,
            0b11100});
    public static final CharData BF_BAR2 = new CharData("BIG_FONT_BAR2", "BFB2", new byte[]{
            0B00111,
            0B01111,
            0B01111,
            0B01111,
            0B01111,
            0B01111,
            0B01111,
            0B00111});
    public static final CharData BF_BAR3 = new CharData("BIG_FONT_BAR3", "BFB3", new byte[]{0B11111,
            0B11111,
            0B00000,
            0B00000,
            0B00000,
            0B00000,
            0B11111,
            0B11111});
    public static final CharData BF_BAR4 = new CharData("BIG_FONT_BAR4", "BFB4", new byte[]{0B11110,
            0B11100,
            0B00000,
            0B00000,
            0B00000,
            0B00000,
            0B11000,
            0B11100});
    public static final CharData BF_BAR5 = new CharData("BIG_FONT_BAR5", "BFB5", new byte[]{});
    public static final CharData BF_BAR6 = new CharData("BIG_FONT_BAR6", "BFB6", new byte[]{});
    public static final CharData BF_BAR7 = new CharData("BIG_FONT_BAR7", "BFB7", new byte[]{});
    public static final CharData BF_BAR8 = new CharData("BIG_FONT_BAR8", "BFB8", new byte[]{});
}
