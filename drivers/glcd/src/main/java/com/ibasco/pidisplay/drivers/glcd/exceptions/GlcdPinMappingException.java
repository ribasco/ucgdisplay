package com.ibasco.pidisplay.drivers.glcd.exceptions;

import com.ibasco.pidisplay.drivers.glcd.GlcdPinMapConfig;

public class GlcdPinMappingException extends GlcdException {
    private GlcdPinMapConfig pinMapConfig;

    public GlcdPinMappingException(String message, GlcdPinMapConfig pinMapConfig) {
        super(message);
        this.pinMapConfig = pinMapConfig;
    }

    public GlcdPinMapConfig getPinMapConfig() {
        return pinMapConfig;
    }
}
