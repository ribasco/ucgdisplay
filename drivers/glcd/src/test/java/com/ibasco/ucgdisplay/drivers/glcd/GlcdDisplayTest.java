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

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBufferLayout;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdController;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

class GlcdDisplayTest {

    @Test
    void testEquals() {
        assertEquals(Glcd.ST7920.D_128x64, new GlcdDisplay(GlcdController.ST7920, "D_128x64", 16, 8, GlcdBufferLayout.HORIZONTAL));
        assertNotEquals(Glcd.ST7920.D_192x32, new GlcdDisplay(GlcdController.ST7920, "D_128x64", 16, 8, GlcdBufferLayout.HORIZONTAL));
    }
}
