/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: PigpioMode.java
 *
 * ---------------------------------------------------------
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

import com.ibasco.ucgdisplay.drivers.glcd.GlcdOptionValue;

/**
 * Enumeration for available Pigpio modes
 *
 * @author Rafael Ibasco
 */
public enum PigpioMode implements GlcdOptionValue<Integer> {
    /**
     * Allows pigpio to communicate directly to the I/O peripheral devices.
     * No daemon service on the background is required.
     * <blockquote>
     * IMPORTANT: When choosing this mode, remember to run your program as ROOT
     * and make sure that the pigpio daemon is not running, otherwise the native library would throw an exception.
     * </blockquote>
     */
    STANDALONE(0),

    /**
     * Allows pigpio to communicate to the daemon via socket interface.
     * This is slower than standalone version but many programs can run at any
     * point in time when talking to the pigpio daemon.
     */
    DAEMON(1);

    private int mode;

    PigpioMode(int mode) {
        this.mode = mode;
    }

    @Override
    public Integer toValue() {
        return mode;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
