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

import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.*;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMData;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;

/**
 * ST7920 Example
 */
public class GlcdST7920Example {

    private static final Logger log = LoggerFactory.getLogger(GlcdST7920Example.class);

    public static void main(String[] args) throws Exception {
        new GlcdST7920Example().run();
    }

    private void drawU8G2Logo(int offset, GlcdDriver driver) {
        driver.setFontMode(1);

        driver.setFontDirection(0);
        driver.setFont(GlcdFont.FONT_INB16_MF); //u8g2_font_inb16_mf
        driver.drawString(offset, 22, "U");

        driver.setFontDirection(1);
        driver.setFont(GlcdFont.FONT_INB19_MN); //u8g2_font_inb19_mn
        driver.drawString(offset + 14, 8, "8");

        driver.setFontDirection(0);
        driver.setFont(GlcdFont.FONT_INB16_MF); //u8g2_font_inb16_mf
        driver.drawString(offset + 36, 22, "g");
        driver.drawString(offset + 48, 22, "2");

        driver.drawHLine(offset + 2, 25, 34);
        driver.drawHLine(offset + 3, 26, 34);
        driver.drawVLine(offset + 32, 22, 12);
        driver.drawVLine(offset + 33, 23, 12);
    }

    private void run() throws Exception {
        //SPI HW 4-Wire config for ST7920

        //NOTE: On Raspberry Pi systems, pins can be automatically configured for hardware capability.
        //For automatic configuration to work, pigpio needs to be installed on the system and set as the default provider.

        //Pinout for Main SPI Peripheral (Raspberry Pi / J8 Header / BCM Pin configuration)
        // - MOSI = 10
        // - SCLK = 11
        // - CE1 = 7
        GlcdConfig config = GlcdConfigBuilder
                //Use ST7920 - 128 x 64 display, SPI 4-wire Hardware
                .create(Glcd.ST7920.D_128x64, GlcdBusInterface.SPI_HW_4WIRE_ST7920)
                //Set to 180 rotation
                .option(GlcdOption.ROTATION, GlcdRotation.ROTATION_180)
                //Using system/c-periphery provider
                .option(GlcdOption.PROVIDER, Provider.SYSTEM)
                //Set to 1,250,000 Hz (1.25 MHz)
                .option(GlcdOption.BUS_SPEED, 1250000)
                //The SPI Bus (RPI as two SPI buses available, the Main and Auxillary)
                .option(GlcdOption.SPI_BUS, SpiBus.MAIN)
                //Use CE1 or Chip Select 1 on Main SPI peripheral/bus
                .option(GlcdOption.SPI_CHANNEL, SpiChannel.CHANNEL_1)
                .build();

        GlcdDriver driver = new GlcdDriver(config);

        //Set the Font (This is required for drawing strings)
        driver.setFont(GlcdFont.FONT_6X12_MR);

        //Get the maximum character height
        int maxHeight = driver.getMaxCharHeight();

        long startMillis = System.currentTimeMillis();

        log.debug("Starting display loop");

        XBMData xbmData = XBMUtils.decodeXbmFile(getClass().getResourceAsStream("/ironman.xbm"));

        int offset = 50;

        for (int i = 1000; i >= 0; i--) {
            //Clear the GLCD buffer
            driver.clearBuffer();

            if (offset >= 128) {
                offset = 0;
            }

            drawU8G2Logo(offset++, driver);

            driver.drawXBM(0, 0, 45, 64, Objects.requireNonNull(xbmData).getData());

            //Write Operations to the GLCD buffer
            driver.setFont(GlcdFont.FONT_6X12_MR);
            driver.drawString(55, maxHeight * 3, "ucgdisplay");
            driver.drawString(55, maxHeight * 4, "1.5.0-alpha");
            driver.drawString(100, maxHeight * 5, String.valueOf(i));

            //Send all buffered data to the display
            driver.sendBuffer();

            //Thread.sleep(1);
        }

        //Clear the display
        driver.clearDisplay();
        long endTime = System.currentTimeMillis() - startMillis;

        log.info("Done in {} seconds", Duration.ofMillis(endTime).toSeconds());
    }
}
