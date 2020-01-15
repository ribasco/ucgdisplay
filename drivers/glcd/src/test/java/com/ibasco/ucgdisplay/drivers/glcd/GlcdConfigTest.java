package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlcdConfigTest {

    @Test
    void testEquals() {
        GlcdConfig configOne = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_128x64);
        configOne.setBusInterface(GlcdBusInterface.SPI_SW_4WIRE_ST7920);

        GlcdConfig configTwo = new GlcdConfig();
        configTwo.setDisplay(Glcd.ST7920.D_128x64);
        configTwo.setBusInterface(GlcdBusInterface.SPI_SW_4WIRE_ST7920);

        assertEquals(configOne, configTwo);
    }

    @Test
    void testEqualsDiffBus() {
        GlcdConfig configOne = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_128x64);
        configOne.setBusInterface(GlcdBusInterface.SPI_SW_4WIRE_ST7920);

        GlcdConfig configTwo = new GlcdConfig();
        configTwo.setDisplay(Glcd.ST7920.D_128x64);
        configTwo.setBusInterface(GlcdBusInterface.PARALLEL_6800);

        assertNotEquals(configOne, configTwo);
    }

    @Test
    void testEqualsDiffDisplay() {
        GlcdConfig configOne = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_128x64);
        configOne.setBusInterface(GlcdBusInterface.SPI_SW_4WIRE_ST7920);

        GlcdConfig configTwo = new GlcdConfig();
        configTwo.setDisplay(Glcd.SSD1306.D_64x48_64X48ER);
        configTwo.setBusInterface(GlcdBusInterface.SPI_HW_4WIRE);

        assertNotEquals(configOne, configTwo);
    }

    @Test
    void testEqualsDiffSize() {
        GlcdConfig configOne = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_128x64);
        configOne.setBusInterface(GlcdBusInterface.SPI_SW_4WIRE_ST7920);

        GlcdConfig configTwo = new GlcdConfig();
        configOne.setDisplay(Glcd.ST7920.D_192x32);
        configTwo.setBusInterface(GlcdBusInterface.SPI_HW_4WIRE_ST7920);

        assertNotEquals(configOne, configTwo);
    }
}