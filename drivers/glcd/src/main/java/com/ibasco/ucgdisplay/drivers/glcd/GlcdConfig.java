/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
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
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdSize;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdConfigException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Configuration class to be used for the display controller. Use {@link GlcdConfigBuilder} to setup your configuration.
 *
 * @author Rafael Ibasco
 */
public class GlcdConfig {

    private static final Logger log = getLogger(GlcdConfig.class);

    private GlcdDisplay display;
    private GlcdBusInterface busInterface;
    private GlcdPinMapConfig pinMap;
    private String setupProcedure;
    private Map<String, Object> options = new HashMap<>();

    GlcdConfig() {
        //package-private
    }

    /**
     * @return The screen size of the selected display controller
     */
    public GlcdSize getDisplaySize() {
        return display.getDisplaySize();
    }

    /**
     * @return The current bus interface for the selected display controller
     */
    public GlcdBusInterface getBusInterface() {
        return busInterface;
    }

    /**
     * @return The current pin map configuration for the selected display controller
     */
    public GlcdPinMapConfig getPinMap() {
        return pinMap;
    }

    /**
     * @return The selected display controller
     */
    public GlcdDisplay getDisplay() {
        return display;
    }

    /**
     * @return A map containing all the provided configuration options of the selected display controller
     */
    Map<String, Object> getOptions() {
        return options;
    }

    /**
     * Retrieve a configuration option value
     *
     * @param option
     *         The configuration option
     * @param <T>
     *         The captured type of the return value
     *
     * @return The value of the configuration option or null if the option does not exist.
     */
    public <T> T getOption(GlcdOption<?> option) {
        //noinspection unchecked
        return (T) options.getOrDefault(option.getName(), null);
    }

    /**
     * Retrieve a configuration option value
     *
     * @param option
     *         The configuration option
     * @param defaultVal
     *         The default value to return in case the option does not exist
     * @param <T>
     *         The captured type of the return value
     *
     * @return The value of the configuration option or null if the option does not exist.
     */
    public <T> T getOption(GlcdOption<T> option, T defaultVal) {
        //noinspection unchecked
        return (T) options.getOrDefault(option.getName(), defaultVal);
    }

    public Object getOption(String option) {
        return options.get(option);
    }

    /**
     * @return The u8g2 setup procedure that is going to be used by native library
     */
    public String getSetupProcedure() {
        if (StringUtils.isBlank(setupProcedure))
            setupProcedure = lookupSetupInfo().getFunction();
        return setupProcedure;
    }

    /**
     * Finds the suitable u8g2 setup procedure based on the selected display controller and bus interface
     *
     * @return The display setup information
     *
     * @throws GlcdConfigException
     *         If any of the required parameters are missing or if no suitable setup procedure was found
     * @see GlcdSetupInfo
     */
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

    <T> void setOption(GlcdOption<T> option, T value) {
        options.put(option.getName(), value);
    }

    void setOption(String option, Object value) {
        options.put(option, value);
    }

    void setDisplay(GlcdDisplay display) {
        this.display = display;
    }

    void setBusInterface(GlcdBusInterface busInterface) {
        this.busInterface = busInterface;
    }

    void setPinMapConfig(GlcdPinMapConfig pinMap) {
        this.pinMap = pinMap;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlcdConfig that = (GlcdConfig) o;
        return display.equals(that.display) &&
                busInterface == that.busInterface;
    }

    @Override
    public int hashCode() {
        return Objects.hash(display, busInterface);
    }
}
