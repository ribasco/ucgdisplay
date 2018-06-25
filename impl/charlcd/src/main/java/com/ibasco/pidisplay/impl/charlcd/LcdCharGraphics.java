package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;
import com.ibasco.pidisplay.core.ui.CharData;
import com.ibasco.pidisplay.core.ui.CharGraphics;
import com.ibasco.pidisplay.core.ui.CharManager;
import com.ibasco.pidisplay.core.ui.DisplayBuffer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A thread-safe implementation of the {@link CharGraphics} interface
 */
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

    private DisplayBuffer buffer;

    private final Object mutex = new Object();

    private CharManager charManager;

    public LcdCharGraphics(CharDisplayDriver driver) {
        this.driver = driver;
        this.buffer = DisplayBuffer.allocate(driver.getWidth(), driver.getHeight());
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
    public CharManager charManager() {
        if (charManager == null) {
            charManager = new CharManager(this.driver);
        }
        return charManager;
    }

    @Override
    public void drawChar(String key) {
        if (StringUtils.isEmpty(key))
            return;
        drawChar(charManager.getCharData(key));
    }

    @Override
    public void drawChar(CharData charData) {
        byte allocationIndex = charManager.allocateChar(charData);
        if (allocationIndex > -1) {
            synchronized (mutex) {
                this.buffer.put(allocationIndex);
            }
        } else {
            log.warn("Unable to allocate chardata");
        }
    }

    @Override
    public void drawText(String text) {
        if (StringUtils.isEmpty(text))
            return;
        synchronized (mutex) {
            this.buffer.put(charManager().processText(text));
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
    public boolean hasChanges() {
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
    public void clearLine() {
        clearLine(buffer.cursorY());
    }

    @Override
    public void clearLine(int lineNumber) {
        synchronized (mutex) {
            buffer.cursor(0, lineNumber);
            buffer.put(StringUtils.repeat(StringUtils.SPACE, getWidth()).getBytes());
        }
    }

    @Override
    public CharDisplayDriver getDriver() {
        return driver;
    }

    public DisplayBuffer getBuffer() {
        synchronized (mutex) {
            return buffer.duplicate();
        }
    }

    @Override
    public void flush() {
        //do not proceed if there are no changes since the last flush
        if (!buffer.isModified())
            return;

        synchronized (mutex) {
            byte[] tmp = new byte[getWidth()];
            //log.info("Flushing Data: ");
            for (int row = 0; row < getHeight(); row++) {
                buffer.get(row, tmp);
                //log.info("[ROW: {}] = {}", row, ByteUtils.toHexString(tmp));
                driver.setCursor(0, row);
                driver.write(tmp);
            }
            buffer.save();
            driver.setCursor(buffer.cursorX(), buffer.cursorY());
        }
    }
}
