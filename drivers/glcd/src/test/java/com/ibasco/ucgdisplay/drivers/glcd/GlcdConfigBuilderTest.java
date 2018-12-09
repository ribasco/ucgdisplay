/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: GlcdConfigBuilderTest.java
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
package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdPin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlcdConfigBuilderTest {

    private GlcdConfigBuilder configBuilder;

    @BeforeEach
    void setUp() {
        configBuilder = GlcdConfigBuilder.create();
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
}
