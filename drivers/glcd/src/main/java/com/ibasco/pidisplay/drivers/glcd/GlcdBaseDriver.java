package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.core.drivers.GraphicsDisplayDriver;
import com.ibasco.pidisplay.core.exceptions.NotImplementedException;
import com.ibasco.pidisplay.core.exceptions.XBMDecodeException;
import com.ibasco.pidisplay.core.u8g2.*;
import com.ibasco.pidisplay.core.ui.Font;
import com.ibasco.pidisplay.core.ui.Rotation;
import com.ibasco.pidisplay.core.util.XBMUtils;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdBusInterface;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdRotation;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdConfigException;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdDriverException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Base Driver implementation
 *
 * @author Rafael Ibasco
 */
abstract public class GlcdBaseDriver implements GraphicsDisplayDriver {

    private static final Logger log = LoggerFactory.getLogger(GlcdBaseDriver.class);

    private long _id;

    private int x = 0, y = 0;

    private static final GlcdRotation DEFAULT_ROTATION = GlcdRotation.ROTATION_90;

    private GlcdConfig config;

    private final U8g2GpioEventListener gpioEventListener = this::onGpioEvent;

    private final U8g2ByteEventListener byteEventListener = this::onByteEvent;

    private boolean initialized = false;

    /**
     * Create new instance based on the config provided
     *
     * @param config
     *         The {@link GlcdConfig} associated with this instance
     *
     * @throws GlcdDriverException
     *         Thrown when setup fails
     */
    public GlcdBaseDriver(GlcdConfig config) {
        try {
            //Make sure we have a valid configuration
            checkConfig(config);
            this.config = config;
            log.debug("GLCD driver initialized (Address: {})", _id);
        } catch (GlcdDriverException e) {
            throw new RuntimeException("Error occured during glcd driver initialization", e);
        }
    }

