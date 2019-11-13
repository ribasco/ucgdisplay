package com.ibasco.ucgdisplay.integration;

import com.ibasco.ucgdisplay.common.exceptions.NativeLibraryException;
import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdControllerType;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdFont;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdSize;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdConfigException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Graphics display driver integration test
 *
 * @author Rafael Ibasco
 */
class GlcdDriverIT {
    private GlcdDriver driver;

    private Executable createVirtualDriverExecutable(GlcdConfig config) {
        return () -> driver = new GlcdDriver(config, true);
    }

    @Test
    @DisplayName("Test basic virtual driver construction with no args")
    void testBasicVirtualDriverNoArgs() {
        GlcdConfig config = GlcdConfigBuilder.create(null, null).build();
        assertThrows(GlcdConfigException.class, createVirtualDriverExecutable(config));
    }

    @Test
    @DisplayName("Test basic construction of virtual glcd driver")
    void testBasicVirtualDriverConstruction() {
        GlcdConfig config = GlcdConfigBuilder
                .create(Glcd.ST7920.D_128x64, GlcdBusInterface.SPI_HW_4WIRE_ST7920)
                .build();

        assertDoesNotThrow(createVirtualDriverExecutable(config));
        assertNotNull(driver);
        assertTrue(driver.getId() > 0);
        assertNotNull(driver.getConfig());
        GlcdDisplay display = driver.getConfig().getDisplay();

        assertNotNull(display);
        assertEquals(GlcdSize.SIZE_128x64, display.getDisplaySize());
        assertEquals(GlcdControllerType.ST7920, display.getController());
        assertTrue(display.getSetupDetails().length > 0);
        assertEquals("u8g2_Setup_st7920_s_128x64_f", config.getSetupProcedure());
    }

    @Test
    @DisplayName("Test driver construction with a non-supported bus interface")
    void testNonSupportedProtocol() {
        GlcdConfig config = GlcdConfigBuilder.create(Glcd.ST7920.D_128x64, GlcdBusInterface.I2C_HW)
                .build();

        assertThrows(GlcdConfigException.class, createVirtualDriverExecutable(config));
    }

    @Test
    @DisplayName("Test construction of virtual driver with invalid configuration")
    void testInvalidConfigVirtualDriver() {
        GlcdConfig config = GlcdConfigBuilder
                .create(Glcd.ST7920.D_128x64, GlcdBusInterface.SPI_SW_4WIRE)
                .build();
        assertThrows(GlcdConfigException.class, createVirtualDriverExecutable(config));
    }

    @DisplayName("Test basic draw string operation")
    @Test
    void testDrawString() {
        GlcdConfig config = GlcdConfigBuilder
                .create(Glcd.ST7920.D_128x64, GlcdBusInterface.SPI_HW_4WIRE_ST7920)
                .build();

        CountDownLatch latch = new CountDownLatch(1);

        AtomicBoolean called = new AtomicBoolean();
        GlcdDriverEventHandler byteEventHandler = event -> {
            latch.countDown();
            called.set(true);
        };

        driver = new GlcdDriver(config, true, byteEventHandler, null);
        driver.setFont(GlcdFont.FONT_6X12_MR);
        driver.drawString("Hello");
        driver.sendBuffer();

        assertDoesNotThrow(() -> {
            latch.await(10, TimeUnit.SECONDS);
        });

        assertTrue(called.get());
    }

    @DisplayName("Test basic draw string operation. No font specified")
    @Test
    void testDrawString_NoFont() {
        GlcdConfig config = GlcdConfigBuilder
                .create(Glcd.ST7920.D_128x64, GlcdBusInterface.SPI_HW_4WIRE_ST7920)
                .build();

        driver = new GlcdDriver(config, true);
        assertThrows(NativeLibraryException.class, () -> driver.drawString("Hello"));
        driver.sendBuffer();
    }
}
