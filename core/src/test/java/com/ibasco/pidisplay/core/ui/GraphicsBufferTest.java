package com.ibasco.ucgdisplay.core.ui;

import com.ibasco.ucgdisplay.core.util.GraphicsUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GraphicsBufferTest {

    public static final Logger log = LoggerFactory.getLogger(GraphicsBufferTest.class);

    Graphics graphics;

    DisplayBuffer buffer;

    @BeforeEach
    void setUp() {
        graphics = mock(Graphics.class);
        when(graphics.getWidth()).thenReturn(20);
        when(graphics.getHeight()).thenReturn(4);
        buffer = DisplayBuffer.allocate(20, 4);
    }

    @Test
    @DisplayName("Test cursor x coordinates")
    void testCursorXPos() {
        assertEquals(0, GraphicsUtils.calcXOffset(buffer.getWidth(), 20));
        assertEquals(1, GraphicsUtils.calcXOffset(buffer.getWidth(), 21));
        assertEquals(2, GraphicsUtils.calcXOffset(buffer.getWidth(), 22));
        assertEquals(3, GraphicsUtils.calcXOffset(buffer.getWidth(), 23));
        assertEquals(4, GraphicsUtils.calcXOffset(buffer.getWidth(), 24));
        assertEquals(5, GraphicsUtils.calcXOffset(buffer.getWidth(), 25));
        assertEquals(6, GraphicsUtils.calcXOffset(buffer.getWidth(), 26));
        assertEquals(7, GraphicsUtils.calcXOffset(buffer.getWidth(), 27));
        assertEquals(8, GraphicsUtils.calcXOffset(buffer.getWidth(), 28));
        assertEquals(9, GraphicsUtils.calcXOffset(buffer.getWidth(), 29));
        assertEquals(10, GraphicsUtils.calcXOffset(buffer.getWidth(), 30));
        assertEquals(11, GraphicsUtils.calcXOffset(buffer.getWidth(), 31));
        assertEquals(12, GraphicsUtils.calcXOffset(buffer.getWidth(), 32));
        assertEquals(13, GraphicsUtils.calcXOffset(buffer.getWidth(), 33));
        assertEquals(14, GraphicsUtils.calcXOffset(buffer.getWidth(), 34));
        assertEquals(15, GraphicsUtils.calcXOffset(buffer.getWidth(), 35));
        assertEquals(16, GraphicsUtils.calcXOffset(buffer.getWidth(), 36));
        assertEquals(17, GraphicsUtils.calcXOffset(buffer.getWidth(), 37));
        assertEquals(18, GraphicsUtils.calcXOffset(buffer.getWidth(), 38));
        assertEquals(19, GraphicsUtils.calcXOffset(buffer.getWidth(), 39));
    }

    @Test
    void testWriteGet() {
        buffer.cursor(0, 0);
        buffer.put("hello world".getBytes());
        buffer.cursor(5, 1);
        buffer.put("test".getBytes());
        buffer.cursor(0, 3);
        buffer.put("12345".getBytes());
        log.debug("X = {}, Y = {}, Pos = {}, Remaining = {}, Limit = {}", buffer.cursorX(), buffer.cursorY(), buffer.cursor(), buffer.remaining(), buffer.limit());
        byte[] data = new byte[80], tmp = new byte[20];
        buffer.cursor(0, 0);
        int row = 0, offset = 0;
        //Simulate flush
        while (buffer.hasRemaining()) {
            buffer.get(tmp);
            System.arraycopy(tmp, 0, data, offset, tmp.length);
            offset += tmp.length;
            log.debug("{}) Processing Row: {}, Length: {}", row++, new String(tmp), tmp.length);
        }
        log.debug("Data Retrieved: {}", new String(buffer.array()));
        buffer.clear(true);
        log.debug("Data Retrieved: {} (after clear)", new String(buffer.array()));
    }

    private void show(byte[] data) {
        StringBuilder sb = new StringBuilder();
        log.debug("  {} ", StringUtils.repeat('-', buffer.getWidth()));
        for (int i = 0; i < data.length; i++) {
            char c = (data[i] == 0) ? ' ' : (char) data[i];
            sb.append(c);
            if (GraphicsUtils.calcXOffset(buffer.getWidth(), i) == buffer.getWidth() - 1) {
                log.debug("{}|{}|", GraphicsUtils.calcYOffset(buffer.getWidth(), buffer.getHeight(), i), sb.toString());
                if (GraphicsUtils.calcYOffset(buffer.getWidth(), buffer.getHeight(), i) < buffer.getHeight())
                    sb.setLength(0);
            }

        }
        log.debug("  {} ", StringUtils.repeat('-', buffer.getWidth()));
    }
}