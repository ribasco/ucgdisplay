package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdDriverException;

@SuppressWarnings("Duplicates")
public class GlcdDriver extends GlcdBaseDriver {
    public GlcdDriver(GlcdConfig config) throws GlcdDriverException {
        super(config);
        initialize();
    }
}
