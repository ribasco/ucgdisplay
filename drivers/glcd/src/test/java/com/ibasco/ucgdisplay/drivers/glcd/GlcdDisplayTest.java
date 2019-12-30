package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBufferType;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdControllerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlcdDisplayTest {

    @Test
    void testEquals() {
        assertEquals(Glcd.ST7920.D_128x64, new GlcdDisplay(GlcdControllerType.ST7920, "D_128x64", 16,8, GlcdBufferType.HORIZONTAL));
        assertNotEquals(Glcd.ST7920.D_192x32, new GlcdDisplay(GlcdControllerType.ST7920, "D_128x64", 16,8, GlcdBufferType.HORIZONTAL));
    }
}