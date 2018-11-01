package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdPinMappingException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GlcdPinMapConfigTest {

    @Test
    @DisplayName("Test map to byte array conversion")
    void testByteArrayOutput() {
        GlcdPinMapConfig mapConfig = new GlcdPinMapConfig();
        mapConfig.map(GlcdPin.D0, 0);
        mapConfig.map(GlcdPin.D1, 1);
        mapConfig.map(GlcdPin.D2, 2);
        mapConfig.map(GlcdPin.D3, 3);
        mapConfig.map(GlcdPin.D4, 4);
        mapConfig.map(GlcdPin.D5, 5);
        mapConfig.map(GlcdPin.D6, 6);
        mapConfig.map(GlcdPin.D7, 7);
        byte[] pinData = mapConfig.toByteArray();
        assertEquals(0, pinData[0]);
        assertEquals(1, pinData[1]);
        assertEquals(2, pinData[2]);
        assertEquals(3, pinData[3]);
        assertEquals(4, pinData[4]);
        assertEquals(5, pinData[5]);
        assertEquals(6, pinData[6]);
        assertEquals(7, pinData[7]);
    }

    @Test
    @DisplayName("Test pin value greater than 255")
    void testInvalidPinNumber() {
        GlcdPinMapConfig mapConfig = new GlcdPinMapConfig();
        assertThrows(GlcdPinMappingException.class, () -> mapConfig.map(GlcdPin.D0, 300));
    }

    @Test
    @DisplayName("Test pin existence on the map")
    void testMapExistence() {
        GlcdPinMapConfig mapConfig = new GlcdPinMapConfig();
        mapConfig.map(GlcdPin.D0, 0);
        mapConfig.map(GlcdPin.D2, 2);
        assertTrue(mapConfig.isMapped(GlcdPin.D0));
        assertTrue(mapConfig.isMapped(GlcdPin.D2));
        assertFalse(mapConfig.isMapped(GlcdPin.D1));
    }

    @Test
    @DisplayName("Test empty map")
    void testEmptyMap() {
        GlcdPinMapConfig config = new GlcdPinMapConfig();
        assertTrue(config.isEmpty());
        config.map(GlcdPin.D0, 0);
        assertFalse(config.isEmpty());
    }
}