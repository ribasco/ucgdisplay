/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Graphics LCD driver
 * Filename: GlcdBaseDriver.java
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

import com.ibasco.ucgdisplay.core.u8g2.U8g2ByteEvent;
import com.ibasco.ucgdisplay.core.u8g2.U8g2EventDispatcher;
import com.ibasco.ucgdisplay.core.u8g2.U8g2GpioEvent;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdFont;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdRotation;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdConfigException;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdDriverException;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdNotInitializedException;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Base graphics display driver implementation
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings({"WeakerAccess", "unused", "unchecked"})
abstract public class GlcdBaseDriver implements U8g2DisplayDriver {

    private static final Logger log = LoggerFactory.getLogger(GlcdBaseDriver.class);

    private static final GlcdRotation DEFAULT_ROTATION = GlcdRotation.ROTATION_90;

    private GlcdConfig config;

    private GlcdDriverEventHandler driverEventHandler;

    private boolean initialized = false;

    private boolean virtual;

    private GlcdDriverAdapter adapter;

    /**
     * Create new instance based on the config provided.
     *
     * @param config
     *         The {@link GlcdConfig} associated with this instance
     */
    public GlcdBaseDriver(GlcdConfig config) {
        this(config, false);
    }

    /**
     * Create new instance based on the config provided.
     *
     * @param config
     *         The {@link GlcdConfig} associated with this instance
     * @param virtual
     *         Set to <code>true</code> to enable virtual mode.
     */
    protected GlcdBaseDriver(GlcdConfig config, boolean virtual) {
        this(config, virtual, null);
    }

    protected GlcdBaseDriver(GlcdConfig config, boolean virtual, GlcdDriverEventHandler handler) {
        this(config, virtual, handler, null);
    }

    /**
     * <p>Creates a new instance based on the config provided. If virtual mode is enabled. Optionally you could also
     * provided a {@link GlcdDriverEventHandler} for handling display events </p>
     *
     * @param config
     *         The {@link GlcdConfig} associated with this instance
     * @param virtual
     *         Set to <code>true</code> to enable virtual mode. If virtual mode is enabled, all instruction/data events are routed to {@link GlcdDriverEventHandler} for further processing.
     * @param handler
     *         The {@link GlcdDriverEventHandler} instance that will handle the data and instruction events thrown by
     *         the native display driver. If a null value is provided, the {@link U8g2DriverAdapter} will be used by default.
     */
    protected GlcdBaseDriver(GlcdConfig config, boolean virtual, GlcdDriverEventHandler handler, GlcdDriverAdapter driverAdapter) {
        this.config = config;
        this.virtual = virtual;
        this.driverEventHandler = handler == null ? createDefaultEventHandler() : handler;
        this.adapter = driverAdapter == null ? new U8g2DriverAdapter() : driverAdapter;
    }

    /**
     * <p>Driver initialization procedures. Sub-classes MUST call this method after call to base constructor.
     * Calling the drawing operations while not initialized will trigger a {@link GlcdDriverException}.</p>
     *
     * @throws GlcdDriverException
     *         When a driver related exception occurs (e.g. invalid configuration setup)
     */
    protected void initialize() throws GlcdDriverException {
        //Make sure we have a valid configuration
        checkConfig(config);

        adapter.initialize(config, virtual);

        if (virtual && driverEventHandler != null) {
            //check if a virtual event handler is present
            U8g2EventDispatcher.addByteListener(this, driverEventHandler);
            U8g2EventDispatcher.addGpioListener(this, driverEventHandler);
        }

        initialized = true;
        log.debug("GLCD driver initialized (Address: {})", getId());
    }

    protected boolean isInitialized() {
        return initialized;
    }

    public final <T extends GlcdDriverEventHandler> T getDriverEventHandler() {
        return (T) driverEventHandler;
    }

    protected final void setDriverEventHandler(GlcdDriverEventHandler driverEventHandler) {
        this.driverEventHandler = driverEventHandler;
    }

    /**
     * Get the underlying {@link GlcdConfig} used by this instance
     *
     * @return The {@link GlcdConfig}
     */
    public final GlcdConfig getConfig() {
        return config;
    }

    /**
     * Called during instruction/data events
     *
     * @param event
     *         The event details
     */
    protected void onByteEvent(U8g2ByteEvent event) {
        //no-op
    }

    /**
     * Called when a gpio event has occured
     *
     * @param event
     *         The event details
     */
    protected void onGpioEvent(U8g2GpioEvent event) {
        //no-op
    }

