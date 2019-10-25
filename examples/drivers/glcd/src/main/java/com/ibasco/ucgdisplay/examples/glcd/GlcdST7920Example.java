package com.ibasco.ucgdisplay.examples.glcd;/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver examples
 * Filename: GlcdST7920Example.java
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

import com.ibasco.ucgdisplay.drivers.glcd.Glcd;
import com.ibasco.ucgdisplay.drivers.glcd.GlcdConfig;
import com.ibasco.ucgdisplay.drivers.glcd.GlcdConfigBuilder;
import com.ibasco.ucgdisplay.drivers.glcd.GlcdDriver;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdFont;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdRotation;

public class GlcdST7920Example {

    public static void main(String[] args) throws Exception {
        //SPI HW 4-Wire config for ST7920 (No pin mapping required)
        GlcdConfig config = GlcdConfigBuilder.create()
                .rotation(GlcdRotation.ROTATION_NONE)
                .busInterface(GlcdBusInterface.SPI_HW_4WIRE_ST7920)
                .transportDevice("/dev/spidev0.0")
                .gpioDevice("/dev/gpiochip0")
                .display(Glcd.ST7920.D_128x64)
                .build();

        GlcdDriver driver = new GlcdDriver(config);

        driver.setFont(GlcdFont.FONT_6X12_MR);
        int maxHeight = driver.getMaxCharHeight();

        for (int i = 60; i >= 0; i--) {
            driver.clearBuffer();
            driver.drawString(0, maxHeight, "ucgdisplay");
            driver.drawString(0, maxHeight * 2, "powered by");
            driver.drawString(0, maxHeight * 3, "u8g2");
            driver.drawString(0, maxHeight * 5, "Shutting down in " + i + "s");
            driver.sendBuffer();
            Thread.sleep(1000);
        }

        System.out.println("Done");
    }
}
