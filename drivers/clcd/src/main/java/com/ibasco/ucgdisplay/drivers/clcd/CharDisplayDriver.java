/*-
 * ========================START=================================
 * UCGDisplay :: Character LCD driver
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
package com.ibasco.ucgdisplay.drivers.clcd;


import com.ibasco.ucgdisplay.common.drivers.DisplayDriver;
import com.ibasco.ucgdisplay.drivers.clcd.enums.ScrollDirection;
import com.ibasco.ucgdisplay.drivers.clcd.enums.TextDirection;

/**
 * Interface for Character based devices (e.e. HD44780)
 *
 * @author Rafael Ibasco
 */
public interface CharDisplayDriver extends DisplayDriver {
    /**
     * Resets the cursor to its original position
     */
    void home();

    /**
     * Turn on/off the LCD display
     *
     * @param state
     *         <code>true</code> to turn on display
     */
    void display(boolean state);

    void blink(boolean state);

    void cursor(boolean state);

    void autoscroll(boolean state);

    void scrollDisplay(ScrollDirection scrollDirection);

    void textDirection(TextDirection textDirection);

    void createChar(int num, byte[] charData);

    /**
     * Not applicable for this driver type
     */
    @Override
    default long getId() {
        return -1;
    }
}