    /**
     * <p>Driver initialization procedures. Sub-classes MUST call this method during/after initialization.
     * Calling the drawing operations while not initialized will trigger {@link GlcdDriverException}.</p>
     *
     * @throws GlcdDriverException
     *         If a driver related exception occurs (e.g. invalid configuration setup)
     */
    protected final void initialize() throws GlcdDriverException {
        //Get rotation setting
        String setupProcedure = config.getSetupProcedure();
        int rotation = config.getRotation().getValue();
        int commInt = config.getBusInterface().getValue();
        int commType = config.getBusInterface().getBusType().getValue();
        int address = config.getDeviceAddress();
        byte[] pinConfig = ObjectUtils.defaultIfNull(config.getPinMap(), new GlcdPinMapConfig()).toByteArray();
        _id = U8g2Graphics.setup(setupProcedure, commInt, commType, rotation, address, pinConfig, config.isEmulated());

        if (_id == -1)
            throw new GlcdDriverException("Could not initialize U8G2 Display Driver");

        //check if emulation flag is set
        if (config.isEmulated()) {
            U8g2EventDispatcher.addByteListener(this, byteEventListener);
            U8g2EventDispatcher.addGpioListener(this, gpioEventListener);
        }

        initialized = true;
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

        //Check protocol if supported
        if (!config.getDisplay().hasCommType(config.getBusInterface())) {
            String protocols = config.getDisplay().getCommTypes().stream().map(Object::toString).collect(Collectors.joining(", "));
            throw new GlcdConfigException(
                    String.format("The selected communication interface '%s' is not supported by your display controller '%s :: %s' (Supported Interfaces: %s)", config.getBusInterface().name(), config.getDisplay().getController().name(), config.getDisplay().getName(), protocols),
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
        if (!config.isEmulated() && (config.getPinMap() == null || config.getPinMap().isEmpty())) {
            throw new GlcdConfigException("Missing pin map configuration", config);
        }

        //Check if rotation was specified, otherwise assign default
        if (config.getRotation() == null) {
            log.warn("No rotation specified. Using default = {}", DEFAULT_ROTATION);
            config.setRotation(DEFAULT_ROTATION);
        }
    }

    private void checkRequirements() {
        if (_id == -1)
            throw new RuntimeException(new GlcdDriverException("Invalid driver instance ID. Please verify that the driver initialization succeeded"));
        if (!initialized)
            throw new RuntimeException(new GlcdDriverException("Driver has not yet been initialized. Please remember to call initialize() in your implementing class"));
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
    public void drawDisc(int x, int y, int radius, int options) {
        checkRequirements();
        U8g2Graphics.drawDisc(_id, x, y, radius, options);
    }

    @Override
    public void drawEllipse(int x, int y, int rx, int ry, int options) {
        checkRequirements();
        U8g2Graphics.drawEllipse(_id, x, y, rx, ry, options);
    }

    @Override
    public void drawFilledEllipse(int x, int y, int rx, int ry, int options) {
        checkRequirements();
        U8g2Graphics.drawFilledEllipse(_id, x, y, rx, ry, options);
    }

    @Override
    public void drawFrame(int x, int y, int width, int height) {
        checkRequirements();
        U8g2Graphics.drawFrame(_id, x, y, width, height);
    }

    @Override
    public void drawGlyph(int x, int y, short encoding) {
        checkRequirements();
        U8g2Graphics.drawGlyph(_id, x, y, encoding);
    }

    @Override
    public void drawHLine(int x, int y, int width) {
        checkRequirements();
        U8g2Graphics.drawHLine(_id, x, y, width);
    }

    @Override
    public void drawVLine(int x, int y, int length) {
        checkRequirements();
        U8g2Graphics.drawVLine(_id, x, y, length);
    }

    @Override
    public void drawLine(int x, int y, int x1, int y1) {
        checkRequirements();
        U8g2Graphics.drawLine(_id, x, y, x1, y1);
    }

    @Override
    public void drawPixel(int x, int y) {
        checkRequirements();
        U8g2Graphics.drawPixel(_id, x, y);
    }

    @Override
    public void drawRoundedBox(int x, int y, int width, int height, int radius) {
        checkRequirements();
        U8g2Graphics.drawRoundedBox(_id, x, y, width, height, radius);
    }

    @Override
    public void drawRoundedFrame(int x, int y, int width, int height, int radius) {
        checkRequirements();
        U8g2Graphics.drawRoundedFrame(_id, x, y, width, height, radius);
    }

    @Override
    public void drawString(int x, int y, String value) {
        checkRequirements();
        U8g2Graphics.drawString(_id, x, y, value);
    }

    @Override
    public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2) {
        checkRequirements();
        U8g2Graphics.drawTriangle(_id, x0, y0, x1, y1, x2, y2);
    }

    public void drawXBM(int x, int y, int width, int height, File data) {
        checkRequirements();
        try {
            XBMUtils.XBMData xbmData = XBMUtils.decodeXbmFile(data);
            assert xbmData != null;
            drawXBM(x, y, width, height, xbmData.getData());
        } catch (XBMDecodeException e) {
            log.error("Could not draw the specified XBM file", e);
        }
    }

    @Override
    public void drawXBM(int x, int y, int width, int height, byte[] data) {
        checkRequirements();
        U8g2Graphics.drawXBM(_id, x, y, width, height, data);
    }

    @Override
    public int drawUTF8(int x, int y, String value) {
        checkRequirements();
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
    public void setFont(Font font) {
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

    //TODO: Replace int parameter with a color type enumeration
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
    public void setDisplayRotation(Rotation rotation) {
        checkRequirements();
        setDisplayRotation(rotation.getValue());
    }

    @Override
    public void setDisplayRotation(int rotation) {
        checkRequirements();
        U8g2Graphics.setDisplayRotation(_id, rotation);
    }

    @Override
    public int getBuffer() {
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
    public void write(byte... data) {
        throw new NotImplementedException();
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
