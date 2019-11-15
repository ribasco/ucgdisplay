/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: Provider.java
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
 * The enumeration for all available I/O providers supported by the library
 *
 * @author Rafael Ibasco
 */
public enum Provider implements GlcdOptionValue<String> {
    /**
     * A GPIO library which makes use of the linux character device (replacement of sysfs). SPI and I2C not supported.
     *
     * @see <a href="https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git/">Libgpiod website</a>
     */
    LIBGPIOD("libgpiod"),
    /**
     * Uses the third-party pigpio library. Standalone mode (no daemon needed).
     * The program is linked against the pigpio library, this means that only ONE program can be running at a time when using this mode.
     * Supports I2C/GPIO and SPI interfaces.
     *
     * @see <a href="https://github.com/joan2937/pigpio/issues/129">Difference between pigpio and pigpiod_if2</a>
     * @see <a href="https://github.com/joan2937/pigpio">Pigpio Website</a>
     */
    PIGPIO_STANDALONE("pigpio"),
    /**
     * Uses the third-party pigpiod_if2 library. Requires the pigpio daemon to be installed and running on the target system. Multiple programs are allowed to run at the same time.
     * Supports I2C/GPIO and SPI interfaces. Communication with the daemon is done via Socket Interface, which is much slower than the {@link #PIGPIO_STANDALONE} mode.
     *
     * @see <a href="https://github.com/joan2937/pigpio/issues/129">Difference between pigpio and pigpiod_if2</a>
     * @see <a href="https://github.com/joan2937/pigpio">Pigpio Website</a>
     */
    PIGPIO_DAEMON("pigpiod"),
    /**
     * <p>Makes use of built-in peripheral I/O interfaces provided by the linux kernel. No external dependencies/packages are required to be installed on the SoC.
     * Fully Supports I2C, GPIO (character device and sysfs) and SPI.</p>
     *
     * @see <a href="https://github.com/vsergeev/c-periphery">C-periphery website</a>
     */
    SYSTEM("cperiphery");

    private String value;

    Provider(String value) {
        this.value = value;
    }

    @Override
    public String toValue() {
        return value;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
