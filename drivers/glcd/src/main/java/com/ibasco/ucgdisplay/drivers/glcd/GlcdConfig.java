/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: GlcdConfig.java
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
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdSize;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdConfigException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Arrays;

/**
 * Configuration class to be used by the glcd native library
 *
 * @author Rafael Ibasco
 */
public class GlcdConfig {

    public static final Logger log = getLogger(GlcdConfig.class);

    private GlcdDisplay display;
    private GlcdBusInterface busInterface;
    private GlcdRotation rotation;
    private GlcdPinMapConfig pinMap;
    private int deviceAddress = -1;
    private String setupProcedure;

    /**
     * @return The device address if available
     */
    public int getDeviceAddress() {
        return deviceAddress;
    }

    /**
     * Sets the device address of the display. This is typically used for I2C communication.
     *
     * @param deviceAddress
     *         The device address of the display
     */
    public void setDeviceAddress(int deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public void setDisplay(GlcdDisplay display) {
        this.display = display;
    }

    public void setBusInterface(GlcdBusInterface busInterface) {
        this.busInterface = busInterface;
    }

    public void setRotation(GlcdRotation rotation) {
        this.rotation = rotation;
    }

    public void setPinMapConfig(GlcdPinMapConfig pinMap) {
        this.pinMap = pinMap;
    }

    public GlcdSize getDisplaySize() {
        return display.getDisplaySize();
    }

    public GlcdBusInterface getBusInterface() {
        return busInterface;
    }

    public GlcdPinMapConfig getPinMap() {
        return pinMap;
    }

    public GlcdDisplay getDisplay() {
        return display;
    }

    public GlcdRotation getRotation() {
        return rotation;
    }

    public String getSetupProcedure() {
        if (StringUtils.isBlank(setupProcedure)) {
            setupProcedure = lookupSetupInfo().getFunction();
        }
        return setupProcedure;
    }

    private GlcdSetupInfo lookupSetupInfo() {
        if (display == null) {
            throw new GlcdConfigException("Display has not been set", this);
        }
        if (busInterface == null) {
            throw new GlcdConfigException("Protocol not set", this);
        }

        GlcdSetupInfo setupInfo = Arrays.stream(display.getSetupDetails())
                .filter(setup -> setup.isSupported(busInterface))
                .findFirst()
                .orElse(null);

        if (setupInfo == null)
            throw new GlcdConfigException(String.format("Could not find a suitable setup procedure for bus interface '%s'", busInterface.name()), this);

        log.debug("Using display setup procedure (Display: {}, Protocol: {}, Setup Proc: {}))", display.getName(), busInterface.name(), setupInfo.getFunction());

        return setupInfo;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
