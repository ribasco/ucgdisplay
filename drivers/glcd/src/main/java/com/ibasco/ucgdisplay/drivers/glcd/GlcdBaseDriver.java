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
import com.ibasco.ucgdisplay.core.u8g2.U8g2Graphics;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdFont;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdRotation;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdConfigException;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdDriverException;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdNotInitializedException;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.XBMDecodeException;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMData;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.ObjectUtils;
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
abstract public class GlcdBaseDriver implements U8g2DisplayDriver {

    private static final Logger log = LoggerFactory.getLogger(GlcdBaseDriver.class);

    private long _id;

    private int x = 0, y = 0;

    private static final GlcdRotation DEFAULT_ROTATION = GlcdRotation.ROTATION_90;

    private GlcdConfig config;

    private GlcdDriverEventHandler driverEventHandler;

    private boolean initialized = false;

    private boolean virtual;

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
     *         the native display driver. If a null value is provided, the internal event handler of this driver
     *         instance will be used instead.
     */
    protected GlcdBaseDriver(GlcdConfig config, boolean virtual, GlcdDriverEventHandler handler) {
        this.config = config;
        this.virtual = virtual;
        this.driverEventHandler = handler == null ? createDefaultEventHandler() : handler;
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

        _id = initNativeDriver(config);

        if (_id == -1)
            throw new GlcdDriverException("Could not initialize U8G2 Display Driver");

        if (virtual && driverEventHandler != null) {
            //check if a virtual event handler is present
            U8g2EventDispatcher.addByteListener(this, driverEventHandler);
            U8g2EventDispatcher.addGpioListener(this, driverEventHandler);
        }
        initialized = true;
        log.debug("GLCD driver initialized (Address: {})", _id);
    }

    protected long initNativeDriver(GlcdConfig config) {
        String setupProcedure = config.getSetupProcedure();
        int rotation = config.getRotation().getValue();
        int commInt = config.getBusInterface().getValue();
        int commType = config.getBusInterface().getBusType().getValue();
        int address = config.getDeviceAddress();
        byte[] pinConfig = ObjectUtils.defaultIfNull(config.getPinMap(), new GlcdPinMapConfig()).toByteArray();
        return U8g2Graphics.setup(setupProcedure, commInt, commType, rotation, address, pinConfig, virtual);
    }

    @SuppressWarnings({"unchecked", "unused"})
    public final <T extends GlcdDriverEventHandler> T getDriverEventHandler() {
        return (T) driverEventHandler;
    }

    @SuppressWarnings("unused")
    protected final void setDriverEventHandler(GlcdDriverEventHandler driverEventHandler) {
        this.driverEventHandler = driverEventHandler;
    }

    @Override
    public final long getId() {
        return _id;
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
        if (_id < 0)
            throw new GlcdDriverException("Invalid driver instance ID. Please verify that the driver initialization succeeded");
        if (!initialized)
            throw new GlcdNotInitializedException("Driver has not yet been initialized. Please remember to call initialize() in your implementing class");
    }

    @Override
    public void drawBox(int width, int height) {
        checkRequirements();
        drawBox(x, y, width, height);
    }

    @Override
    public void drawBox(int x, int y, int width, int height) {
        checkRequirements();
        U8g2Graphics.drawBox(_id, x, y, width, height);
    }

    @Override
    @Deprecated
    public void drawBitmap(int count, int height, byte[] bitmap) {
        checkRequirements();
        drawBitmap(x, y, count, height, bitmap);
    }