    protected GlcdDriverEventHandler createDefaultEventHandler() {
        return new GlcdDriverEventHandler() {
            @Override
            public void onByteEvent(U8g2ByteEvent event) {
                GlcdBaseDriver.this.onByteEvent(event);
            }

            @Override
            public void onGpioEvent(U8g2GpioEvent event) {
                GlcdBaseDriver.this.onGpioEvent(event);
            }
        };
    }

    /**
     * Validates the configuration file specified
     *
     * @param config
     *         The {@link GlcdConfig} instance
     *
     * @throws GlcdConfigException
     *         Throws when a validation error occurs
     */
    protected void checkConfig(GlcdConfig config) throws GlcdConfigException {
        if (config == null)
            throw new GlcdConfigException("Config cannot be null", null);

        GlcdBusInterface bus = config.getBusInterface();

        if (!virtual && bus == null)
            throw new GlcdConfigException("Bus interface not specified", config);

        //Check protocol if supported
        if (bus != null && !config.getDisplay().hasBusInterface(config.getBusInterface())) {
            String protocols = config.getDisplay().getBusInterfaces().stream().map(Object::toString).collect(Collectors.joining(", "));
            throw new GlcdConfigException(
                    String.format("The selected bus interface '%s' is not supported by your display controller '%s :: %s' (Supported Interfaces: %s)", config.getBusInterface().name(), config.getDisplay().getController().name(), config.getDisplay().getName(), protocols),
                    config
            );
        }

        //If I2C is selected, make sure device address is not empty
        if (((config.getBusInterface() == GlcdBusInterface.I2C_HW) || (config.getBusInterface() == GlcdBusInterface.I2C_SW)) && config.getDeviceAddress() == -1) {
            throw new GlcdConfigException("You must specify a device address for I2C comm interface", config);
        }

        //Check display
        if (config.getDisplay() == null)
            throw new GlcdConfigException("No display specified", config);

        //Check pin mapping
        if (!virtual && (config.getPinMap() == null || config.getPinMap().isEmpty())) {
            throw new GlcdConfigException("Missing pin map configuration", config);
        }

        //Check if rotation was specified, otherwise assign default
        if (config.getRotation() == null) {
            log.warn("No rotation specified. Using default = {}", DEFAULT_ROTATION);
            config.setRotation(DEFAULT_ROTATION);
        }
    }

    private void checkRequirements() {
        if (!initialized)
            throw new GlcdNotInitializedException("Driver has not yet been initialized. Please remember to call initialize() in your implementing class");
    }

    @Override
    public void drawBox(int width, int height) {
        checkRequirements();
        adapter.drawBox(width, height);
    }

    @Override
    public void drawBox(int x, int y, int width, int height) {
        checkRequirements();
        adapter.drawBox(x, y, width, height);
    }

    @Override
    @Deprecated
    public void drawBitmap(int count, int height, byte[] bitmap) {
        checkRequirements();
        adapter.drawBitmap(count, height, bitmap);
    }

    @Override
    @Deprecated
    public void drawBitmap(int x, int y, int count, int height, byte[] bitmap) {
        checkRequirements();
        adapter.drawBitmap(x, y, count, height, bitmap);
    }

    @Override
    public void drawCircle(int radius, int options) {
        checkRequirements();
        adapter.drawCircle(radius, options);
    }

    @Override
    public void drawCircle(int x, int y, int radius, int options) {
        checkRequirements();
        adapter.drawCircle(x, y, radius, options);
    }

    @Override
    public void drawDisc(int radius, int options) {
        checkRequirements();
        adapter.drawDisc(radius, options);
    }

    @Override
    public void drawDisc(int x, int y, int radius, int options) {
        checkRequirements();
        adapter.drawDisc(x, y, radius, options);
    }

    @Override
    public void drawEllipse(int rx, int ry, int options) {
        checkRequirements();
        adapter.drawEllipse(rx, ry, options);
    }

    @Override
    public void drawEllipse(int x, int y, int rx, int ry, int options) {
        checkRequirements();
        adapter.drawEllipse(x, y, rx, ry, options);
    }

    @Override
    public void drawFilledEllipse(int rx, int ry, int options) {
        checkRequirements();
        adapter.drawFilledEllipse(rx, ry, options);
    }

    @Override
    public void drawFilledEllipse(int x, int y, int rx, int ry, int options) {
        checkRequirements();
        adapter.drawFilledEllipse(x, y, rx, ry, options);
    }

