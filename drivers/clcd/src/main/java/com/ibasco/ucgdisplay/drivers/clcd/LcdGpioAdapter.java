/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Character LCD Driver
 * Filename: LcdGpioAdapter.java
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

import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdReadWriteState;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdRegisterSelectState;
import com.pi4j.io.gpio.PinState;

import java.io.IOException;

/**
 * The GPIO Adapter for the LCD.
 *
 * @author Rafael Ibasco
 */
public interface LcdGpioAdapter {
    /**
     * Perform any necessary initialization. (e.g. Setting pinmodes of digital output pins)
     */
    void initialize();

    /**
     * Sends a 4-bit value to the interface.
     *
     * @param value
     *         The 4-bit value to be transmitted over the LCD Interface
     *
     * @throws IOException
     *         Thrown when a problem occurs during transmission
     */
    void write4Bits(byte value) throws IOException;

    /**
     * Sends an 8-bit value to the interface.
     *
     * @param value
     *         The 8-bit value to be transmitted over the LCD Interface
     *
     * @throws IOException
     *         Thrown when a problem occurs during transmission
     */
    void write8Bits(byte value) throws IOException;

    /**
     * Sets the LCD's Register Select Pin state
     *
     * @param state
     *         The state of the Register Select Pin. Set to either  {@link LcdRegisterSelectState#COMMAND} or {@link
     *         LcdRegisterSelectState#DATA}
     */
    void setRegSelectState(LcdRegisterSelectState state);

    /**
     * Sets the LCD's Read/Write Pin state. Setting the state to WRITE will set the LCD RW pin to LOW while READ will
     * set the LCD RW pin to HIGH.
     *
     * @param state
     *         The state to set (READ/WRITE) for the LCD RW Pin
     */
    void setReadWriteState(LcdReadWriteState state);

    /**
     * Sets the LCD's Enable Pin state
     *
     * @param state
     *         The {@link PinState} to set for the LCD Enable pin.
     */
    void setEnableState(PinState state);
}
