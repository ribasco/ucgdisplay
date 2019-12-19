/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2019 Universal Character/Graphics display library
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

import com.ibasco.ucgdisplay.core.u8g2.U8g2EventDispatcher;
import com.ibasco.ucgdisplay.core.u8g2.U8g2Graphics;
import com.ibasco.ucgdisplay.drivers.glcd.enums.*;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.GlcdDriverException;
import com.ibasco.ucgdisplay.drivers.glcd.exceptions.XBMDecodeException;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMData;
import com.ibasco.ucgdisplay.drivers.glcd.utils.XBMUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Driver adapter using JNI and U8g2. Please note that this class is NOT thread-safe.
 *
 * @author Rafael Ibasco
 */
public class U8g2DriverAdapter implements GlcdDriverAdapter {

    private long _id;

    private int x = 0, y = 0;

    private void checkRequirements() {
        if (_id < 0)
            throw new GlcdDriverException("Invalid driver ID. Please verify that the driver initialization succeeded");
    }

    @Override
    public void initialize(GlcdConfig config, boolean virtual) {
        String setupProcedure = config.getSetupProcedure();
        int commInt = config.getBusInterface().getValue();
        int commType = config.getBusInterface().getBusType().getValue();
        int[] pinConfig = ObjectUtils.defaultIfNull(config.getPinMap(), new GlcdPinMapConfig()).toIntArray();
        int rotation = GlcdRotation.ROTATION_NONE.toValue();
        if (config.getOption(GlcdOption.ROTATION) != null) {
            rotation = config.getOption(GlcdOption.ROTATION);
        }
        _id = U8g2Graphics.setup(setupProcedure, commInt, commType, rotation, pinConfig, config.getOptions(), virtual);
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
        if (value == null) {
            value = "";
        }
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
            throw new GlcdDriverException(e);
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
    public void setFontMode(GlcdFontMode mode) {
        checkRequirements();
        U8g2Graphics.setFontMode(_id, mode.getValue());
    }

    @Override
    public void setFontDirection(GlcdFontDirection direction) {
        checkRequirements();
        U8g2Graphics.setFontDirection(_id, direction.getValue());
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
    public void setDrawColor(GlcdDrawColor color) {
        checkRequirements();
        U8g2Graphics.setDrawColor(_id, color.getValue());
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
    public int setAutoPageClear(boolean clear) {
        checkRequirements();
        return U8g2Graphics.setAutoPageClear(_id, clear ? 1 : 0);
    }

    @Override
    public void setBitmapMode(GlcdBitmapMode mode) {
        checkRequirements();
        U8g2Graphics.setBitmapMode(_id, mode.getValue());
    }

    @Override
    public void setContrast(int value) {
        checkRequirements();
        U8g2Graphics.setContrast(_id, value);
    }

    @Override
    public void setDisplayRotation(GlcdRotation rotation) {
        checkRequirements();
        setDisplayRotation(rotation.toValue());
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
    public long getId() {
        return _id;
    }
}
