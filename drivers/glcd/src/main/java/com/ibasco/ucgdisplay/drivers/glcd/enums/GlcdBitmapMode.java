/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
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
 * Enumeration for the bitmap mode (Switch between solid or transparent)
 *
 * @author Rafael Ibasco
 *
 * @see <a href="https://github.com/olikraus/u8g2/wiki/u8g2reference#setbitmapmode">U8g2 documentation (setBitmapMode)</a>
 */
public enum GlcdBitmapMode {
    SOLID(0),
    TRANSPARENT(1);

    private int value;

    GlcdBitmapMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
