/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: GlcdConfigBuilderTest.java
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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

class GlcdConfigBuilderTest {

    private GlcdConfigBuilder configBuilder;

    @Mock
    private GlcdDisplay display;

    @Mock
    private GlcdBusInterface busInterface;

    @BeforeEach
    void setUp() {
        configBuilder = GlcdConfigBuilder.create(display, busInterface);
    }

    @Test
    void testCalledMappedPinShortcut() {
        assertNotNull(configBuilder.mapPin(GlcdPin.D0, 10));
        GlcdConfig config = configBuilder.build();
        assertNotNull(config);
        assertNotNull(config.getPinMap());
    }

    @Test
    void testUncalledMappedPinShortcut() {
        GlcdConfig config = configBuilder.build();
        assertNotNull(config);
        assertNull(config.getPinMap());
    }

    @Test
    void testDirectSetMapConfig() {
        GlcdPinMapConfig mapConfig = new GlcdPinMapConfig();
        GlcdConfig config = configBuilder.pinMap(mapConfig).mapPin(GlcdPin.D0, 10).build();
        assertNotNull(config);
        assertSame(mapConfig, config.getPinMap());
    }

    @Test
    void testCopyOptions() {
        Map<GlcdOption<?>, Object> newOptions = new HashMap<>();
        newOptions.put(GlcdOption.ROTATION, GlcdRotation.ROTATION_180);
        newOptions.put(GlcdOption.PROVIDER, Provider.PIGPIO);
        newOptions.put(GlcdOption.PIGPIO_MODE, PigpioMode.STANDALONE);
        newOptions.put(GlcdOption.SPI_FLAGS, 10);

        //Existing entries
        configBuilder
                .option(GlcdOption.SPI_BITS_PER_WORD, 8)
                .option(GlcdOption.SPI_PERIPHERAL, SpiPeripheral.MAIN)
                .option(GlcdOption.SPI_FLAGS, 5);

        configBuilder.options(newOptions);

        GlcdConfig config = configBuilder.build();
        int spiFlags = config.getOption(GlcdOption.SPI_FLAGS);

        assertEquals(6, config.getOptions().size());
        assertEquals(10, spiFlags);
    }
}
