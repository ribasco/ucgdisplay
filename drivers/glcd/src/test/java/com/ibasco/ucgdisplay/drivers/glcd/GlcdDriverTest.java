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

import com.ibasco.ucgdisplay.core.u8g2.U8g2Graphics;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdCommProtocol;
import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdRotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlcdDriverTest {

    private GlcdConfig config;

    @Mock
    private GlcdDriverEventHandler mockEventHandler;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private GlcdDriverAdapter mockDriverAdapter;

    @Captor
    ArgumentCaptor<Integer> widthCaptor;

    @Captor
    ArgumentCaptor<Integer> heightCaptor;

    @Captor
    ArgumentCaptor<Integer> xCaptor;

    @Captor
    ArgumentCaptor<Integer> yCaptor;

    @Captor
    ArgumentCaptor<byte[]> byteArrayCaptor;

    @Captor
    ArgumentCaptor<Integer> optionsCaptor;

    @Captor
    ArgumentCaptor<Integer> radiusCaptor;

    @BeforeEach
    void setUp() {
        config = new GlcdConfig();
    }

    @Test
    void initialize() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertTrue(driver.isInitialized());
        verify(mockDriverAdapter, times(1)).initialize(config, true);
    }

    @Test
    void drawBox() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawBox(10, 10));

        verify(mockDriverAdapter).drawBox(widthCaptor.capture(), heightCaptor.capture());
        assertEquals(10, widthCaptor.getValue().intValue());
        assertEquals(10, heightCaptor.getValue().intValue());
    }

    @Test
    void drawBox1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawBox(5, 10, 15, 20));

        verify(mockDriverAdapter).drawBox(xCaptor.capture(), yCaptor.capture(), widthCaptor.capture(), heightCaptor.capture());

        assertEquals(5, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals(15, widthCaptor.getValue().intValue());
        assertEquals(20, heightCaptor.getValue().intValue());
    }

    @Test
    void drawCircle() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawCircle(20, U8g2Graphics.U8G2_DRAW_LOWER_LEFT | U8g2Graphics.U8G2_DRAW_UPPER_RIGHT));

        verify(mockDriverAdapter).drawCircle(radiusCaptor.capture(), optionsCaptor.capture());

        assertEquals(20, radiusCaptor.getValue().intValue());
        assertEquals(U8g2Graphics.U8G2_DRAW_LOWER_LEFT | U8g2Graphics.U8G2_DRAW_UPPER_RIGHT, optionsCaptor.getValue().intValue());
    }

    @Test
    void drawCircle1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawCircle(0, 10, 20, U8g2Graphics.U8G2_DRAW_LOWER_LEFT | U8g2Graphics.U8G2_DRAW_UPPER_RIGHT));

        ArgumentCaptor<Integer> radius = ArgumentCaptor.forClass(Integer.class);

        verify(mockDriverAdapter).drawCircle(xCaptor.capture(), yCaptor.capture(), radius.capture(), optionsCaptor.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals(20, radius.getValue().intValue());
        assertEquals(U8g2Graphics.U8G2_DRAW_LOWER_LEFT | U8g2Graphics.U8G2_DRAW_UPPER_RIGHT, optionsCaptor.getValue().intValue());
    }

    @Test
    void drawDisc() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawDisc(30, U8g2Graphics.U8G2_DRAW_ALL));

        verify(mockDriverAdapter).drawDisc(radiusCaptor.capture(), optionsCaptor.capture());

        assertEquals(30, radiusCaptor.getValue().intValue());
        assertEquals(U8g2Graphics.U8G2_DRAW_ALL, optionsCaptor.getValue().intValue());
    }

    @Test
    void drawDisc1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawDisc(5, 10, 30, U8g2Graphics.U8G2_DRAW_ALL));

        verify(mockDriverAdapter).drawDisc(xCaptor.capture(), yCaptor.capture(), radiusCaptor.capture(), optionsCaptor.capture());

        assertEquals(5, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals(30, radiusCaptor.getValue().intValue());
        assertEquals(U8g2Graphics.U8G2_DRAW_ALL, optionsCaptor.getValue().intValue());
    }

    @Test
    void drawEllipse() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawEllipse(5, 10, U8g2Graphics.U8G2_DRAW_ALL));

        ArgumentCaptor<Integer> rx = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> ry = ArgumentCaptor.forClass(Integer.class);

        verify(mockDriverAdapter).drawEllipse(rx.capture(), ry.capture(), optionsCaptor.capture());

        assertEquals(5, rx.getValue().intValue());
        assertEquals(10, ry.getValue().intValue());
        assertEquals(U8g2Graphics.U8G2_DRAW_ALL, optionsCaptor.getValue().intValue());
    }

    @Test
    void drawEllipse1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawEllipse(0, 8, 5, 10, U8g2Graphics.U8G2_DRAW_ALL));

        ArgumentCaptor<Integer> rx = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> ry = ArgumentCaptor.forClass(Integer.class);

        verify(mockDriverAdapter).drawEllipse(xCaptor.capture(), yCaptor.capture(), rx.capture(), ry.capture(), optionsCaptor.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(8, yCaptor.getValue().intValue());
        assertEquals(5, rx.getValue().intValue());
        assertEquals(10, ry.getValue().intValue());
        assertEquals(U8g2Graphics.U8G2_DRAW_ALL, optionsCaptor.getValue().intValue());
    }

    @Test
    void drawFilledEllipse() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawFilledEllipse(5, 10, U8g2Graphics.U8G2_DRAW_ALL));

        ArgumentCaptor<Integer> rx = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> ry = ArgumentCaptor.forClass(Integer.class);

        verify(mockDriverAdapter).drawFilledEllipse(rx.capture(), ry.capture(), optionsCaptor.capture());

        assertEquals(5, rx.getValue().intValue());
        assertEquals(10, ry.getValue().intValue());
        assertEquals(U8g2Graphics.U8G2_DRAW_ALL, optionsCaptor.getValue().intValue());
    }

    @Test
    void drawFilledEllipse1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawFilledEllipse(0, 8, 5, 10, U8g2Graphics.U8G2_DRAW_ALL));

        ArgumentCaptor<Integer> rx = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> ry = ArgumentCaptor.forClass(Integer.class);

        verify(mockDriverAdapter).drawFilledEllipse(xCaptor.capture(), yCaptor.capture(), rx.capture(), ry.capture(), optionsCaptor.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(8, yCaptor.getValue().intValue());
        assertEquals(5, rx.getValue().intValue());
        assertEquals(10, ry.getValue().intValue());
        assertEquals(U8g2Graphics.U8G2_DRAW_ALL, optionsCaptor.getValue().intValue());
    }

    @Test
    void drawFrame() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawFrame(0, 8));

        verify(mockDriverAdapter).drawFrame(widthCaptor.capture(), heightCaptor.capture());

        assertEquals(0, widthCaptor.getValue().intValue());
        assertEquals(8, heightCaptor.getValue().intValue());
    }

    @Test
    void drawFrame1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawFrame(5, 10, 0, 8));

        verify(mockDriverAdapter).drawFrame(xCaptor.capture(), yCaptor.capture(), widthCaptor.capture(), heightCaptor.capture());

        assertEquals(5, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals(0, widthCaptor.getValue().intValue());
        assertEquals(8, heightCaptor.getValue().intValue());
    }

    @Test
    void drawGlyph() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawGlyph((short) 128));

        ArgumentCaptor<Short> encoding = ArgumentCaptor.forClass(Short.class);

        verify(mockDriverAdapter).drawGlyph(encoding.capture());

        assertEquals(128, encoding.getValue().shortValue());
    }

    @Test
    void drawGlyph1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawGlyph(0, 10, (short) 128));

        ArgumentCaptor<Short> encoding = ArgumentCaptor.forClass(Short.class);

        verify(mockDriverAdapter).drawGlyph(xCaptor.capture(), yCaptor.capture(), encoding.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals(128, encoding.getValue().shortValue());
    }

    @Test
    void drawHLine() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawHLine(128));

        verify(mockDriverAdapter).drawHLine(widthCaptor.capture());

        assertEquals(128, widthCaptor.getValue().intValue());
    }

    @Test
    void drawHLine1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawHLine(0, 5, 128));

        verify(mockDriverAdapter).drawHLine(xCaptor.capture(), yCaptor.capture(), widthCaptor.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(5, yCaptor.getValue().intValue());
        assertEquals(128, widthCaptor.getValue().intValue());
    }

    @Test
    void drawVLine() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawVLine(128));

        ArgumentCaptor<Integer> length = ArgumentCaptor.forClass(Integer.class);

        verify(mockDriverAdapter).drawVLine(length.capture());

        assertEquals(128, length.getValue().intValue());
    }

    @Test
    void drawVLine1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawVLine(0, 10, 128));
        ArgumentCaptor<Integer> length = ArgumentCaptor.forClass(Integer.class);
        verify(mockDriverAdapter).drawVLine(xCaptor.capture(), yCaptor.capture(), length.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals(128, length.getValue().intValue());
    }

    @Test
    void drawLine() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawLine(0, 10));

        verify(mockDriverAdapter).drawLine(xCaptor.capture(), yCaptor.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
    }

    @Test
    void drawLine1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawLine(0, 10, 15, 20));

        ArgumentCaptor<Integer> x1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> y1 = ArgumentCaptor.forClass(Integer.class);

        verify(mockDriverAdapter).drawLine(xCaptor.capture(), yCaptor.capture(), x1.capture(), y1.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals(15, x1.getValue().intValue());
        assertEquals(20, y1.getValue().intValue());
    }

    @Test
    void drawPixel() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow((Executable) driver::drawPixel);
        verify(mockDriverAdapter).drawPixel();
    }

    @Test
    void drawPixel1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawPixel(0, 10));
        verify(mockDriverAdapter).drawPixel(xCaptor.capture(), yCaptor.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
    }

    @Test
    void drawRoundedBox() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        assertDoesNotThrow(() -> driver.drawRoundedBox(0, 10, 15));

        verify(mockDriverAdapter).drawRoundedBox(widthCaptor.capture(), heightCaptor.capture(), radiusCaptor.capture());

        assertEquals(0, widthCaptor.getValue().intValue());
        assertEquals(10, heightCaptor.getValue().intValue());
        assertEquals(15, radiusCaptor.getValue().intValue());
    }

    @Test
    void drawRoundedBox1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawRoundedBox(10, 20, 0, 10, 15));
        verify(mockDriverAdapter).drawRoundedBox(xCaptor.capture(), yCaptor.capture(), widthCaptor.capture(), heightCaptor.capture(), radiusCaptor.capture());

        assertEquals(10, xCaptor.getValue().intValue());
        assertEquals(20, yCaptor.getValue().intValue());
        assertEquals(0, widthCaptor.getValue().intValue());
        assertEquals(10, heightCaptor.getValue().intValue());
        assertEquals(15, radiusCaptor.getValue().intValue());
    }

    @Test
    void drawRoundedFrame() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawRoundedFrame(0, 10, 15));
        verify(mockDriverAdapter).drawRoundedFrame(widthCaptor.capture(), heightCaptor.capture(), radiusCaptor.capture());

        assertEquals(0, widthCaptor.getValue().intValue());
        assertEquals(10, heightCaptor.getValue().intValue());
        assertEquals(15, radiusCaptor.getValue().intValue());
    }

    @Test
    void drawRoundedFrame1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawRoundedFrame(10, 20, 0, 10, 15));
        verify(mockDriverAdapter).drawRoundedFrame(xCaptor.capture(), yCaptor.capture(), widthCaptor.capture(), heightCaptor.capture(), radiusCaptor.capture());

        assertEquals(10, xCaptor.getValue().intValue());
        assertEquals(20, yCaptor.getValue().intValue());
        assertEquals(0, widthCaptor.getValue().intValue());
        assertEquals(10, heightCaptor.getValue().intValue());
        assertEquals(15, radiusCaptor.getValue().intValue());
    }

    @Test
    void drawString() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawString("Test"));
        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);
        verify(mockDriverAdapter).drawString(text.capture());
        assertEquals("Test", text.getValue());
    }

    @Test
    void drawString1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawString(0, 10, "Test"));
        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);
        verify(mockDriverAdapter).drawString(xCaptor.capture(), yCaptor.capture(), text.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals("Test", text.getValue());
    }

    @Test
    void drawTriangle() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawTriangle(0, 10, 15, 20));

        ArgumentCaptor<Integer> x1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> y1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> x2 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> y2 = ArgumentCaptor.forClass(Integer.class);

        verify(mockDriverAdapter).drawTriangle(x1.capture(), y1.capture(), x2.capture(), y2.capture());

        assertEquals(0, x1.getValue().intValue());
        assertEquals(10, y1.getValue().intValue());

        assertEquals(15, x2.getValue().intValue());
        assertEquals(20, y2.getValue().intValue());
    }

    @Test
    void drawTriangle1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawTriangle(2, 4, 0, 10, 15, 20));

        ArgumentCaptor<Integer> x1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> y1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> x2 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> y2 = ArgumentCaptor.forClass(Integer.class);

        verify(mockDriverAdapter).drawTriangle(xCaptor.capture(), yCaptor.capture(), x1.capture(), y1.capture(), x2.capture(), y2.capture());

        assertEquals(2, xCaptor.getValue().intValue());
        assertEquals(4, yCaptor.getValue().intValue());
        assertEquals(0, x1.getValue().intValue());
        assertEquals(10, y1.getValue().intValue());
        assertEquals(15, x2.getValue().intValue());
        assertEquals(20, y2.getValue().intValue());
    }

    @Test
    void drawXBM() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        File mockFile = mock(File.class);

        assertDoesNotThrow(() -> driver.drawXBM(0, 10, mockFile));

        ArgumentCaptor<File> fileArg = ArgumentCaptor.forClass(File.class);

        verify(mockDriverAdapter).drawXBM(widthCaptor.capture(), heightCaptor.capture(), fileArg.capture());

        assertEquals(0, widthCaptor.getValue().intValue());
        assertEquals(10, heightCaptor.getValue().intValue());
        assertEquals(mockFile, fileArg.getValue());
    }

    @Test
    void drawXBM1() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        File mockFile = mock(File.class);

        assertDoesNotThrow(() -> driver.drawXBM(5, 10, 15, 20, mockFile));

        ArgumentCaptor<File> fileArg = ArgumentCaptor.forClass(File.class);

        verify(mockDriverAdapter).drawXBM(xCaptor.capture(), yCaptor.capture(), widthCaptor.capture(), heightCaptor.capture(), fileArg.capture());

        assertEquals(5, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals(15, widthCaptor.getValue().intValue());
        assertEquals(20, heightCaptor.getValue().intValue());
        assertEquals(mockFile, fileArg.getValue());
    }

    @Test
    void drawXBM2() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        byte[] data = new byte[]{5, 10, 15};
        assertDoesNotThrow(() -> driver.drawXBM(15, 20, data));

        verify(mockDriverAdapter).drawXBM(widthCaptor.capture(), heightCaptor.capture(), byteArrayCaptor.capture());

        assertEquals(15, widthCaptor.getValue().intValue());
        assertEquals(20, heightCaptor.getValue().intValue());
        assertArrayEquals(data, byteArrayCaptor.getValue());
    }

    @Test
    void drawXBM3() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);

        byte[] data = new byte[]{5, 10, 15};
        assertDoesNotThrow(() -> driver.drawXBM(5, 10, 15, 20, data));

        verify(mockDriverAdapter).drawXBM(xCaptor.capture(), yCaptor.capture(), widthCaptor.capture(), heightCaptor.capture(), byteArrayCaptor.capture());

        assertEquals(5, xCaptor.getValue().intValue());
        assertEquals(10, yCaptor.getValue().intValue());
        assertEquals(15, widthCaptor.getValue().intValue());
        assertEquals(20, heightCaptor.getValue().intValue());
        assertArrayEquals(data, byteArrayCaptor.getValue());
    }

    @Test
    void drawUTF8() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawUTF8("☃"));

        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);

        verify(mockDriverAdapter).drawUTF8(text.capture());

        assertEquals("☃", text.getValue());
    }

    @Test
    void drawUTF81() {
        updateValidConfig(config);
        GlcdDriver driver = new GlcdDriver(config, true, mockEventHandler, mockDriverAdapter);
        assertDoesNotThrow(() -> driver.drawUTF8(0, 5, "☃"));

        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);

        verify(mockDriverAdapter).drawUTF8(xCaptor.capture(), yCaptor.capture(), text.capture());

        assertEquals(0, xCaptor.getValue().intValue());
        assertEquals(5, yCaptor.getValue().intValue());
        assertEquals("☃", text.getValue());
    }
