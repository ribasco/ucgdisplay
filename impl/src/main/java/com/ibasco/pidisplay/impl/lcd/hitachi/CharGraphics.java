package com.ibasco.pidisplay.impl.lcd.hitachi;

import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;
import com.pi4j.component.lcd.LCDTextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Graphics implementation for Character Display Graphics
 *
 * @author Rafael Ibasco
 */
public class CharGraphics implements Graphics {

    private static final Logger log = LoggerFactory.getLogger(CharGraphics.class);

    private LcdDriver driver;

    //<editor-fold desc="Custom Character Constants">
    public static final byte CHAR_RIGHTARROW = 0x7E;

    public static final byte CHAR_LEFTARROW = 0x7F;

    public static final byte CHAR_SPACE = (byte) 0xA0;

    public static final byte CHAR_BULLETPOINT = (byte) 0xA5;

    public static final byte[] CHAR_CUSTOM_RETURN = new byte[]{
            0b00100,
            0b01110,
            0b10101,
            0b00100,
            0b00100,
            0b00100,
            0b11100,
            0b00000
    };
    //</editor-fold>

    private AtomicInteger colOffset = new AtomicInteger(0);

    private AtomicInteger rowOffset = new AtomicInteger(0);

    private CharBuffer[] buffer;

    CharGraphics(LcdDriver driver) {
        this.driver = driver;
        char[] cbuf = new char[driver.getColumnCount() * driver.getRowCount()];
        Arrays.fill(cbuf, ' ');
        log.debug("Initialized Buffer Dimensions to {} x {}", driver.getColumnCount(), driver.getRowCount());
        buffer = new CharBuffer[driver.getRowCount()];
        for (int i = 0; i < buffer.length; i++)
            buffer[i] = CharBuffer.allocate(driver.getColumnCount());
    }

    @Override
    public void clear() {
        driver.clear();
    }

    @Override
    public void cursorBlink(boolean state) {
        driver.cursor(state);
        driver.blink(state);
    }

    @Override
    public void clear(int y) {
        driver.clear(y);
    }

    @Override
    public void clear(int x, int y, int length) {
        driver.clear(y, x, length);
    }

    @Override
    public void setCursor(int x, int y) {
        this.colOffset.set(x);
        this.rowOffset.set(y);
        driver.setCursorPosition(y, x);
    }

    @Override
    public void drawText(char data) {
        driver.write(data);
        colOffset.incrementAndGet();
    }

    @Override
    public void drawText(char[] data) {
        driver.write(data);
        colOffset.addAndGet(data.length - 1);
    }

    @Override
    public void drawText(byte[] data) {
        driver.write(data);
        colOffset.addAndGet(data.length);
    }

    @Override
    public void drawText(byte data) {
        driver.write(data);
        this.colOffset.incrementAndGet();
    }

    @Override
    public void drawText(String text) {
        driver.write(text);
        colOffset.addAndGet(text.length() - 1);
    }
/*
    @Override
    public void drawText(int y, String text) {
        driver.write(y, text);
    }

    @Override
    public void drawText(String text, int x, int y) {
        driver.write(text);
    }

    @Override
    public void drawText(String text, Object... args) {
        driver.write(text, args);
    }

    @Override
    public void drawText(int y, String text, TextAlignment alignment) {
        drawText(y, text, alignment, new Object[]{});
    }

    @Override
    public void drawText(int y, String text, TextAlignment alignment, Object... arguments) {
        driver.write(y, text, toLcdTextAlignment(alignment), arguments);
    }*/

    @Override
    public int getWidth() {
        return driver.getColumnCount();
    }

    @Override
    public int getHeight() {
        return driver.getRowCount();
    }

    private LCDTextAlignment toLcdTextAlignment(TextAlignment alignment) {
        LCDTextAlignment a;
        if (alignment == TextAlignment.CENTER)
            a = LCDTextAlignment.ALIGN_CENTER;
        else if (alignment == TextAlignment.LEFT)
            a = LCDTextAlignment.ALIGN_LEFT;
        else if (alignment == TextAlignment.RIGHT)
            a = LCDTextAlignment.ALIGN_RIGHT;
        else
            throw new IllegalStateException("Invalid alignment");
        return a;
    }

    public int getColOffset() {
        return colOffset.get();
    }

    public int getRowOffset() {
        return rowOffset.get();
    }

    @Override
    public void flush() {
    }


}
