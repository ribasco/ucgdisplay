/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: GlcdDisplay.java
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
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdControllerType;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdSize;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Holds important meta data information of a Graphics display device
 *
 * @author Rafael Ibasco
 */
public class GlcdDisplay {

    public static final Logger log = getLogger(GlcdDisplay.class);

    private String name;
    private GlcdControllerType controller;
    private GlcdSetupInfo[] setupDetails;
    private GlcdSize displaySize;

    GlcdDisplay(GlcdControllerType controller, String name, int tileWidth, int tileHeight, GlcdSetupInfo... setupInfo) {
        this.name = name;
        this.controller = controller;
        this.setupDetails = setupInfo;
        this.displaySize = GlcdSize.get(tileWidth, tileHeight);
    }

    /**
     * Check if this display supports the provided bus interface
     *
     * @param busInterface
     *         The {@link GlcdBusInterface} to check
     *
     * @return <code>True</code> if this display supports the provided bus interface
     */
    public boolean hasBusInterface(GlcdBusInterface busInterface) {
        for (GlcdSetupInfo info : setupDetails) {
            if ((info.getProtocols() & busInterface.getValue()) > 0)
                return true;
        }
        return false;
    }

    /**
     * @return A list of supported bus interfaces of this display
     */
    public List<GlcdBusInterface> getBusInterfaces() {
        if (setupDetails == null || setupDetails.length <= 0)
            return null;
        List<GlcdBusInterface> protocols = new ArrayList<>();
        for (GlcdSetupInfo setup : setupDetails) {
            for (GlcdBusInterface protocol : GlcdBusInterface.values()) {
                if ((setup.getProtocols() & protocol.getValue()) > 0)
                    protocols.add(protocol);
            }
        }
        return protocols;
    }

    /**
     * @return A {@link GlcdSize} containing information about the display's dimensions
     */
    public GlcdSize getDisplaySize() {
        return displaySize;
    }

    /**
     * @return A {@link GlcdControllerType} describing the controller of this display
     */
    public GlcdControllerType getController() {
        return controller;
    }

    public String getName() {
        return name;
    }

    public GlcdSetupInfo[] getSetupDetails() {
        return setupDetails;
    }
}
