package com.ibasco.ucgdisplay.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilsTest {

    public static final Logger log = LoggerFactory.getLogger(ArrayUtilsTest.class);

    @Test
    @DisplayName("Test merge byte array")
    void testMerge() {
        byte[] lhs = "test1234     A ".getBytes();
        byte[] rhs = "t3ST5324   X   ".getBytes();
        assertEquals(13, ArrayUtils.merge(lhs, rhs));
        assertEquals("t3ST5324   X   ", new String(lhs));
    }

    @Test
    @DisplayName("Test difference between two byte arrays")
    void testDiff() {
        byte[] lhs = new byte[68];
        byte[] rhs = "   hello                  3          2        1      5      2      7".getBytes();
        byte[] diff = ArrayUtils.diff(lhs, rhs);

        log.debug("Diff: {}", new String(diff));
        for (int i = 0; i < diff.length; i++)
            log.debug("{} = {}", i, diff[i]);
        assertEquals(lhs.length, rhs.length);
        assertNotNull(lhs);
    }

    @Test
    @DisplayName("Test index search for non-null bytes")
    void testIndexSearch() {
        byte[] lhs = new byte[2048];
        lhs[618] = 3;
        lhs[1086] = 10;
        Predicate<Byte> criteria = p -> p != 0; //byte not null
        assertEquals(618, ArrayUtils.indexOf(lhs, 0, criteria));
        assertEquals(1086, ArrayUtils.indexOf(lhs, 619, criteria));
        assertEquals(-1, ArrayUtils.indexOf(lhs, 1087, criteria));
    }

    @Test
    @DisplayName("Test method with null arguments")
    void testNullArgs() {
        byte[] lhs = null;
        byte[] rhs = "   hello                  3          2        1      5      2  7".getBytes();
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.diff(lhs, rhs));
    }

    @Test
    @DisplayName("Test method with empty arguments")
    void testEmptyArgs() {
        byte[] lhs = new byte[0];
        byte[] rhs = new byte[0];
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.diff(lhs, rhs));
    }
}