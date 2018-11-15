/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: GlcdDriver.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
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
        this(config, virtual, handler, null);
    }

    public GlcdDriver(GlcdConfig config, boolean virtual, GlcdDriverEventHandler handler, GlcdDriverAdapter driverAdapter) {
        super(config, virtual, handler, driverAdapter);
        initialize();
    }
}
