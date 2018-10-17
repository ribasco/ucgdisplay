package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdDriverException;

/**
 * A basic implementation of a graphics display driver
 *
 * @author Rafael Ibasco
 */
public class GlcdDriver extends GlcdBaseDriver {
    /**
     * @throws GlcdDriverException
     *         When driver initialization fails
     */
    public GlcdDriver(GlcdConfig config) throws GlcdDriverException {
        this(config, false);
    }

    /**
     * @throws GlcdDriverException
     *         When driver initialization fails
     */
    public GlcdDriver(GlcdConfig config, boolean virtual) throws GlcdDriverException {
        this(config, virtual, null);
    }

    /**
     * @throws GlcdDriverException
     *         When driver initialization fails
     */
    public GlcdDriver(GlcdConfig config, boolean virtual, GlcdDriverEventHandler handler) throws GlcdDriverException {
        super(config, virtual, handler);
        initialize();
    }
}
