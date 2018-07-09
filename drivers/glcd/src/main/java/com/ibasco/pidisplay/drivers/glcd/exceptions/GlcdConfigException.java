package com.ibasco.pidisplay.drivers.glcd.exceptions;

import com.ibasco.pidisplay.drivers.glcd.GlcdConfig;

public class GlcdConfigException extends GlcdDriverException {
    private GlcdConfig config;

    public GlcdConfigException(String message, GlcdConfig config) {
        super(message);
        this.config = config;
    }

    public GlcdConfig getConfig() {
        return config;
    }
}
