package com.ibasco.pidisplay.core.util;

import com.ibasco.pidisplay.core.ui.DisplayBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphicsUtilsTest {

    public static final Logger log = LoggerFactory.getLogger(GraphicsUtilsTest.class);

    final int width = 20;
    final int height = 4;
    final int size = width * height;

    @Test
    @DisplayName("Test x-offset calculation")
    void testCalcXOffset() {
        int expected = 0;
        for (int i = 0; i < size; i++) {
            //log.debug("X = {}, Y = {}, POS = {}, EXP = {}", GraphicsUtils.calcXOffset(width, i), GraphicsUtils.calcYOffset(width, height, i), i, expected);
            assertEquals(expected, GraphicsUtils.calcXOffset(width, i));
            if (expected++ == (width - 1)) {
                expected = 0;
            }
        }
    }

    @Test
    @DisplayName("Test y-offset calculation")
    void testCalcYOffset() {
        int col = 0, expected = 0;
        for (int i = 0; i < size; i++) {
            assertEquals(expected, GraphicsUtils.calcYOffset(width, height, i));
            //log.debug("y = {}, pos = {}", GraphicsUtils.calcYOffset(width, height, i), i);
            if (col++ == (width - 1)) {
                col = 0;
                expected++;
            }
        }
    }

    @Test
    @DisplayName("Test absolute offset calculation")
    void testCalcOffset() {
        int expected_offset = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                //log.debug("x = {}, y = {}, expected = {}, actual = {}", col, row, expected_offset, col + (row * width));
                assertEquals(expected_offset, GraphicsUtils.calcOffset(width, col, row));
                expected_offset++;
            }
        }
    }

    @Test
    @DisplayName("Test print display buffer")
    void testPrintDisplayBuffer() {
        DisplayBuffer buffer = DisplayBuffer.allocate(20, 4);

        buffer.cursor(0, 0);
        buffer.put("hello world".getBytes());
        buffer.cursor(0, 1);
        buffer.put("hello world".getBytes());
        buffer.cursor(0, 2);
        buffer.put("hello world".getBytes());
        buffer.cursor(0, 3);
        buffer.put("hello world".getBytes());

        StringBuilder output = new StringBuilder();
        GraphicsUtils.printBuffer(buffer, output);
        log.debug("Output:\n{}", output.toString());
    }

}