package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdDriverException;

@SuppressWarnings("Duplicates")
public class GlcdDriver extends GlcdBaseDriver {
    public GlcdDriver(GlcdConfig config) {
        super(config);
        try {
            initialize();
        } catch (GlcdDriverException e) {
            throw new RuntimeException(e);
        }
    }
}
