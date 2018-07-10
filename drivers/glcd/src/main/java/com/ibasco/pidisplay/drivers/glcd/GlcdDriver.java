package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.core.drivers.GraphicsDisplayDriver;
import com.ibasco.pidisplay.core.exceptions.NotYetImplementedException;
import com.ibasco.pidisplay.core.exceptions.XBMDecodeException;
import com.ibasco.pidisplay.core.ui.Font;
import com.ibasco.pidisplay.core.ui.U8g2Interface;
import com.ibasco.pidisplay.core.util.XBMUtils;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdRotation;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdConfigException;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class GlcdDriver implements GraphicsDisplayDriver {
    private static final Logger log = LoggerFactory.getLogger(GlcdDriver.class);

    private long _id;
    private int x = 0, y = 0;
    private static final GlcdRotation DEFAULT_ROTATION = GlcdRotation.ROTATION_90;

    public GlcdDriver(GlcdConfig config) throws GlcdDriverException {
        //Make sure we have a valid configuration
        checkConfig(config);

        //Get rotation setting
        String setupProcedure = config.getSetupProcedure();
        int rotation = config.getRotation().getValue();
        int commInt = config.getCommInterface().getValue();
        int commType = config.getCommInterface().getType().getValue();
        int address = config.getDeviceAddress();
        byte[] pinConfig = config.getPinMap().build();

        _id = U8g2Interface.setup(setupProcedure, commInt, commType, rotation, address, pinConfig);

        if (_id == -1)
            throw new GlcdDriverException("Could not initialize U8G2 Display Driver");

        log.info("Got Address: {}", _id);
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
    private void checkConfig(GlcdConfig config) throws GlcdConfigException {
        //Check protocol if supported
        if (!config.getDisplay().hasCommType(config.getCommInterface())) {
            String protocols = config.getDisplay().getCommTypes().stream().map(Object::toString).collect(Collectors.joining(", "));
            throw new GlcdConfigException(
                    String.format("The selected protocol '%s' is not supported by display '%s' (Supported Protocols: %s)", config.getCommInterface().name(), config.getDisplay().getName(), protocols),
                    config
            );
        }

        //If I2C is selected, make sure device address is not empty
        //Check display
        if (config.getDisplay() == null)
            throw new GlcdConfigException("No display specified", config);

        //Check pin mapping
        if (config.getPinMap() == null || config.getPinMap().isEmpty()) {
            throw new GlcdConfigException("No pins have been mapped", config);
        }

        //Check if rotation was specified, otherwise assign default
        if (config.getRotation() == null) {
            log.warn("No rotation specified. Using default = {}", DEFAULT_ROTATION);
            config.setRotation(DEFAULT_ROTATION);
        }
    }

    @Override
    public void drawBox(int width, int height) {
        drawBox(x, y, width, height);
    }

    @Override
    public void drawBox(int x, int y, int width, int height) {
        U8g2Interface.drawBox(_id, x, y, width, height);
    }

    @Override
    @Deprecated
    public void drawBitmap(int count, int height, byte[] bitmap) {
        drawBitmap(x, y, count, height, bitmap);
    }

    @Override
    @Deprecated
    public void drawBitmap(int x, int y, int count, int height, byte[] bitmap) {
        U8g2Interface.drawBitmap(_id, x, y, count, height, bitmap);
    }

    @Override
    public void drawCircle(int radius, int options) {
        drawCircle(x, y, radius, options);
    }

    @Override
    public void drawCircle(int x, int y, int radius, int options) {
        U8g2Interface.drawCircle(_id, x, y, radius, options);
    }

    @Override
    public void drawDisc(int x, int y, int radius, int options) {
        U8g2Interface.drawDisc(_id, x, y, radius, options);
    }

    @Override
    public void drawEllipse(int x, int y, int rx, int ry, int options) {
        U8g2Interface.drawEllipse(_id, x, y, rx, ry, options);
    }

    @Override
    public void drawFilledEllipse(int x, int y, int rx, int ry, int options) {
        U8g2Interface.drawFilledEllipse(_id, x, y, rx, ry, options);
    }

    @Override
    public void drawFrame(int x, int y, int width, int height) {
        U8g2Interface.drawFrame(_id, x, y, width, height);
    }

    @Override
    public void drawGlyph(int x, int y, short encoding) {
        U8g2Interface.drawGlyph(_id, x, y, encoding);
    }

    @Override
    public void drawHLine(int x, int y, int width) {
        U8g2Interface.drawHLine(_id, x, y, width);
    }

    @Override
    public void drawLine(int x, int y, int x1, int y1) {
        U8g2Interface.drawLine(_id, x, y, x1, y1);
    }

    @Override
    public void drawPixel(int x, int y) {
        U8g2Interface.drawPixel(_id, x, y);
    }

    @Override
    public void drawRoundedBox(int x, int y, int width, int height, int radius) {
        U8g2Interface.drawRoundedBox(_id, x, y, width, height, radius);
    }

    @Override
    public void drawRoundedFrame(int x, int y, int width, int height, int radius) {
        U8g2Interface.drawRoundedFrame(_id, x, y, width, height, radius);
    }

    @Override
    public void drawString(int x, int y, String value) {
        U8g2Interface.drawString(_id, x, y, value);
    }

    @Override
    public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2) {
        U8g2Interface.drawTriangle(_id, x0, y0, x1, y1, x2, y2);
    }

    public void drawXBM(int x, int y, int width, int height, File data) {
        try {
            byte[] xbmData = XBMUtils.decodeXbmFile(data);
            drawXBM(x, y, width, height, xbmData);
        } catch (XBMDecodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawXBM(int x, int y, int width, int height, byte[] data) {
        U8g2Interface.drawXBM(_id, x, y, width, height, data);
    }

    @Override
    public int drawUTF8(int x, int y, String value) {
        return U8g2Interface.drawUTF8(_id, x, y, value);
    }

    @Override
    public int getUTF8Width(String text) {
        return U8g2Interface.getUTF8Width(_id, text);
    }

    @Override
    public void setFont(byte[] data) {
        U8g2Interface.setFont(_id, data);
    }

    @Override
    public void setFont(Font font) {
        U8g2Interface.setFont(_id, font.getKey());
    }

    @Override
    public void setFontMode(int mode) {
        U8g2Interface.setFontMode(_id, mode);
    }

    @Override
    public void setFontDirection(int direction) {
        U8g2Interface.setFontDirection(_id, direction);
    }

    @Override
    public void setFontPosBaseline() {
        U8g2Interface.setFontPosBaseline(_id);
    }

    @Override
    public void setFontPosBottom() {
        U8g2Interface.setFontPosBottom(_id);
    }

    @Override
    public void setFontPosTop() {
        U8g2Interface.setFontPosTop(_id);
    }

    @Override
    public void setFontPosCenter() {
        U8g2Interface.setFontPosCenter(_id);
    }

    @Override
    public void setFontRefHeightAll() {
        U8g2Interface.setFontRefHeightAll(_id);
    }

    @Override
    public void setFontRefHeightExtendedText() {
        U8g2Interface.setFontRefHeightExtendedText(_id);
    }

    @Override
    public void setFontRefHeightText() {
        U8g2Interface.setFontRefHeightText(_id);
    }

    @Override
    public void setFlipMode(boolean enable) {
        U8g2Interface.setFlipMode(_id, enable);
    }

    @Override
    public void setPowerSave(boolean enable) {
        U8g2Interface.setPowerSave(_id, enable);
    }

    @Override
    public void setDrawColor(int color) {
        U8g2Interface.setDrawColor(_id, color);
    }

    @Override
    public void initDisplay() {
        U8g2Interface.initDisplay(_id);
    }

    @Override
    public void firstPage() {
        U8g2Interface.firstPage(_id);
    }

    @Override
    public int nextPage() {
        return U8g2Interface.nextPage(_id);
    }

    @Override
    public int getAscent() {
        return U8g2Interface.getAscent(_id);
    }

    @Override
    public int getDescent() {
        return U8g2Interface.getDescent(_id);
    }

    @Override
    public int getMaxCharWidth() {
        return U8g2Interface.getMaxCharWidth(_id);
    }

    @Override
    public int getMaxCharHeight() {
        return U8g2Interface.getMaxCharHeight(_id);
    }

    @Override
    public void sendBuffer() {
        U8g2Interface.sendBuffer(_id);
    }

    @Override
    public void clearBuffer() {
        U8g2Interface.clearBuffer(_id);
    }

    @Override
    public void clearDisplay() {
        U8g2Interface.clearDisplay(_id);
    }

    @Override
    public void begin() {
        U8g2Interface.begin(_id);
    }

    @Override
    public int getHeight() {
        return U8g2Interface.getHeight(_id);
    }

    @Override
    public int getWidth() {
        return U8g2Interface.getWidth(_id);
    }

    @Override
    public void clear() {
        U8g2Interface.clear(_id);
    }

    @Override
    public void setCursor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int setAutoPageClear(int mode) {
        return U8g2Interface.setAutoPageClear(_id, mode);
    }

    @Override
    public void setBitmapMode(int mode) {
        U8g2Interface.setBitmapMode(_id, mode);
    }

    @Override
    public void setContrast(int value) {
        U8g2Interface.setContrast(_id, value);
    }

    public void setDisplayRotation(GlcdRotation rotation) {
        setDisplayRotation(rotation.getValue());
    }

    @Override
    public void setDisplayRotation(int rotation) {
        U8g2Interface.setDisplayRotation(_id, rotation);
    }

    @Override
    public int getBuffer() {
        return U8g2Interface.getBuffer(_id);
    }

    @Override
    public int getBufferTileWidth() {
        return U8g2Interface.getBufferTileWidth(_id);
    }

    @Override
    public int getBufferTileHeight() {
        return U8g2Interface.getBufferTileHeight(_id);
    }

    @Override
    @Deprecated
    public int getPageCurrTileRow() {
        return U8g2Interface.getPageCurrTileRow(_id);
    }

    @Override
    @Deprecated
    public void setPageCurrTileRow(int row) {
        U8g2Interface.setPageCurrTileRow(_id, row);
    }

    @Override
    public int getBufferCurrTileRow() {
        return U8g2Interface.getBufferCurrTileRow(_id);
    }

    @Override
    public void setBufferCurrTileRow(int row) {
        U8g2Interface.setBufferCurrTileRow(_id, row);
    }

    @Override
    public void write(byte... data) {
        throw new NotYetImplementedException();
    }
}
