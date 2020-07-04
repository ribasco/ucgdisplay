/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
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

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdCommProtocol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GlcdConfigTest {

    @Test
    void testEquals() {
        GlcdConfig configOne = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_128x64);
        configOne.setBusInterface(GlcdCommProtocol.SPI_SW_4WIRE_ST7920);

        GlcdConfig configTwo = new GlcdConfig();
        configTwo.setDisplay(Glcd.ST7920.D_128x64);
        configTwo.setBusInterface(GlcdCommProtocol.SPI_SW_4WIRE_ST7920);

        assertEquals(configOne, configTwo);
    }

    @Test
    void testEqualsDiffBus() {
        GlcdConfig configOne = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_128x64);
        configOne.setBusInterface(GlcdCommProtocol.SPI_SW_4WIRE_ST7920);

        GlcdConfig configTwo = new GlcdConfig();
        configTwo.setDisplay(Glcd.ST7920.D_128x64);
        configTwo.setBusInterface(GlcdCommProtocol.PARALLEL_6800);

        assertNotEquals(configOne, configTwo);
    }

    @Test
    void testEqualsDiffDisplay() {
        GlcdConfig configOne = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_128x64);
        configOne.setBusInterface(GlcdCommProtocol.SPI_SW_4WIRE_ST7920);

        GlcdConfig configTwo = new GlcdConfig();
        configTwo.setDisplay(Glcd.SSD1306.D_64x48_64X48ER);
        configTwo.setBusInterface(GlcdCommProtocol.SPI_HW_4WIRE);

        assertNotEquals(configOne, configTwo);
    }

    @Test
    void testEqualsDiffSize() {
        GlcdConfig configOne = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_128x64);
        configOne.setBusInterface(GlcdCommProtocol.SPI_SW_4WIRE_ST7920);

        GlcdConfig configTwo = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_192x32);
        configTwo.setBusInterface(GlcdCommProtocol.SPI_HW_4WIRE_ST7920);

        assertNotEquals(configOne, configTwo);
    }
}
