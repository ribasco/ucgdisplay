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

import com.ibasco.ucgdisplay.drivers.glcd.GlcdOptionValue;

public enum GlcdBufferSize implements GlcdOptionValue<Integer> {
    /**
     * Only one page of the display memory is stored in the microcontroller RAM.
     * <p>
     * Use a firstPage()/nextPage() loop for drawing on the display.
     */
    PAGE_BUFFER_1(0, "Page Buffer (One Page)"),

    /**
     * Same as 1, but maintains two pages in the microcontroller RAM.
     * This will be up to two times faster than 1.
     */
    PAGE_BUFFER_2(1, "Page Buffer (Two Pages)"),

    /**
     * Keep a copy of the full display frame buffer in the microcontroller RAM.
     * Use clearBuffer() to clear the RAM and sendBuffer() to transfer the microcontroller RAM to the display.
     */
    FULL_FRAME_BUFFER(2, "Full Frame Buffer");

    private final String name;

    private final int value;

    GlcdBufferSize(int value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Integer toValue() {
        return this.value;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
