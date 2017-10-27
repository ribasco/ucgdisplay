package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.core.CharGraphics;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Graphics implementation for Character Display Graphics. This class is thread-safe.
 * *
 *
 * @author Rafael Ibasco
 */
public class LcdCharGraphics implements CharGraphics {

    private static final Logger log = LoggerFactory.getLogger(LcdCharGraphics.class);

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

    private final ByteBuffer buffer;

    private final Object mutex = new Object();

    LcdCharGraphics(LcdDriver driver) {
        //buffer index calculation = col + (row * maxCols)
        this.driver = driver;
        log.debug("Initialized Buffer Dimensions to {} x {}", driver.getColumnCount(), driver.getRowCount());
        buffer = ByteBuffer.allocateDirect(driver.getColumnCount() * driver.getRowCount());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        resetBuffer();
    }

    @Override
    public void clear() {
        synchronized (mutex) {
            resetBuffer();
        }
    }

    @Override
    public void cursorBlink(boolean state) {
        synchronized (mutex) {
            driver.cursor(state);
            driver.blink(state);
        }
    }

    @Override
    public void setCursor(int x, int y) {
        synchronized (mutex) {
            validateOffsets(x, y);
            int bufferIdx = x + (y * getWidth());
            buffer.position(bufferIdx);
            log.trace("Setting Cursor: x={}, y={}, idx={} ({})", x, y, buffer.position(), bufferIdx);
        }
    }

    @Override
    public void drawText(char data) {
        writeBuffer(data);
    }

    @Override
    public void drawText(char[] data) {
        writeBuffer(data);
    }

    @Override
    public void drawText(byte[] data) {
        writeBuffer(data);
    }

    @Override
    public void drawText(byte data) {
        writeBuffer(data);
    }

    @Override
    public void drawText(String text) {
        if (StringUtils.isBlank(text))
            return;
        writeBuffer(text.getBytes());
    }

    @Override
    public int getWidth() {
        return driver.getColumnCount();
    }

    @Override
    public int getHeight() {
        return driver.getRowCount();
    }

    public final ByteBuffer getBuffer() {
        return buffer.asReadOnlyBuffer();
    }

    private void writeBuffer(char... data) {
        if (data == null)
            return;
        if (data.length == 1) {
            writeBuffer((byte) data[0]);
            return;
        }
        writeBuffer(new String(data).getBytes());
    }

    private void writeBuffer(byte... data) {
        if (!buffer.hasRemaining()) {
            log.warn("No available space remaining in buffer (Size: {})", buffer.capacity());
            return;
        }
        synchronized (mutex) {
            if (data.length > buffer.remaining()) {
                log.warn("Data length is greater than the remaining size in buffer. Data has been trimmed");
                buffer.put(data, 0, buffer.remaining());
                return;
            }
            buffer.put(data);
        }
    }

    private void resetBuffer() {
        buffer.rewind();
        while (buffer.hasRemaining())
            buffer.put((byte) 32); //fill with space
        buffer.clear();
    }

    @Override
    public void flush() {
        synchronized (mutex) {
            buffer.rewind();
            int row = 0;
            while (buffer.hasRemaining()) {
                int size = (buffer.remaining() < getWidth()) ? buffer.remaining() : getWidth();
                byte[] rowData = new byte[size];
                buffer.get(rowData);
                driver.setCursorPosition(row++, 0);
                driver.write(rowData);
            }
            resetBuffer();
        }
    }

    private void validateOffsets(int x, int y) {
        if (x > (getWidth() - 1)) {
            throw new IllegalStateException("Invalid Column Offset: " + x);
        }
        if (y > (getHeight() - 1)) {
            throw new IllegalStateException("Invalid Row Offset: " + y);
        }
    }
}