/*
    @Test
    void getUTF8Width() {

    }

    @Test
    void setFont() {

    }

    @Test
    void setFont1() {

    }

    @Test
    void setFontMode() {

    }

    @Test
    void setFontDirection() {

    }

    @Test
    void setFontPosBaseline() {

    }

    @Test
    void setFontPosBottom() {

    }

    @Test
    void setFontPosTop() {

    }

    @Test
    void setFontPosCenter() {

    }

    @Test
    void setFontRefHeightAll() {

    }

    @Test
    void setFontRefHeightExtendedText() {

    }

    @Test
    void setFontRefHeightText() {

    }

    @Test
    void setFlipMode() {

    }

    @Test
    void setPowerSave() {

    }

    @Test
    void setDrawColor() {

    }

    @Test
    void initDisplay() {

    }

    @Test
    void firstPage() {

    }

    @Test
    void nextPage() {

    }

    @Test
    void getAscent() {

    }

    @Test
    void getDescent() {

    }

    @Test
    void getMaxCharWidth() {

    }

    @Test
    void getMaxCharHeight() {

    }

    @Test
    void sendBuffer() {

    }

    @Test
    void clearBuffer() {

    }

    @Test
    void clearDisplay() {

    }

    @Test
    void begin() {

    }

    @Test
    void getHeight() {

    }

    @Test
    void getWidth() {

    }

    @Test
    void clear() {

    }

    @Test
    void setCursor() {

    }

    @Test
    void setAutoPageClear() {

    }

    @Test
    void setBitmapMode() {

    }

    @Test
    void setContrast() {

    }

    @Test
    void setDisplayRotation() {

    }

    @Test
    void setDisplayRotation1() {

    }

    @Test
    void getBuffer() {

    }

    @Test
    void getBufferTileWidth() {

    }

    @Test
    void getBufferTileHeight() {

    }

    @Test
    void getBufferCurrTileRow() {

    }

    @Test
    void setBufferCurrTileRow() {

    }

    @Test
    void getStrWidth() {

    }

    @Test
    void setClipWindow() {

    }

    @Test
    void setMaxClipWindow() {

    }*/

    private void updateValidConfig(GlcdConfig config) {
        config.setDisplay(Glcd.ST7920.D_128x64);
        config.setBusInterface(GlcdCommProtocol.SPI_HW_4WIRE_ST7920);
        config.setPinMapConfig(new GlcdPinMapConfig());
        config.setOption(GlcdOption.ROTATION, GlcdRotation.ROTATION_NONE);
    }
}
