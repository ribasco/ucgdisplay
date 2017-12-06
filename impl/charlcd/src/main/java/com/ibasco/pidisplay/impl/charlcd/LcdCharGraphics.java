package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.core.CharGraphics;
import com.ibasco.pidisplay.core.GraphicsBuffer;
import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;
import com.ibasco.pidisplay.core.util.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcdCharGraphics implements CharGraphics {

    public static final Logger log = LoggerFactory.getLogger(LcdCharGraphics.class);

    @Deprecated
    public static final byte CHAR_SPACE = 32;
    @Deprecated
    public static final byte CHAR_RIGHTARROW = 1;
    @Deprecated
    public static final byte CHAR_BULLETPOINT = 2;
    @Deprecated
    public static final byte CHAR_LEFTARROW = 3;

    private CharDisplayDriver driver;

    private GraphicsBuffer buffer;

    private final Object mutex = new Object();

    public LcdCharGraphics(CharDisplayDriver driver) {
        this.driver = driver;
        this.buffer = GraphicsBuffer.allocate(driver.getWidth(), driver.getHeight());
    }

    @Override
    public void setDisplayCursor(int x, int y) {
        synchronized (mutex) {
            this.driver.setCursor(x, y);
        }
    }

    @Override
    public void setCursor(int x, int y) {
        synchronized (mutex) {
            this.buffer.cursor(x, y);
        }
    }

    @Override
    public void drawText(String text) {
        if (StringUtils.isEmpty(text))
            return;
        synchronized (mutex) {
            this.buffer.put(text.getBytes());
        }
    }

    @Override
    public int getCursorX() {
        synchronized (mutex) {
            return buffer.cursorX();
        }
    }

    @Override
    public int getCursorY() {
        synchronized (mutex) {
            return buffer.cursorY();
        }
    }

    @Override
    public int getWidth() {
        return driver.getWidth();
    }

    @Override
    public int getHeight() {
        return driver.getHeight();
    }

    @Override
    public void clear() {
        synchronized (mutex) {
            buffer.clear(true);
        }
    }

    @Override
    public boolean isDirty() {
        synchronized (mutex) {
            return buffer.isModified();
        }
    }

    @Override
    public void setCursorBlink(boolean state) {
        synchronized (mutex) {
            driver.blink(state);
            driver.cursor(state);
        }
    }

    @Override
    public void setAutoscroll(boolean state) {
        synchronized (mutex) {
            driver.autoscroll(state);
        }
    }

    @Override
    public void setBlink(boolean state) {
        synchronized (mutex) {
            driver.blink(state);
        }
    }

    @Override
    public CharDisplayDriver getDriver() {
        return driver;
    }

    public GraphicsBuffer getBuffer() {
        synchronized (mutex) {
            return buffer.duplicate();
        }
    }

    @Override
    public void flush() {
        synchronized (mutex) {
            if (!buffer.isModified())
                return;
            log.debug("Buffer modified...flushing");
            byte[] tmp = new byte[getWidth()];
            for (int row = 0; row < getHeight(); row++) {
                buffer.get(row, tmp);
                driver.setCursor(0, row);
                driver.write(ArrayUtils.replaceNullBytes(tmp, (byte) 32));
            }
            buffer.save();
            driver.setCursor(buffer.cursorX(), buffer.cursorY());
        }
    }
}
