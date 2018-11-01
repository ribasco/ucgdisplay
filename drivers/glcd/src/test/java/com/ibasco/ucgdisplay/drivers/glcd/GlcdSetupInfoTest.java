package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.core.u8g2.U8g2Graphics;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GlcdSetupInfoTest {
    @Test
    @DisplayName("Test isSupported() function")
    void testSupportedBusInterfaces() {
        GlcdSetupInfo setupInfo = new GlcdSetupInfo("test_setup_f", U8g2Graphics.COM_8080 | U8g2Graphics.COM_3WSPI | U8g2Graphics.COM_I2C);

        assertEquals("test_setup_f", setupInfo.getFunction());
        assertEquals(0x1A, setupInfo.getProtocols());

        assertTrue(setupInfo.isSupported(GlcdBusInterface.PARALLEL_8080));
        assertTrue(setupInfo.isSupported(GlcdBusInterface.SPI_SW_3WIRE));
        assertTrue(setupInfo.isSupported(GlcdBusInterface.I2C_HW));
        assertTrue(setupInfo.isSupported(GlcdBusInterface.I2C_SW));

        assertFalse(setupInfo.isSupported(GlcdBusInterface.SERIAL_HW));
        assertFalse(setupInfo.isSupported(GlcdBusInterface.PARALLEL_6800));
        assertFalse(setupInfo.isSupported(GlcdBusInterface.PARALLEL_6800_KS0108));
        assertFalse(setupInfo.isSupported(GlcdBusInterface.SPI_SW_4WIRE));
        assertFalse(setupInfo.isSupported(GlcdBusInterface.SPI_HW_4WIRE_ST7920));
    }
}