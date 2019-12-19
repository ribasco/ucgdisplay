/*-
 * ========================START=================================
 * UCGDisplay :: Character LCD driver
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
package com.ibasco.ucgdisplay.drivers.clcd.enums;

/**
 * Enumeration for the Operation Type of the LCD Interface (4-bit or 8-bit Mode)
 */
public enum LcdOperationMode {
    /**
     * LCD 4-Bit Operating Mode. Only 4 Data Pins are used for LCD data transfer.
     */
    FOURBIT((byte) 0x0),
    /**
     * LCD 8-Bit Operating Mode. All 8 Data Pins are used for LCD data transfer.
     */
    EIGHTBIT((byte) 0x10);

    private byte value;

    LcdOperationMode(byte value) {
        this.value = value;
    }

    /**
     * Returns the value of the operation mode enumeration
     *
     * @return The byte value representing the lcd operation mode
     */
    public byte getValue() {
        return value;
    }
}