    @Override
    public void drawFrame(int width, int height) {
        checkRequirements();
        adapter.drawFrame(width, height);
    }

    @Override
    public void drawFrame(int x, int y, int width, int height) {
        checkRequirements();
        adapter.drawFrame(x, y, width, height);
    }

    @Override
    public void drawGlyph(short encoding) {
        checkRequirements();
        adapter.drawGlyph(encoding);
    }

    @Override
    public void drawGlyph(int x, int y, short encoding) {
        checkRequirements();
        adapter.drawGlyph(x, y, encoding);
    }

    @Override
    public void drawHLine(int width) {
        checkRequirements();
        adapter.drawHLine(width);
    }

    @Override
    public void drawHLine(int x, int y, int width) {
        checkRequirements();
        adapter.drawHLine(x, y, width);
    }

    @Override
    public void drawVLine(int length) {
        checkRequirements();
        adapter.drawVLine(length);
    }

    @Override
    public void drawVLine(int x, int y, int length) {
        checkRequirements();
        adapter.drawVLine(x, y, length);
    }

    @Override
    public void drawLine(int x1, int y1) {
        checkRequirements();
        adapter.drawLine(x1, y1);
    }

    @Override
    public void drawLine(int x, int y, int x1, int y1) {
        checkRequirements();
        adapter.drawLine(x, y, x1, y1);
    }

    @Override
    public void drawPixel() {
        checkRequirements();
        adapter.drawPixel();
    }

    @Override
    public void drawPixel(int x, int y) {
        checkRequirements();
        adapter.drawPixel(x, y);
    }

    @Override
    public void drawRoundedBox(int width, int height, int radius) {
        checkRequirements();
        adapter.drawRoundedBox(width, height, radius);
    }

    @Override
    public void drawRoundedBox(int x, int y, int width, int height, int radius) {
        checkRequirements();
        adapter.drawRoundedBox(x, y, width, height, radius);
    }

    @Override
    public void drawRoundedFrame(int width, int height, int radius) {
        checkRequirements();
        adapter.drawRoundedFrame(width, height, radius);
    }

    @Override
    public void drawRoundedFrame(int x, int y, int width, int height, int radius) {
        checkRequirements();
        adapter.drawRoundedFrame(x, y, width, height, radius);
    }

    @Override
    public void drawString(String value) {
        checkRequirements();
        adapter.drawString(value);
    }

    @Override
    public void drawString(int x, int y, String value) {
        checkRequirements();
        adapter.drawString(x, y, value);
    }

    @Override
    public void drawTriangle(int x1, int y1, int x2, int y2) {
        checkRequirements();
        adapter.drawTriangle(x1, y1, x2, y2);
    }

