package com.ibasco.ucgdisplay.integration;

import com.ibasco.ucgdisplay.common.exceptions.NativeLibraryException;
import com.ibasco.ucgdisplay.drivers.glcd.*;
import com.ibasco.ucgdisplay.drivers.glcd.enums.*;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdConfigException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Graphics display driver integration test
 *
 * @author Rafael Ibasco
 */
class GlcdDriverIT {

    private static final Logger log = LoggerFactory.getLogger(GlcdDriverIT.class);

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

    @DisplayName("Test U8G2 native buffer (Horizontal Layout)")
    @Test
    void testU8g2NativeBufferHorizontal() {
        driver = createVirtualDriver();
        assertEquals(GlcdBufferLayout.HORIZONTAL, driver.getConfig().getDisplay().getBufferType());

        driver.clearBuffer();
        driver.drawPixel(0, 0); //draw first pixel
        driver.drawPixel(127, 63); //draw last pixel

        ByteBuffer u8g2Buff = driver.getNativeBuffer();

        assertNotNull(u8g2Buff);
        assertFalse(isBufferEmpty(u8g2Buff));

        driver.sendBuffer();

        assertFalse(isBufferEmpty(u8g2Buff));

        u8g2Buff.clear();
        int x = 0, y = 0;
        while (u8g2Buff.hasRemaining()) {
            byte data = u8g2Buff.get();
            for (int pos = 7; pos >= 0; pos--) {
                if (x >= 128) {
                    x = 0;
                    y++;
                }
                int bit;
                if ((data & (1 << pos)) != 0) {
                    bit = 1;
                } else {
                    bit = 0;
                }
                log.debug("({}, {}) = {}", x, y, bit);
                if (x == 0 && y == 0) {
                    assertEquals(1, bit);
                } else if (x == 127 && y == 63) {
                    assertEquals(1, bit);
                } else {
                    assertEquals(0, bit);
                }
                x++;
            }
        }
    }

    @DisplayName("Test BGRA native buffer (Horizontal Layout)")
    @Test
    void testBGRANativeBufferHorizontal() {
        driver = createVirtualDriver();
        driver.clearBuffer();
        driver.drawPixel(0, 0); //draw first pixel
        driver.drawPixel(127, 63); //draw last pixel

        ByteBuffer bgraBuff = driver.getNativeBgraBuffer();

        assertNotNull(bgraBuff);
        assertTrue(isBufferEmpty(bgraBuff));

        driver.sendBuffer();

        assertFalse(isBufferEmpty(bgraBuff));

        bgraBuff.clear();
        int ctr = 0;
        while (bgraBuff.hasRemaining()) {
            int value = bgraBuff.getInt();
            if (ctr == 0) {
                assertEquals(255, value);
            } else if (ctr == 8191) {
                assertEquals(255, value);
            } else {
                assertEquals(0, value);
            }
            ctr++;
        }
    }

    @DisplayName("Test U8G2 native buffer (Vertical Layout)")
    @Test
    void testU8g2NativeBufferVertical() {
        driver = createVirtualDriver(GlcdConfigBuilder.create(Glcd.SSD1306.D_128x64_128X64NONAME, GlcdCommProtocol.SPI_HW_4WIRE).build());
        //driver = createVirtualDriver(GlcdConfigBuilder.create(Glcd.ST7920.D_128x64, GlcdCommProtocol.SPI_HW_4WIRE_ST7920).build());
        int width = driver.getConfig().getDisplaySize().getDisplayWidth();
        int height = driver.getConfig().getDisplaySize().getDisplayHeight();

        driver.clearBuffer();

        driver.drawPixel(0, 0); //draw first pixel
        driver.drawPixel(1, 0); //draw first pixel
        driver.drawPixel(0, 2); //draw first pixel
        driver.drawPixel(0, 4); //draw first pixel
        driver.drawPixel(0, 6); //draw first pixel
        driver.drawPixel(0, 8); //draw first pixel
        driver.drawPixel(0, 16); //draw first pixel
        driver.drawPixel(0, 24); //draw first pixel
        driver.drawPixel(0, 32); //draw first pixel
        driver.drawPixel(0, 40); //draw first pixel
        driver.drawPixel(0, 48); //draw first pixel
        driver.drawPixel(0, 56); //draw first pixel
        driver.drawPixel(127, 63); //draw last pixel

        assertEquals(GlcdBufferLayout.VERTICAL, driver.getConfig().getDisplay().getBufferType());

        ByteBuffer u8g2Buff = driver.getNativeBuffer();

        assertNotNull(u8g2Buff);
        assertFalse(isBufferEmpty(u8g2Buff));

        driver.sendBuffer();
        assertFalse(isBufferEmpty(u8g2Buff));
    }

    private void processVerticalHz(int width, byte[] buffer) {
        int bitpos = 0, x = 0, y = 0, page = 0, pos = 0, mark = 0;
        while (true) {
            if (x > (width - 1)) {
                //are we at the last bit?
                if (bitpos++ >= 7) {
                    if (!(pos < buffer.length))
                        break;
                    page++;
                    bitpos = 0;
                    pos = width * page;
                    mark = pos;
                } else {
                    pos = mark;
                }
                x = 0;
            }
            byte data = buffer[pos++];
            y =  (page * 8) + bitpos;
            int bit = (data & (1 << bitpos)) != 0 ? 1 : 0;
            log.debug("Bit = {}, Idx = {}, Page = {} ({}, {}) = {}", bitpos, pos - 1, page, x, y, bit);
            x++;
        }
    }

    private void processVerticalHz(int width, ByteBuffer buffer) {
        int bitpos = 0, x = 0, y = 0, page = 0;
        buffer.clear();
        buffer.mark();
        while (true) {
            if (x > (width - 1)) {
                //are we at the last bit?
                if (bitpos++ >= 7) {
                    if (!buffer.hasRemaining())
                        break;
                    page++;
                    bitpos = 0;
                    buffer.position(width * page);
                    buffer.mark();
                } else {
                    buffer.reset();
                }
                x = 0;
            }
            byte data = buffer.get();
            y =  (page * 8) + bitpos;
            int bit = (data & (1 << bitpos)) != 0 ? 1 : 0;
            log.debug("Bit = {}, Idx = {}, Page = {} ({}, {}) = {}", bitpos, buffer.position() - 1, page, x, y, bit);
            x++;
        }
    }

    private void processVertical(int width, ByteBuffer u8g2Buff) {
        u8g2Buff.clear();
        int x = 0, y, page = 0;
        while (u8g2Buff.hasRemaining()) {
            log.debug("PAGE = {}", page);
            byte data = u8g2Buff.get();
            if (x >= width) {
                x = 0;
                page++;
            }
            for (int pos = 0; pos <= 7; pos++) {
                y = (page * 8) + pos;
                int bit = (data & (1 << pos)) != 0 ? 1 : 0;
                log.debug("\t\t({}) x={}, y={} = {}", u8g2Buff.position() - 1, x, y, bit);
            }
            x++;
        }
    }

    private boolean isBufferEmpty(ByteBuffer buffer) {
        ((Buffer) buffer).clear();
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
