/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: GlcdOption.java
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
package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.enums.*;

/**
 * Options for the native layer
 *
 * @param <T>
 *         The type of the underlying option
 *
 * @author Rafael Ibasco
 */
public class GlcdOption<T> {

    /**
     * The default provider to be used for all I/O peripherals
     */
    public static final GlcdOption<Provider> PROVIDER = createOption("default_provider");

    /**
     * The transfer rate to be applied for the I/O peripheral (e.g. SPI interface).
     * <p>
     * ex: Use 100000 for 100,000 bits per second
     */
    public static final GlcdOption<Integer> DEVICE_SPEED = createOption("device_speed");

    /**
     * The GPIO Chip device
     */
    public static final GlcdOption<GpioChip> GPIO_CHIP = createOption("gpio_chip");

    /**
     * The SPI channel (also known as Chip-Select)
     * <p>
     * Note: This is only required if the provider is pigpio and Hardware SPI is selected.
     *
     * <p><strong>Raspberry Pi SPI Channels</strong></p>
     *
     * <pre>
     *     _____________________________________
     *    |             CE0    CE1    CE2      |
     *    | -----------------------------------|
     *    | Main SPI    8      7       -       |
     *    | Aux SPI     18     17      16      |
     *    `````````````````````````````````````
     * </pre>
     *
     * @see <a href="http://abyz.me.uk/rpi/pigpio/pdif2.html#spi_channel">SPI Channel</a>
     */
    public static final GlcdOption<SpiChannel> SPI_CHANNEL = createOption("spi_channel");

    /**
     * The SPI Peripheral device. For raspberry pi systems, there are two SPI peripherals available, the main and
     * the auxillary.
     */
    public static final GlcdOption<SpiBus> SPI_BUS = createOption("spi_bus_number");

    /**
     * The SPI Mode. Values can be 0, 1, 2, or 3. (Default: 0)
     *
     * <blockquote>
     * WARNING: On pigpio, modes 1 and 3 do not appear to work on the auxiliary SPI.
     * </blockquote>
     *
     * @see SpiMode
     */
    public static final GlcdOption<SpiMode> SPI_MODE = createOption("spi_mode");

    /**
     * SPI Bit Order. Can be either 0 = MSB First or 1 = LSB First. (Default: 0)
     */
    public static final GlcdOption<SpiBitOrder> SPI_BIT_ORDER = createOption("spi_bit_order");

    /**
     * SPI Bits per word. (Default: 8)
     */
    public static final GlcdOption<Integer> SPI_BITS_PER_WORD = createOption("spi_bits_per_word");

    /**
     * The I2C bus number to be used by the native layer for communication.
     * <p>
     * Note: This is only required if you choose pigpio as your I2C provider and the I/O implementation is Hardware.
     */
    public static final GlcdOption<I2CBus> I2C_BUS = createOption("i2c_bus_number");

    /**
     * The i2c device address.
     */
    public static final GlcdOption<Integer> I2C_DEVICE_ADDRESS = createOption("i2c_address");

    /**
     * Custom SPI flags. Only for advanced users.
     *
     * @see <a href="http://abyz.me.uk/rpi/pigpio/pdif2.html#spi_open">SPI Flags</a>
     */
    public static final GlcdOption<Integer> SPI_FLAGS = createOption("spi_flags");

    /**
     * Custom I2C flags. Only for advanced users.
     */
    public static final GlcdOption<Integer> I2C_FLAGS = createOption("i2c_flags");

    /**
     * The display rotation. Default is None.
     */
    public static final GlcdOption<GlcdRotation> ROTATION = createOption("rotation");

    /**
     * The IP address of the pigpiod daemon. If null/empty, the default value would be used (Default: localhost).
     * <p>
     * Note: The default value can also be overriden by an environment variable on the target's OS (PIGPIO_ADDR)
     */
    public static final GlcdOption<String> PIGPIO_ADDRESS = createOption("pigpio_addr");

    /**
     * The port number of the pigpiod daemon. If null/empty, the default value would be used (Default: 8888).
     * <p>
     * Note: The default value can also be overriden by an environment variable on the target's OS (PIGPIO_PORT)
     */
    public static final GlcdOption<Integer> PIGPIO_PORT = createOption("pigpio_port");

    private String name;

    /**
     * Private constructor. This class is only meant to be a placeholder for the Glcd config options.
     *
     * @param name
     *         The name/key of the configuration option.
     */
    private GlcdOption(String name) {
        this.name = name;
    }

    /**
     * Simple factory method for creating new option instances
     *
     * @param name
     *         The name of the config option
     * @param <T>
     *         The captured type of the {@link GlcdOption}
     *
     * @return A new instance of the {@link GlcdOption} class
     */
    private static <T> GlcdOption<T> createOption(String name) {
        return new GlcdOption<>(name);
    }

    /**
     * @return The raw key/name of this option
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
