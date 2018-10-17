package com.ibasco.ucgdisplay.drivers.glcd.exceptions;

import com.ibasco.ucgdisplay.drivers.glcd.GlcdPinMapConfig;

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
