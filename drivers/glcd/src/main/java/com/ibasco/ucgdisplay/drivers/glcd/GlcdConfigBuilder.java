/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2021 Universal Character/Graphics display library
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

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdCommProtocol;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdPin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for building {@link GlcdConfig} instances
 *
 * @author Rafael Ibasco
 */
public class GlcdConfigBuilder {
    private static final Logger log = LoggerFactory.getLogger(GlcdConfigBuilder.class);

    private GlcdConfig config;

    private GlcdPinMapConfig pinMapConfig;

    /**
     * Create a new {@link GlcdConfigBuilder} instance. The internal {@link GlcdConfig} will be initialized in this constructor.
     */
    private GlcdConfigBuilder() {
        config = new GlcdConfig();
    }

    /**
     * Factory method for creating a new instance of this class together with it's required parameters.
     *
     * @param display
     *         The matching {@link GlcdDisplay} of your display device.
     * @param busInterface
     *         The bus interface to be used for communication for your display device.
     *
     * @return A new instance of {@link GlcdConfigBuilder}
     */
    public static GlcdConfigBuilder create(GlcdDisplay display, GlcdCommProtocol busInterface) {
        GlcdConfigBuilder configBuilder = new GlcdConfigBuilder();
        configBuilder.config.setDisplay(display);
        configBuilder.config.setBusInterface(busInterface);
        return configBuilder;
    }

    /**
     * Adds a new configuration option for the display controller. Existing option entries are replaced.
     *
     * @param option
     *         The display configuration option.
     * @param value
     *         The value of the configuration option. If null is provided, the option would be deleted from the underlying map.
     * @param <T>
     *         Capture of the {@link GlcdOption} type.
     *
     * @return This instance
     *
     * @see GlcdOption
     */
    public <T> GlcdConfigBuilder option(GlcdOption<T> option, T value) {
        processOption(option, value);
        return this;
    }

    /**
     * Copies the provided map to this instance. Existing entries are retained but duplicate entries would be overwritten.
     *
     * @param options
     *         The options map to copy
     *
     * @return This instance
     */
    public GlcdConfigBuilder options(Map<GlcdOption<?>, Object> options) {
        return this.options(options, false);
    }

    /**
     * Copies the provided map to this instance.
     *
     * @param options
     *         The options map to copy
     * @param clearEntries
     *         Clears the map and overwrites all existing entries
     *
     * @return This instance
     */
    public GlcdConfigBuilder options(Map<GlcdOption<?>, Object> options, boolean clearEntries) {
        if (clearEntries)
            config.getOptions().clear();
        options.forEach(this::processOption);
        return this;
    }

    /**
     * Adds a new configuration option for the display controller. This is similar to {@link #option(GlcdOption, Object)}
     * but this accepts a String value as key, allowing you to provide other values.
     *
     * @param option
     *         The name/key of the configuration option.
     * @param value
     *         The value of the configuration option.
     * @param <T>
     *         The captured type of the configuration option value
     *
     * @return This instance
     *
     * @see GlcdOption
     */
    public <T> GlcdConfigBuilder option(String option, T value) {
        config.getOptions().put(option, value);
        return this;
    }

    /**
     * Set a pin map configuration to be used by the display controller.
     *
     * @param pinmap
     *         The pin map configuration
     *
     * @return This instance
     *
     * @see GlcdPin
     */
    public GlcdConfigBuilder pinMap(GlcdPinMapConfig pinmap) {
        config.setPinMapConfig(pinmap);
        return this;
    }

    /**
     * Maps a {@link GlcdPin} to a numeric pin of the SoC. For Raspberry Pi devices, the pin numbering follows the BCM pinout (0 - 53).
     * This is just a shortcut for {@link #pinMap(GlcdPinMapConfig)}
     *
     * @param glcdPin
     *         The {@link GlcdPin} enumeration
     * @param value
     *         The GPIO pin number on your SoC device.
     *
     * @return This instance
     *
     * @see #pinMap(GlcdPinMapConfig)
     */
    public GlcdConfigBuilder mapPin(GlcdPin glcdPin, int value) {
        if (this.pinMapConfig == null)
            this.pinMapConfig = new GlcdPinMapConfig();
        this.pinMapConfig.map(glcdPin, value);
        return this;
    }

    /**
     * Builds a {@link GlcdConfig} instance containing all the provided configuration options.
     *
     * @return The {@link GlcdConfig} to be used by U8g2
     */
    public GlcdConfig build() {
        if (config.getPinMap() == null && this.pinMapConfig != null)
            config.setPinMapConfig(this.pinMapConfig);
        return config;
    }

    private void processOption(GlcdOption<?> option, Object value) {
        if (value == null) {
            config.getOptions().remove(option.getName());
        } else {
            if (GlcdOptionValue.class.isAssignableFrom(value.getClass())) {
                //Process special enum types
                GlcdOptionValue<?> optionValue = (GlcdOptionValue<?>) value;

                if (!optionValueTypeSupported(optionValue))
                    throw new IllegalStateException("The value type of option '" + option + "' value is not supported");

                log.debug("option() : Found GlcdOptionValue type for key '{}'. Converting to reference type", option.getName());
                config.getOptions().put(option.getName(), optionValue.toValue());
            } else {
                if (!valueTypeSupported(value)) {
                    log.warn("option() : The value type is currently not supported by the native library. Supported types are: " + Arrays.stream(getSupportedTypes())
                            .map(Class::getSimpleName)
                            .collect(Collectors.joining(", ")));
                }
                config.getOptions().put(option.getName(), value);
            }
        }
    }

    private Class<?>[] getSupportedTypes() {
        return new Class[]{Integer.class, String.class};
    }

    private boolean valueTypeSupported(Object value) {
        if (value == null)
            return false;
        return Arrays.stream(getSupportedTypes()).anyMatch(c -> c.equals(value.getClass()));
    }

    private boolean optionValueTypeSupported(GlcdOptionValue<?> optionValue) {
        return Arrays.stream(getSupportedTypes()).anyMatch(c -> c.equals(optionValue.getType()));
    }
}
