/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD Driver
 * Filename: GlcdSetupInfo.java
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

/**
 * Holds information of u8g2 setup procedure and supported bus protocols of a {@link GlcdDisplay}
 *
 * @author Rafael Ibasco
 */
public final class GlcdSetupInfo {
    private int protocols;
    private String function;

    GlcdSetupInfo(String setupFunction, int supportedProtocols) {
        this.protocols = supportedProtocols;
        this.function = setupFunction;
    }

    int getProtocols() {
        return protocols;
    }

    public String getFunction() {
        return function;
    }

    public boolean isSupported(GlcdBusInterface protocol) {
        return (protocols & protocol.getValue()) > 0;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GlcdSetupInfo{");
        sb.append("protocols=").append(protocols);
        sb.append(", function='").append(function).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
