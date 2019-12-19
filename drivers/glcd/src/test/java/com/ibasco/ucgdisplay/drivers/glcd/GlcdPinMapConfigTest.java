/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
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

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdPinMappingException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GlcdPinMapConfigTest {

    private GlcdPinMapConfig mapConfig;

    @BeforeEach
    void setup() {
        mapConfig = new GlcdPinMapConfig();
    }

    @Test
    @DisplayName("Test map to byte array conversion")
    void testByteArrayOutput() {
        mapConfig.map(GlcdPin.D0, 0);
        mapConfig.map(GlcdPin.D1, 1);
        mapConfig.map(GlcdPin.D2, 2);
        mapConfig.map(GlcdPin.D3, 3);
        mapConfig.map(GlcdPin.D4, 4);
        mapConfig.map(GlcdPin.D5, 5);
        mapConfig.map(GlcdPin.D6, 6);
        mapConfig.map(GlcdPin.D7, 7);
        int[] pinData = mapConfig.toIntArray();
        assertEquals(0, pinData[0]);
        assertEquals(1, pinData[1]);
        assertEquals(2, pinData[2]);
        assertEquals(3, pinData[3]);
        assertEquals(4, pinData[4]);
        assertEquals(5, pinData[5]);
        assertEquals(6, pinData[6]);
        assertEquals(7, pinData[7]);
    }

    @Test
    @DisplayName("Test pin value greater than 255")
    void testInvalidPinNumber() {
        assertThrows(GlcdPinMappingException.class, () -> mapConfig.map(GlcdPin.D0, 300));
    }

    @Test
    @DisplayName("Test pin existence on the map")
    void testMapExistence() {
        mapConfig.map(GlcdPin.D0, 0);
        mapConfig.map(GlcdPin.D2, 2);
        assertTrue(mapConfig.isMapped(GlcdPin.D0));
        assertTrue(mapConfig.isMapped(GlcdPin.D2));
        assertFalse(mapConfig.isMapped(GlcdPin.D1));
    }

    @Test
    @DisplayName("Test empty map")
    void testEmptyMap() {
        assertTrue(mapConfig.isEmpty());
        mapConfig.map(GlcdPin.D0, 0);
        assertFalse(mapConfig.isEmpty());
    }

    @Test
    void toIntArray() {
        mapConfig.map(GlcdPin.D0, 0);
        mapConfig.map(GlcdPin.D1, 1);
        mapConfig.map(GlcdPin.D2, 2);
        mapConfig.map(GlcdPin.D3, 3);
        mapConfig.map(GlcdPin.D4, 4);
        mapConfig.map(GlcdPin.D5, 5);
        mapConfig.map(GlcdPin.D6, 6);
        mapConfig.map(GlcdPin.D7, 7);
        int[] pinData = mapConfig.toIntArray();
        assertEquals(0, pinData[0]);
        assertEquals(1, pinData[1]);
        assertEquals(2, pinData[2]);
        assertEquals(3, pinData[3]);
        assertEquals(4, pinData[4]);
        assertEquals(5, pinData[5]);
        assertEquals(6, pinData[6]);
        assertEquals(7, pinData[7]);
        for (int i = 8; i < 15; i++)
            assertEquals(-1, pinData[i]);
    }
}
