package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LcdCharGraphicsTest {

    public static final Logger log = LoggerFactory.getLogger(LcdCharGraphicsTest.class);

    LcdDriver driver = mock(LcdDriver.class);

    @BeforeEach
    void setUp() {
        driver = mock(LcdDriver.class);
        when(driver.getColumnCount()).thenReturn(20);
        when(driver.getRowCount()).thenReturn(4);
    }

    @Test
    @DisplayName("Test flush display")
    public void testWriteFlush() {
        LcdCharGraphics graphics = new LcdCharGraphics(driver);

        log.debug("Capacity: {}", graphics.getBuffer().capacity());

        assertEquals(80, graphics.getBuffer().remaining());

        graphics.drawText("12345");

        assertEquals(75, graphics.getBuffer().remaining());
        assertEquals(5, graphics.getBuffer().position());
        assertEquals(20, graphics.getWidth());
        assertEquals(4, graphics.getHeight());

        graphics.drawText("this is just a test of a really long mother fucking text asdadasdas asd asd adas das");

        displayBuffer(graphics);

        graphics.flush();

        verify(driver).setCursorPosition(0, 0);

        assertEquals(0, graphics.getBuffer().position());
    }

    @Test
    @DisplayName("Test cursor/write operation")
    public void testCursorPosition() {
        LcdCharGraphics graphics = new LcdCharGraphics(driver);
        graphics.setCursor(0, 0);
        graphics.drawText("Rafael");
        graphics.setCursor(0, 1);
        graphics.drawText("Luis");
        graphics.setCursor(0, 2);
        graphics.drawText("Lee");
        graphics.setCursor(0, 3);
        graphics.drawText("Ibasco");
        displayBuffer(graphics);
    }

    @Test
    @DisplayName("Test write operation on read-only buffer")
    public void writeToReadOnlyBuffer() {
        LcdCharGraphics graphics = new LcdCharGraphics(driver);
        assertThrows(ReadOnlyBufferException.class, () -> graphics.getBuffer().putChar('c'));
    }

    private void displayBuffer(LcdCharGraphics graphics) {
        ByteBuffer bb = (ByteBuffer) graphics.getBuffer().rewind();
        while (bb.hasRemaining()) {
            int size = (bb.remaining() >= graphics.getWidth()) ? graphics.getWidth() : bb.remaining();
            byte[] rowData = new byte[size];
            bb.get(rowData);
            log.debug("Row: {}", new String(rowData));
        }
    }
}