    @Override
    public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2) {
        checkRequirements();
        adapter.drawTriangle(x0, y0, x1, y1, x2, y2);
    }

    @Override
    public void drawXBM(int width, int height, File file) {
        checkRequirements();
        adapter.drawXBM(width, height, file);
    }

    @Override
    public void drawXBM(int x, int y, int width, int height, File file) {
        checkRequirements();
        adapter.drawXBM(x, y, width, height, file);
    }

    @Override
    public void drawXBM(int width, int height, byte[] data) {
        checkRequirements();
        adapter.drawXBM(width, height, data);
    }

    @Override
    public void drawXBM(int x, int y, int width, int height, byte[] data) {
        checkRequirements();
        adapter.drawXBM(x, y, width, height, data);
    }

    @Override
    public int drawUTF8(String value) {
        checkRequirements();
        return adapter.drawUTF8(value);
    }

    @Override
    public int drawUTF8(int x, int y, String value) {
        checkRequirements();
        return adapter.drawUTF8(x, y, value);
    }

    @Override
    public int getUTF8Width(String text) {
        checkRequirements();
        return adapter.getUTF8Width(text);
    }

    @Override
    public void setFont(byte[] data) {
        checkRequirements();
        adapter.setFont(data);
    }

    @Override
    public void setFont(GlcdFont font) {
        checkRequirements();
        adapter.setFont(font);
    }

    @Override
    public void setFontMode(int mode) {
        checkRequirements();
        adapter.setFontMode(mode);
    }

    @Override
    public void setFontDirection(int direction) {
        checkRequirements();
        adapter.setFontDirection(direction);
    }

    @Override
    public void setFontPosBaseline() {
        checkRequirements();
        adapter.setFontPosBaseline();
    }

    @Override
    public void setFontPosBottom() {
        checkRequirements();
        adapter.setFontPosBottom();
    }

    @Override
    public void setFontPosTop() {
        checkRequirements();
        adapter.setFontPosTop();
    }

    @Override
    public void setFontPosCenter() {
        checkRequirements();
        adapter.setFontPosCenter();
    }

    @Override
    public void setFontRefHeightAll() {
        checkRequirements();
        adapter.setFontRefHeightAll();
    }

    @Override
    public void setFontRefHeightExtendedText() {
        checkRequirements();
        adapter.setFontRefHeightExtendedText();
    }

    @Override
    public void setFontRefHeightText() {
        checkRequirements();
        adapter.setFontRefHeightText();
    }

    @Override
    public void setFlipMode(boolean enable) {
        checkRequirements();
        adapter.setFlipMode(enable);
    }

    @Override
    public void setPowerSave(boolean enable) {
        checkRequirements();
        adapter.setPowerSave(enable);
    }

    @Override
    public void setDrawColor(int color) {
        checkRequirements();
        adapter.setDrawColor(color);
    }

    @Override
    public void initDisplay() {
        checkRequirements();
        adapter.initDisplay();
    }

    @Override
    public void firstPage() {
        checkRequirements();
        adapter.firstPage();
    }

    @Override
    public int nextPage() {
        checkRequirements();
        return adapter.nextPage();
    }

    @Override
    public int getAscent() {
        checkRequirements();
        return adapter.getAscent();
    }

    @Override
    public int getDescent() {
        checkRequirements();
        return adapter.getDescent();
    }

    @Override
    public int getMaxCharWidth() {
        checkRequirements();
        return adapter.getMaxCharWidth();
    }

    @Override
    public int getMaxCharHeight() {
        checkRequirements();
        return adapter.getMaxCharHeight();
    }

    @Override
    public void sendBuffer() {
        checkRequirements();
        adapter.sendBuffer();
    }

    @Override
    public void clearBuffer() {
        checkRequirements();
        adapter.clearBuffer();
    }

    @Override
    public void clearDisplay() {
        checkRequirements();
        adapter.clearDisplay();
    }

    @Override
    public void begin() {
        checkRequirements();
        adapter.begin();
    }

    @Override
    public int getHeight() {
        checkRequirements();
        return adapter.getHeight();
    }

    @Override
    public int getWidth() {
        checkRequirements();
        return adapter.getWidth();
    }

    @Override
    public void clear() {
        checkRequirements();
        adapter.clear();
    }

    @Override
    public void setCursor(int x, int y) {
        adapter.setCursor(x, y);
    }

    @Override
    public int setAutoPageClear(int mode) {
        checkRequirements();
        return adapter.setAutoPageClear(mode);
    }

    //TODO: Replace int parameter with an enumeration type
    @Override
    public void setBitmapMode(int mode) {
        checkRequirements();
        adapter.setBitmapMode(mode);
    }

    @Override
    public void setContrast(int value) {
        checkRequirements();
        adapter.setContrast(value);
    }

    @Override
    public void setDisplayRotation(GlcdRotation rotation) {
        checkRequirements();
        adapter.setDisplayRotation(rotation);
    }

    @Override
    public void setDisplayRotation(int rotation) {
        checkRequirements();
        adapter.setDisplayRotation(rotation);
    }

    @Override
    public byte[] getBuffer() {
        checkRequirements();
        return adapter.getBuffer();
    }

    @Override
    public int getBufferTileWidth() {
        checkRequirements();
        return adapter.getBufferTileWidth();
    }

    @Override
    public int getBufferTileHeight() {
        checkRequirements();
        return adapter.getBufferTileHeight();
    }

    @Override
    public int getBufferCurrTileRow() {
        checkRequirements();
        return adapter.getBufferCurrTileRow();
    }

    @Override
    public void setBufferCurrTileRow(int row) {
        checkRequirements();
        adapter.setBufferCurrTileRow(row);
    }

    @Override
    public int getStrWidth(String text) {
        checkRequirements();
        return adapter.getStrWidth(text);
    }

    @Override
    public void setClipWindow(int x0, int y0, int x1, int y1) {
        checkRequirements();
        adapter.setClipWindow(x0, y0, x1, y1);
    }

    @Override
    public void setMaxClipWindow() {
        checkRequirements();
        adapter.setMaxClipWindow();
    }

    @Override
    public final long getId() {
        return adapter.getId();
    }

    @Override
    public void write(byte... data) {
        throw new NotImplementedException("Write not implemented for this driver");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlcdBaseDriver that = (GlcdBaseDriver) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
