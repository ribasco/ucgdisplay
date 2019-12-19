/*-
 * ========================START=================================
 * UCGDisplay :: Common
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
package com.ibasco.ucgdisplay.common.drivers;

/**
 * Base interface for all Display Devices
 *
 * @author Rafael Ibasco
 */
public interface DisplayDriver {
    /**
     * <p>Returns the height of the display.</p>
     *
     * @return The height of the display.
     *
     * @see #getWidth()
     */
    int getHeight();

    /**
     * <p>Returns the width of the display.</p>
     *
     * @return The width of the display.
     *
     * @see #getWidth()
     */
    int getWidth();

    /**
     * <p>Clears the display screen</p>
     */
    void clear();

    /**
     * <p>Sets the current position on the display</p>
     *
     * @param x
     *         The X-coordinate
     * @param y
     *         The Y-coordinate
     */
    void setCursor(int x, int y);

    /**
     * <p>Writes data to the display</p>
     *
     * @param data
     *         The byte(s) to send to the display
     */
    void write(byte... data);

    /**
     * Returns a unique identifier for this driver instance
     *
     * @return An unsigned long reperesenting the driver id
     */
    long getId();
}
