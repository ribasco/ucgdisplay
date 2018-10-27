/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: GlcdConfigBuilder.java
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

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdRotation;

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

    public GlcdConfigBuilder busInterface(GlcdBusInterface protocol) {
        config.setBusInterface(protocol);
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
