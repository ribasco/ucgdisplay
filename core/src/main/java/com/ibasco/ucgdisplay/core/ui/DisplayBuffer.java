package com.ibasco.ucgdisplay.core.ui;

import com.ibasco.ucgdisplay.core.exceptions.OffsetOutOfBoundsException;
import com.ibasco.ucgdisplay.core.util.ArrayUtils;
import com.ibasco.ucgdisplay.core.util.GraphicsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Rafael Ibasco
 */
public class DisplayBuffer {

    public static final Logger log = LoggerFactory.getLogger(DisplayBuffer.class);

    private final ByteBuffer buffer;

    private byte[] savedBytes;

    private int width;

    private int height;

    private int size;

    DisplayBuffer(int width, int height) {
        this(width, height, null);
    }

    DisplayBuffer(int width, int height, ByteBuffer buffer) {
        this.width = width;
        this.height = height;
        checkDimensions();
        this.size = width * height;
        this.buffer = (buffer == null) ? ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN) : buffer;
        clear(true);
        this.savedBytes = new byte[size];
        reset();
    }

    public static DisplayBuffer allocate(int width, int height) {
        return new DisplayBuffer(width, height);
    }

    public int getSize() {
        return size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Sets the X and Y coordinates of the display
     *
     * @param x
     *         The x-axis coordiante of the display
     * @param y
     *         The y-axis coordinate of the display
     *
     * @return The {@link DisplayBuffer} instance
     */
    public DisplayBuffer cursor(int x, int y) {
        int offset = GraphicsUtils.calcOffset(width, x, y);//calculateOffset(x, y);
        checkOffset(offset);
        buffer.position(offset);
        return this;
    }

    /**
     * @return Returns the Y-cursor relative to the buffer position
     */
    public int cursorY() {
        return GraphicsUtils.calcYOffset(getWidth(), getHeight(), checkOffset(cursor()));
    }

    /**
     * @return Returns the X-cursor relative to the buffer position
     */
    public int cursorX() {
        return GraphicsUtils.calcXOffset(getWidth(), checkOffset(cursor()));
    }

    public DisplayBuffer duplicate() {
        return new DisplayBuffer(this.getWidth(), this.getHeight(), this.buffer.duplicate());
    }

    public DisplayBuffer put(byte... data) {
        buffer.put(data);
        return this;
    }

    /**
     * Puts a string to the buffer. Used for testing only.
     *
     * @param data
     *         The {@link String} data to write to the buffer
     *
     * @return This buffer
     */
    DisplayBuffer put(String data) {
        buffer.put(Objects.toString(data, "").getBytes());
        return this;
    }

    /**
     * Extracts row data directly from the buffer. This does not affect the position of the buffer.
     *
     * @param row
     *         The row index
     * @param dest
     *         The byte array to store the row data. The buffer size MUST be equal to the width of the buffer.
     *
     * @return This buffer
     */
    public DisplayBuffer get(int row, byte[] dest) {
        if (dest.length != width)
            throw new IllegalArgumentException("Invalid buffer size. Expected: " + width);
        if (row > (getHeight() - 1))
            throw new IndexOutOfBoundsException("Row index is out of bounds. Max is " + (height - 1));
        int offset = checkOffset(row * width);
        System.arraycopy(buffer.array(), offset, dest, 0, width);
        return this;
    }

    public DisplayBuffer get(int x, int y, byte[] dest) {
        int offset = GraphicsUtils.calcOffset(width, x, y);
        checkOffset(offset);
        System.arraycopy(buffer.array(), offset, dest, 0, dest.length);
        return this;
    }

    public DisplayBuffer get(int x, int y, int offset, byte[] buffer) {
        checkBounds(x, y);
        int pos = GraphicsUtils.calcOffset(width, x, y);//calculateOffset(x, y);
        this.buffer.position(pos);
        this.buffer.get(buffer, offset, buffer.length);
        return this;
    }

    public DisplayBuffer get(byte[] buffer) {
        this.buffer.get(buffer);
        return this;
    }

    /**
     * Takes a snapshot of the current state of the buffer. The snapshot will then be comapred to the
     *
     * @return This buffer
     */
    public DisplayBuffer save() {
        System.arraycopy(array(), 0, savedBytes, 0, savedBytes.length);
        return this;
    }

    /**
     * @return A byte array containing the saved bytes. Modification of this array will not affect the internal state of this buffer.
     */
    public byte[] recall() {
        byte[] tmp = new byte[savedBytes.length];
        System.arraycopy(savedBytes, 0, tmp, 0, tmp.length);
        return tmp;
    }

    /**
     * Method to retrieve the changes made since the last time the buffer was saved. This method should usually be called before {@link #save()}.
     *
     * @return A byte array containing the modified data. Returns {@code null} if there are no changes since the last time {@link #save()} was called.
     */
    public byte[] diff() {
        return ArrayUtils.diff(savedBytes, array());
    }

    /**
     * Method to check if the snapshot contents are equals to the contents of the buffer.
     *
     * @return True if the buffer contents have been modified from the last saved state
     */
    public boolean isModified() {
        return !Arrays.equals(savedBytes, array());
    }

    public DisplayBuffer reset() {
        Arrays.fill(savedBytes, (byte) 32);
        return this;
    }

    public void clear() {
        clear(false);
    }

    public void clear(boolean resetValues) {
        if (resetValues)
            Arrays.fill(buffer.array(), (byte) 32);
        buffer.clear();
    }

    public byte[] array() {
        return buffer.array();
    }

    public boolean hasRemaining() {
        return buffer.hasRemaining();
    }

    public int remaining() {
        return buffer.remaining();
    }

    public int limit() {
        return buffer.limit();
    }

    public DisplayBuffer limit(int newLimit) {
        buffer.limit(newLimit);
        return this;
    }

    public int capacity() {
        return buffer.capacity();
    }

    public int cursor() {
        int max = getWidth() * getHeight();
        int pos = buffer.position();
        return pos >= max ? pos - 1 : pos;
    }

    private int checkOffset(int offset) {
        int max = (width * height) - 1;
        if (offset < 0 || offset > max)
            throw new OffsetOutOfBoundsException(offset, 0, max);
        return offset;
    }

    private void checkBounds(int x, int y) {
        if (x > (getWidth() - 1))
            throw new OffsetOutOfBoundsException(x, 0, getWidth() - 1);
        if (y > (getHeight() - 1))
            throw new OffsetOutOfBoundsException(y, 0, getHeight() - 1);
    }

    private void checkDimensions() {
        if ((getWidth() * getHeight()) <= 0)
            throw new IllegalArgumentException("Invalid dimensions");
    }
}
