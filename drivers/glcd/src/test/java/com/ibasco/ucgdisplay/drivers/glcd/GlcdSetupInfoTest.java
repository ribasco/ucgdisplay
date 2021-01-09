/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
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
package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.core.u8g2.U8g2Graphics;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdCommProtocol;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GlcdSetupInfoTest {
    @Test
    @DisplayName("Test isSupported() function")
    void testSupportedBusInterfaces() {
        GlcdSetupInfo setupInfo = new GlcdSetupInfo("test_setup_f", U8g2Graphics.COM_8080 | U8g2Graphics.COM_3WSPI | U8g2Graphics.COM_I2C);

        assertEquals("test_setup_f", setupInfo.getFunction());
        assertEquals(0x1A, setupInfo.getProtocols());

        assertTrue(setupInfo.isSupported(GlcdCommProtocol.PARALLEL_8080));
        assertTrue(setupInfo.isSupported(GlcdCommProtocol.SPI_SW_3WIRE));
        assertTrue(setupInfo.isSupported(GlcdCommProtocol.I2C_HW));
        assertTrue(setupInfo.isSupported(GlcdCommProtocol.I2C_SW));

        assertFalse(setupInfo.isSupported(GlcdCommProtocol.SERIAL_HW));
        assertFalse(setupInfo.isSupported(GlcdCommProtocol.PARALLEL_6800));
        assertFalse(setupInfo.isSupported(GlcdCommProtocol.PARALLEL_6800_KS0108));
        assertFalse(setupInfo.isSupported(GlcdCommProtocol.SPI_SW_4WIRE));
        assertFalse(setupInfo.isSupported(GlcdCommProtocol.SPI_HW_4WIRE_ST7920));
    }
}
