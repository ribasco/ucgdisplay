package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdPin;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GlcdConfigBuilderTest {

    GlcdConfigBuilder configBuilder;

    @BeforeEach
    void setUp() {
        configBuilder = GlcdConfigBuilder.create();
    }

    @Test
    void testCalledMappedPinShortcut() {
        assertNotNull(configBuilder.mapPin(GlcdPin.D0, 10));
        GlcdConfig config = configBuilder.build();
        assertNotNull(config);
        assertNotNull(config.getPinMap());
    }

    @Test
    void testUncalledMappedPinShortcut() {
        GlcdConfig config = configBuilder.build();
        assertNotNull(config);
        assertNull(config.getPinMap());
    }

    @Test
    void testDirectSetMapConfig() {
        GlcdPinMapConfig mapConfig = new GlcdPinMapConfig();
        GlcdConfig config = configBuilder.pinMap(mapConfig).mapPin(GlcdPin.D0, 10).build();
        assertNotNull(config);
        assertSame(mapConfig, config.getPinMap());
    }
}