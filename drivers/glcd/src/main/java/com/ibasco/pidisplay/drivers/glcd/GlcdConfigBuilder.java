package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdProtocol;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdRotation;

/**
 * Utility class for building {@link GlcdConfig} instances
 *
 * @author Rafael Ibasco
 */
public class GlcdConfigBuilder {
    private GlcdConfig config;

    private GlcdConfigBuilder() {
        config = new GlcdConfig();
    }

    public static GlcdConfigBuilder create() {
        return new GlcdConfigBuilder();
    }

    public GlcdConfigBuilder display(GlcdDisplay display) {
        config.setDisplay(display);
        return this;
    }

    public GlcdConfigBuilder protocol(GlcdProtocol protocol) {
        config.setProtocol(protocol);
        return this;
    }

    public GlcdConfigBuilder pinMap(GlcdPinMapConfig pinmap) {
        config.setPinMapConfig(pinmap);
        return this;
    }

    public GlcdConfigBuilder rotation(GlcdRotation rotation) {
        config.setRotation(rotation);
        return this;
    }

    public GlcdConfig build() {
        return config;
    }
}