    @Override
    @Deprecated
    public void drawBitmap(int x, int y, int count, int height, byte[] bitmap) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawBitmap(_id, x, y, count, height, bitmap);
    }

    @Override
    public void drawCircle(int radius, int options) {
        checkRequirements();
        drawCircle(x, y, radius, options);
    }

    @Override
    public void drawCircle(int x, int y, int radius, int options) {
        checkRequirements();
        U8g2Graphics.drawCircle(_id, x, y, radius, options);
    }

    @Override
    public void drawDisc(int radius, int options) {
        drawDisc(x, y, radius, options);
    }

    @Override
    public void drawDisc(int x, int y, int radius, int options) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawDisc(_id, x, y, radius, options);
    }

    @Override
    public void drawEllipse(int rx, int ry, int options) {
        drawEllipse(x, y, rx, ry, options);
    }

    @Override
    public void drawEllipse(int x, int y, int rx, int ry, int options) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawEllipse(_id, x, y, rx, ry, options);
    }

    @Override
    public void drawFilledEllipse(int rx, int ry, int options) {
        drawFilledEllipse(x, y, rx, ry, options);
    }

    @Override
    public void drawFilledEllipse(int x, int y, int rx, int ry, int options) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawFilledEllipse(_id, x, y, rx, ry, options);
    }

    @Override
    public void drawFrame(int width, int height) {
        drawFrame(x, y, width, height);
    }

    @Override
    public void drawFrame(int x, int y, int width, int height) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawFrame(_id, x, y, width, height);
    }

    @Override
    public void drawGlyph(short encoding) {
        drawGlyph(x, y, encoding);
    }

    @Override
    public void drawGlyph(int x, int y, short encoding) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawGlyph(_id, x, y, encoding);
    }

    @Override
    public void drawHLine(int width) {
        drawHLine(x, y, width);
    }

    @Override
    public void drawHLine(int x, int y, int width) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawHLine(_id, x, y, width);
    }

    @Override
    public void drawVLine(int length) {
        drawVLine(x, y, length);
    }

    @Override
    public void drawVLine(int x, int y, int length) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawVLine(_id, x, y, length);
    }

    @Override
    public void drawLine(int x1, int y1) {
        drawLine(x, y, x1, y1);
    }

    @Override
    public void drawLine(int x, int y, int x1, int y1) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawLine(_id, x, y, x1, y1);
    }

    @Override
    public void drawPixel() {
        drawPixel(x, y);
    }

    @Override
    public void drawPixel(int x, int y) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawPixel(_id, x, y);
    }

    @Override
    public void drawRoundedBox(int width, int height, int radius) {
        drawRoundedBox(x, y, width, height, radius);
    }

    @Override
    public void drawRoundedBox(int x, int y, int width, int height, int radius) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawRoundedBox(_id, x, y, width, height, radius);
    }

    @Override
    public void drawRoundedFrame(int width, int height, int radius) {
        drawRoundedFrame(x, y, width, height, radius);
    }

    @Override
    public void drawRoundedFrame(int x, int y, int width, int height, int radius) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawRoundedFrame(_id, x, y, width, height, radius);
    }

    @Override
    public void drawString(String value) {
        drawString(x, y, value);
    }

    @Override
    public void drawString(int x, int y, String value) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawString(_id, x, y, value);
    }

    @Override
    public void drawTriangle(int x1, int y1, int x2, int y2) {
        drawTriangle(x, y, x1, y1, x2, y2);
    }

    @Override
    public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2) {
        checkRequirements();
        setCursor(x0, y0);
        U8g2Graphics.drawTriangle(_id, x0, y0, x1, y1, x2, y2);
    }

    @Override
    public void drawXBM(int width, int height, File file) {
        drawXBM(x, y, width, height, file);
    }

    @Override
    public void drawXBM(int x, int y, int width, int height, File file) {
        checkRequirements();
        setCursor(x, y);
        try {
            XBMData xbmData = XBMUtils.decodeXbmFile(file);
            assert xbmData != null;
            drawXBM(x, y, width, height, xbmData.getData());
        } catch (XBMDecodeException e) {
            log.error("Could not draw the specified XBM file", e);
        }
    }

    @Override
    public void drawXBM(int width, int height, byte[] data) {
        drawXBM(x, y, width, height, data);
    }

    @Override
    public void drawXBM(int x, int y, int width, int height, byte[] data) {
        checkRequirements();
        setCursor(x, y);
        U8g2Graphics.drawXBM(_id, x, y, width, height, data);
    }

    @Override
    public int drawUTF8(String value) {
        return drawUTF8(x, y, value);
    }

    @Override
    public int drawUTF8(int x, int y, String value) {
        checkRequirements();
        setCursor(x, y);
        return U8g2Graphics.drawUTF8(_id, x, y, value);
    }

    @Override
    public int getUTF8Width(String text) {
        checkRequirements();
        return U8g2Graphics.getUTF8Width(_id, text);
    }

    @Override
    public void setFont(byte[] data) {
        checkRequirements();
        U8g2Graphics.setFont(_id, data);
    }

    @Override
    public void setFont(GlcdFont font) {
        checkRequirements();
        U8g2Graphics.setFont(_id, font.getKey());
    }

    @Override
    public void setFontMode(int mode) {
        checkRequirements();
        U8g2Graphics.setFontMode(_id, mode);
    }

    @Override
    public void setFontDirection(int direction) {
        checkRequirements();
        U8g2Graphics.setFontDirection(_id, direction);
    }

    @Override
    public void setFontPosBaseline() {
        checkRequirements();
        U8g2Graphics.setFontPosBaseline(_id);
    }

    @Override
    public void setFontPosBottom() {
        checkRequirements();
        U8g2Graphics.setFontPosBottom(_id);
    }

    @Override
    public void setFontPosTop() {
        checkRequirements();
        U8g2Graphics.setFontPosTop(_id);
    }

    @Override
    public void setFontPosCenter() {
        checkRequirements();
        U8g2Graphics.setFontPosCenter(_id);
    }

    @Override
    public void setFontRefHeightAll() {
        checkRequirements();
        U8g2Graphics.setFontRefHeightAll(_id);
    }

    @Override
    public void setFontRefHeightExtendedText() {
        checkRequirements();
        U8g2Graphics.setFontRefHeightExtendedText(_id);
    }

    @Override
    public void setFontRefHeightText() {
        checkRequirements();
        U8g2Graphics.setFontRefHeightText(_id);
    }

    @Override
    public void setFlipMode(boolean enable) {
        checkRequirements();
        U8g2Graphics.setFlipMode(_id, enable);
    }

    @Override
    public void setPowerSave(boolean enable) {
        checkRequirements();
        U8g2Graphics.setPowerSave(_id, enable);
    }

    @Override
    public void setDrawColor(int color) {
        checkRequirements();
        U8g2Graphics.setDrawColor(_id, color);
    }

    @Override
    public void initDisplay() {
        checkRequirements();
        U8g2Graphics.initDisplay(_id);
    }

    @Override
    public void firstPage() {
        checkRequirements();
        U8g2Graphics.firstPage(_id);
    }

    @Override
    public int nextPage() {
        checkRequirements();
        return U8g2Graphics.nextPage(_id);
    }

    @Override
    public int getAscent() {
        checkRequirements();
        return U8g2Graphics.getAscent(_id);
    }

    @Override
    public int getDescent() {
        checkRequirements();
        return U8g2Graphics.getDescent(_id);
    }

    @Override
    public int getMaxCharWidth() {
        checkRequirements();
        return U8g2Graphics.getMaxCharWidth(_id);
    }

    @Override
    public int getMaxCharHeight() {
        checkRequirements();
        return U8g2Graphics.getMaxCharHeight(_id);
    }

    @Override
    public void sendBuffer() {
        checkRequirements();
        U8g2Graphics.sendBuffer(_id);
    }

    @Override
    public void clearBuffer() {
        checkRequirements();
        U8g2Graphics.clearBuffer(_id);
    }

    @Override
    public void clearDisplay() {
        checkRequirements();
        U8g2Graphics.clearDisplay(_id);
    }

    @Override
    public void begin() {
        checkRequirements();
        U8g2Graphics.begin(_id);
    }

    @Override
    public int getHeight() {
        checkRequirements();
        return U8g2Graphics.getHeight(_id);
    }

    @Override
    public int getWidth() {
        checkRequirements();
        return U8g2Graphics.getWidth(_id);
    }

    @Override
    public void clear() {
        checkRequirements();
        U8g2Graphics.clear(_id);
    }

    @Override
    public void setCursor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int setAutoPageClear(int mode) {
        checkRequirements();
        return U8g2Graphics.setAutoPageClear(_id, mode);
    }

    //TODO: Replace int parameter with an enumeration type
    @Override
    public void setBitmapMode(int mode) {
        checkRequirements();
        U8g2Graphics.setBitmapMode(_id, mode);
    }

    @Override
    public void setContrast(int value) {
        checkRequirements();
        U8g2Graphics.setContrast(_id, value);
    }

    @Override
    public void setDisplayRotation(GlcdRotation rotation) {
        checkRequirements();
        setDisplayRotation(rotation.getValue());
    }

    @Override
    public void setDisplayRotation(int rotation) {
        checkRequirements();
        U8g2Graphics.setDisplayRotation(_id, rotation);
    }

    @Override
    public byte[] getBuffer() {
        checkRequirements();
        return U8g2Graphics.getBuffer(_id);
    }

    @Override
    public int getBufferTileWidth() {
        checkRequirements();
        return U8g2Graphics.getBufferTileWidth(_id);
    }

    @Override
    public int getBufferTileHeight() {
        checkRequirements();
        return U8g2Graphics.getBufferTileHeight(_id);
    }

    @Override
    public int getBufferCurrTileRow() {
        checkRequirements();
        return U8g2Graphics.getBufferCurrTileRow(_id);
    }

    @Override
    public void setBufferCurrTileRow(int row) {
        checkRequirements();
        U8g2Graphics.setBufferCurrTileRow(_id, row);
    }

    @Override
    public int getStrWidth(String text) {
        checkRequirements();
        return U8g2Graphics.getStrWidth(_id, text);
    }

    @Override
    public void setClipWindow(int x0, int y0, int x1, int y1) {
        checkRequirements();
        U8g2Graphics.setClipWindow(_id, x0, y0, x1, y1);
    }

    @Override
    public void setMaxClipWindow() {
        checkRequirements();
        U8g2Graphics.setMaxClipWindow(_id);
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
        return _id == that._id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    protected void finalize() throws Throwable {
        U8g2EventDispatcher.removeByteListener(this);
        U8g2EventDispatcher.removeGpioListener(this);
        super.finalize();
    }
}
