package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.core.exceptions.OffsetOutOfBoundsException;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.commons.util.StringUtils.isBlank;
import static org.mockito.Mockito.*;

class LcdCharGraphicsTest {

    public static final Logger log = LoggerFactory.getLogger(LcdCharGraphicsTest.class);

    LcdDriver driver;

    LcdCharGraphics graphics;

    @BeforeEach
    void setUp() {
        driver = mock(LcdDriver.class);
        when(driver.getColumnCount()).thenReturn(20);
        when(driver.getRowCount()).thenReturn(4);
        graphics = new LcdCharGraphics(driver);
    }

    @Test
    @DisplayName("Test invalid buffer size")
    public void testInvalidSize() {
        when(driver.getColumnCount()).thenReturn(0);
        when(driver.getRowCount()).thenReturn(4);
        assertThrows(IllegalStateException.class, () -> graphics = new LcdCharGraphics(driver));
    }

    @Test
    @DisplayName("Test internal properties")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void testInternalProperties() {
        graphics.getWidth();
        graphics.getHeight();
        verify(driver, times(2)).getRowCount();
        verify(driver, times(2)).getColumnCount();
    }

    @Test
    @DisplayName("Test flush display")
    public void testWriteFlush() {
        //log.debug("Capacity: {}", graphics.getBuffer().capacity());

        assertEquals(80, graphics.getBuffer().remaining());

        graphics.drawText("12345");

        assertEquals(75, graphics.getBuffer().remaining());
        assertEquals(5, graphics.getBuffer().position());
        assertEquals(20, graphics.getWidth());
        assertEquals(4, graphics.getHeight());

        graphics.drawText("this is a really really really really long text");

        displayBuffer(graphics);

        graphics.flush();

        verify(driver).setCursorPosition(0, 0);

        assertEquals(0, graphics.getBuffer().position());
    }

    @Test
    @DisplayName("Test cursor/write operation")
    public void testCursorPosition() {
        graphics.setCursor(0, 0);
        graphics.drawText("Line One");
        graphics.setCursor(0, 1);
        graphics.drawText("Line Two");
        graphics.setCursor(0, 2);
        graphics.drawText("Line Three");
        graphics.setCursor(0, 3);
        graphics.drawText("Line Four");

        ByteBuffer buffer = graphics.getBuffer();

        String row0 = getLine(0, buffer);
        String row1 = getLine(1, buffer);
        String row2 = getLine(2, buffer);
        String row3 = getLine(3, buffer);

        assertNotNull(buffer);

        assertEquals(graphics.getWidth(), row0.length());
        assertEquals(graphics.getWidth(), row1.length());
        assertEquals(graphics.getWidth(), row2.length());
        assertEquals(graphics.getWidth(), row3.length());

        assertEquals("Line One", row0.trim());
        assertEquals("Line Two", row1.trim());
        assertEquals("Line Three", row2.trim());
        assertEquals("Line Four", row3.trim());

        displayBuffer(graphics);
    }

    @Test
    @DisplayName("Test write operation on read-only buffer")
    public void writeToReadOnlyBuffer() {
        assertThrows(ReadOnlyBufferException.class, () -> graphics.getBuffer().putChar('c'));
    }

    @Test
    @DisplayName("Test clear display")
    public void testClearDisplay() {
        graphics.setCursor(0, 0);
        graphics.drawText("Row One");
        graphics.setCursor(0, 1);
        graphics.drawText("Row Two");
        graphics.setCursor(0, 2);
        graphics.drawText("Row Three");
        graphics.setCursor(0, 3);
        graphics.drawText("Row Four");

        String row0_before_clr = getLine(0, graphics.getBuffer());
        assertFalse(isBlank(row0_before_clr));

        graphics.clear();

        String row0_after_clr = getLine(0, graphics.getBuffer());
        assertTrue(isBlank(row0_after_clr));
    }

    @Test
    @DisplayName("Test cursor offset validation")
    public void testOffsetValidation() {
        assertThrows(OffsetOutOfBoundsException.class, () -> graphics.setCursor(20, 0));
        assertThrows(OffsetOutOfBoundsException.class, () -> graphics.setCursor(0, 4));
    }

    private String getLine(int row, ByteBuffer buffer) {
        buffer.position(row * graphics.getWidth());
        byte[] data = new byte[graphics.getWidth()];
        buffer.get(data);
        return new String(data);
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