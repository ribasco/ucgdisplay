/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
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

import com.ibasco.ucgdisplay.core.u8g2.U8g2Graphics;

/**
 * Enumeration of a bus interface data transport proedure types
 *
 * @author Rafael Ibasco
 */
public enum GlcdBusType {
    /**
     * Hardware specific features are used for data-transport between devices
     */
    HARDWARE(U8g2Graphics.BUS_HARDWARE),

    /**
     * Software bit-banging procedures are used for data-transport between devices
     */
    SOFTWARE(U8g2Graphics.BUS_SOFTWARE);

    private int value;

    GlcdBusType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
