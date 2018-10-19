/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Character LCD Driver
 * Filename: LcdPin.java
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

/**
 * Enumeration for LCD Pins.
 *
 * Note: Order is important, DO NOT CHANGE!
 *
 * @author Rafael Ibasco
 */
public enum LcdPin {
    /**
     * Data Pin 0.
     * Note: Only Used in 8-bit mode.
     */
    DATA_0("LCD - Data 0", false),
    /**
     * Data Pin 1.
     * Note: Only Used in 8-bit mode.
     */
    DATA_1("LCD - Data 1", false),
    /**
     * Data Pin 2.
     * Note: Only Used in 8-bit mode.
     */
    DATA_2("LCD - Data 2", false),
    /**
     * Data Pin 3.
     * Note: Only used in 8-bit mode.
     */
    DATA_3("LCD - Data 3", false),
    /**
     * Data Pin 4
     */
    DATA_4("LCD - Data 4", true),
    /**
     * Data Pin 5
     */
    DATA_5("LCD - Data 5", true),
    /**
     * Data Pin 6
     */
    DATA_6("LCD - Data 6", true),
    /**
     * Data Pin 7
     */
    DATA_7("LCD - Data 7", true),
    /**
     * Register Select Pin
     */
    RS("LCD - Register Select", true),
    /**
     * Enable Pin
     */
    EN("LCD - Enable", true),
    /**
     * Read/Write Pin.
     * Note: This pin is rarely used and preferably should be tied to ground and be excluded from the pin map.
     */
    RW("LCD - Read/Write", false),
    /**
     * LCD Backlight Pin
     */
    BACKLIGHT("LCD - Backlight", false);

    private String name;
    private boolean required;

    LcdPin(String name, boolean required) {
        this.name = name;
        this.required = required;
    }

    /**
     * @return Returns {@code true} if the {@link LcdPin} is required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Get Data Pins used in 4-Bit Operation
     *
     * @return Returns all data pins from DATA 4 to DATA 7
     */
    public static LcdPin[] getFourBitDataPins() {
        return new LcdPin[]{DATA_4, DATA_5, DATA_6, DATA_7};
    }

    /**
     * Get all Data Pins
     *
     * @return Returns all data pins from DATA 0 to DATA 7
     */
    public static LcdPin[] getAllDataPins() {
        return new LcdPin[]{DATA_0, DATA_1, DATA_2, DATA_3, DATA_4, DATA_5, DATA_6, DATA_7};
    }

    /**
     * Get all {@link LcdPin}
     *
     * @return Returns all pins
     */
    public static LcdPin[] getAllPins() {
        return new LcdPin[]{RS, EN, RW, DATA_0, DATA_1, DATA_2, DATA_3, DATA_4, DATA_5, DATA_6, DATA_7, BACKLIGHT/*, BUTTON_1, BUTTON_2, BUTTON_3, BUTTON_4*/};
    }

    /**
     * Get the Name of the {@link LcdPin}
     *
     * @return The name associated with the {@link LcdPin}
     */
    public String getName() {
        return this.name;
    }
}
