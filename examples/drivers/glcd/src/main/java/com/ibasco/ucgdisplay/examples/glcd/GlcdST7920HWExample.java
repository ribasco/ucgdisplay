package com.ibasco.ucgdisplay.examples.glcd;/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver examples
 * Filename: GlcdST7920HWExample.java
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
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.XBMDecodeException;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMData;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ST7920 Example - Hardware SPI
 */
@SuppressWarnings("DuplicatedCode")
public class GlcdST7920HWExample {

    private static final Logger log = LoggerFactory.getLogger(GlcdST7920HWExample.class);

    private int bannerOffset = 0;

    private static class XbmEntry {
        private int width = 0;
        private int height = 0;
        private XBMData data;

        XbmEntry(int width, int height, XBMData data) {
            this.width = width;
            this.height = height;
            this.data = data;
        }
    }

    @FunctionalInterface
    interface Banner {
        void drawBanner(int offset, GlcdDriver driver);
    }

    private List<XbmEntry> xbmImages = Arrays.asList(
            createXbmEntry(45, 64, "ironman.xbm"),
            createXbmEntry(50, 64, "raspberrypi.xbm"),
            createXbmEntry(48, 64, "punisher.xbm"),
            createXbmEntry(50, 64, "handok.xbm"),
            createXbmEntry(64, 64, "code.xbm"),
            createXbmEntry(64, 64, "iot.xbm"),
            createXbmEntry(64, 64, "java.xbm"),
            createXbmEntry(38, 64, "serj.xbm"),
            createXbmEntry(77, 64, "serj2.xbm")
    );

    private List<Banner> banners = Arrays.asList(this::drawProjectBanner, this::drawU8G2Logo);

    private AtomicBoolean shutdown = new AtomicBoolean();

    private GlcdST7920HWExample() throws Exception {
    }

    public static void main(String[] args) throws Exception {
        new GlcdST7920HWExample().run();
    }

    private void drawProjectBanner(int offset, GlcdDriver driver) {
        driver.setFontMode(GlcdFontMode.TRANSPARENT);
        driver.setFontDirection(GlcdFontDirection.LEFT_TO_RIGHT);
        driver.setFont(GlcdFont.FONT_HELVB24_TF); //u8g2_font_inb16_mf
        driver.drawString(offset, 22, "ucgdisplay");
    }

    private void drawU8G2Logo(int offset, GlcdDriver driver) {
        driver.setFontMode(GlcdFontMode.TRANSPARENT);

        driver.setFontDirection(GlcdFontDirection.LEFT_TO_RIGHT);
        driver.setFont(GlcdFont.FONT_INB16_MF); //u8g2_font_inb16_mf
        driver.drawString(offset, 22, "U");

        driver.setFontDirection(GlcdFontDirection.TOP_TO_DOWN);
        driver.setFont(GlcdFont.FONT_INB19_MN); //u8g2_font_inb19_mn
        driver.drawString(offset + 14, 8, "8");

        driver.setFontDirection(GlcdFontDirection.LEFT_TO_RIGHT);
        driver.setFont(GlcdFont.FONT_INB16_MF); //u8g2_font_inb16_mf
        driver.drawString(offset + 36, 22, "g");
        driver.drawString(offset + 48, 22, "2");

        driver.drawHLine(offset + 2, 25, 34);
        driver.drawHLine(offset + 3, 26, 34);
        driver.drawVLine(offset + 32, 22, 12);
        driver.drawVLine(offset + 33, 23, 12);
    }

    private void drawStaticImage(GlcdDriver driver, int index, int x, int y) {
        XbmEntry entry = xbmImages.get(index);
        //Make background transparent
        driver.setDrawColor(GlcdDrawColor.XOR);
        driver.setBitmapMode(GlcdBitmapMode.TRANSPARENT);
        driver.drawXBM(x, y, entry.width, entry.height, entry.data.getData());
    }

    private void drawScrollingBanner(GlcdDriver driver, int index) {
        Banner banner = banners.get(index);
        banner.drawBanner(bannerOffset, driver);
    }

    @SuppressWarnings("SameParameterValue")
    private static XbmEntry createXbmEntry(int width, int height, String fileName) throws XBMDecodeException {
        return new XbmEntry(width, height, XBMUtils.decodeXbmFile(GlcdST7920HWExample.class.getResourceAsStream("/" + fileName)));
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
                //Set to 1,000,000 Hz/bps (1.00 MHz)
                .option(GlcdOption.BUS_SPEED, 1000000)
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

        int imageIndex = 0;
        int bannerIndex = 0;
        long previousMillisImage = 0;
        long previousMillisBanner = 0;
        long previousMillisBannerOffset = 0;
        long previousMillisFps = 0;
        long imageInterval = 3000;
        long bannerChangeInterval = 30000;
        long bannerScrollInterval = 100;
        long fpsInterval = 1000;
        int fpsCtr = 0;
        int lastFpsValue = 0;

        while (!shutdown.get()) {
            long currentMillis = System.currentTimeMillis();

            //Clear the GLCD buffer
            driver.clearBuffer();

            //Change display image every 3 seconds
            if (currentMillis - previousMillisImage >= imageInterval) {
                previousMillisImage = currentMillis;
                imageIndex++;
                if (imageIndex > (xbmImages.size() - 1)) {
                    imageIndex = 0;
                }
            }

            //Change banner every 5 seconds
            if (currentMillis - previousMillisBanner >= bannerChangeInterval) {
                previousMillisBanner = currentMillis;
                bannerIndex++;
                if (bannerIndex > (banners.size() - 1)) {
                    bannerIndex = 0;
                }
            }

            //Scroll banner every 300 milliseconds
            if (currentMillis - previousMillisBannerOffset >= bannerScrollInterval) {
                previousMillisBannerOffset = currentMillis;
                bannerOffset++;
                if (bannerOffset >= 128)
                    bannerOffset = 0;
            }

            if (currentMillis - previousMillisFps >= fpsInterval) {
                previousMillisFps = currentMillis;
                lastFpsValue = fpsCtr;
                fpsCtr = 0;
            } else {
                fpsCtr++;
            }

            drawScrollingBanner(driver, bannerIndex);
            drawStaticImage(driver, imageIndex, 0, 0);

            //Write Operations to the GLCD buffer
            driver.setFont(GlcdFont.FONT_6X12_MR);
            driver.drawString(65, maxHeight * 4, "ucgdisplay");
            driver.drawString(80, maxHeight * 5, "FPS: " + lastFpsValue);
            //Send all buffered data to the display
            driver.sendBuffer();
        }

        //Clear the display
        driver.clearDisplay();
        long endTime = System.currentTimeMillis() - startMillis;

        log.info("Done in {} seconds", Duration.ofMillis(endTime).toSeconds());
    }
}
