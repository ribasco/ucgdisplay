package com.ibasco.ucgdisplay.integration;

import com.ibasco.ucgdisplay.common.exceptions.NativeLibraryException;
import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdCommProtocol;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdController;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdFont;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdSize;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdConfigException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.nio.ByteBuffer;
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
                .create(Glcd.ST7920.D_128x64, GlcdCommProtocol.SPI_HW_4WIRE_ST7920)
                .build();

        assertDoesNotThrow(createVirtualDriverExecutable(config));
        assertNotNull(driver);
        assertTrue(driver.getId() > 0);
        assertNotNull(driver.getConfig());
        GlcdDisplay display = driver.getConfig().getDisplay();

        assertNotNull(display);
        assertEquals(GlcdSize.SIZE_128x64, display.getDisplaySize());
        assertEquals(GlcdController.ST7920, display.getController());
        assertTrue(display.getSetupDetails().length > 0);
        assertEquals("u8g2_Setup_st7920_s_128x64_f", config.getSetupProcedure());
    }

    @Test
    @DisplayName("Test driver construction with a non-supported bus interface")
    void testNonSupportedProtocol() {
        GlcdConfig config = GlcdConfigBuilder.create(Glcd.ST7920.D_128x64, GlcdCommProtocol.I2C_HW)
                .build();

        assertThrows(GlcdConfigException.class, createVirtualDriverExecutable(config));
    }

    @Test
    @DisplayName("Test construction of virtual driver with invalid configuration")
    void testInvalidConfigVirtualDriver() {
        GlcdConfig config = GlcdConfigBuilder
                .create(Glcd.ST7920.D_128x64, GlcdCommProtocol.SPI_SW_4WIRE)
                .build();
        assertThrows(GlcdConfigException.class, createVirtualDriverExecutable(config));
    }

    @DisplayName("Test basic draw string operation")
    @Test
    void testDrawString() {
        GlcdConfig config = GlcdConfigBuilder
                .create(Glcd.ST7920.D_128x64, GlcdCommProtocol.SPI_HW_4WIRE_ST7920)
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
                .create(Glcd.ST7920.D_128x64, GlcdCommProtocol.SPI_HW_4WIRE_ST7920)
                .build();

        driver = new GlcdDriver(config, true);
        assertThrows(NativeLibraryException.class, () -> driver.drawString("Hello"));
        driver.sendBuffer();
    }

    @DisplayName("Test XBM export")
    @Test
    void testExportXBM() {
        driver = createVirtualDriver();
        driver.setFont(GlcdFont.FONT_9X15B_TR);
        driver.clearBuffer();
        driver.drawBox(0, 0, 20, 20);
        driver.sendBuffer();
        String output = driver.exportToXBM();
        assertNotNull(output);
        assertTrue(output.contains("xbm_width 128"));
        assertTrue(output.contains("xbm_height 64"));
    }

    @DisplayName("Test XBM2 export")
    @Test
    void testExportXBM2() {
        driver = createVirtualDriver();
        driver.setFont(GlcdFont.FONT_9X15B_TR);
        driver.clearBuffer();
        driver.setFontPosTop();
        driver.drawString(0, 0, "Hello world");
        driver.drawBox(0, 0, 20, 20);
        driver.sendBuffer();
        String output = driver.exportToXBM2();
        assertNotNull(output);
        assertTrue(output.contains("xbm_width 128"));
        assertTrue(output.contains("xbm_height 64"));
    }

    @DisplayName("Test export PBM")
    @Test
    void testExportPBM() {
        driver = createVirtualDriver();
        driver.setFont(GlcdFont.FONT_9X15B_TR);
        driver.clearBuffer();
        driver.setFontPosTop();
        driver.drawString(0, 0, "Hello world");
        driver.drawBox(0, 0, 20, 20);
        driver.sendBuffer();
        String output = driver.exportToPBM();
        assertNotNull(output);
        String[] parts = output.split("\n");
        assertTrue(parts.length > 0);
        assertEquals("P1", parts[0]);
        assertEquals("128", parts[1]);
        assertEquals("64", parts[2]);
    }

    @DisplayName("Test export PBM2")
    @Test
    void testExportPBM2() {
        driver = createVirtualDriver();
        driver.setFont(GlcdFont.FONT_9X15B_TR);
        driver.clearBuffer();
        driver.setFontPosTop();
        driver.drawString(0, 0, "Hello world");
        driver.drawBox(0, 0, 20, 20);
        driver.sendBuffer();
        String output = driver.exportToPBM2();
        assertNotNull(output);
        String[] parts = output.split("\n");
        assertTrue(parts.length > 0);
        assertEquals("P1", parts[0]);
        assertEquals("128", parts[1]);
        assertEquals("64", parts[2]);
    }

    @DisplayName("Test update display")
    @Test
    void testUpdateDisplay() {
        driver = createVirtualDriver();
        driver.clearBuffer();
        driver.drawBox(0, 0, 20, 20);
        driver.updateDisplay();
        byte[] buffer = driver.getBuffer();
        assertNotNull(buffer);
        assertFalse(isBufferEmpty(buffer));
    }

    @DisplayName("Test update display area")
    @Test
    void testUpdateDisplayArea() {
        driver = createVirtualDriver();
        driver.clearBuffer();
        driver.drawBox(0, 0, 20, 20);
        driver.updateDisplay(0, 0, 20, 20);
        byte[] buffer = driver.getBuffer();
        assertNotNull(buffer);
        assertFalse(isBufferEmpty(buffer));
    }

    @DisplayName("Test clear buffer")
    @Test
    void testClearBuffer() {
        driver = createVirtualDriver();
        driver.clearBuffer();
        driver.drawBox(0, 0, 20, 20);
        driver.setBufferCurrTileRow(0);
        driver.sendBuffer();
        assertNotNull(driver.getNativeBuffer());
        assertFalse(isBufferEmpty(driver.getNativeBuffer()));
        driver.clearBuffer();
        driver.sendBuffer();
        assertTrue(isBufferEmpty(driver.getNativeBuffer()));
    }

    @DisplayName("Test send command")
    @Test
    void testSendCommand() {
        driver = createVirtualDriver();
        assertDoesNotThrow(() -> driver.sendCommand("caaaaaac", new byte[] {0x027, 0, 3, 0, 7, 0, 127, 0x2f}));
    }

    private boolean isBufferEmpty(ByteBuffer buffer) {
        buffer.clear();
        while (buffer.hasRemaining()) {
            int b = Byte.toUnsignedInt(buffer.get());
            if (b != 0)
                return false;
        }
        return true;
    }

    private boolean isBufferEmpty(byte[] data) {
        for (byte b : data) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    private GlcdDriver createVirtualDriver() {
        return createVirtualDriver(null);
    }

    private GlcdDriver createVirtualDriver(GlcdConfig config) {
        if (config == null) {
            config = GlcdConfigBuilder
                    .create(Glcd.ST7920.D_128x64, GlcdCommProtocol.SPI_HW_4WIRE_ST7920)
                    .build();
        }

        driver = new GlcdDriver(config, true);
        return driver;
    }
}
