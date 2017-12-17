package com.ibasco.pidisplay.core.ui;

import com.ibasco.pidisplay.core.drivers.DisplayDriver;

/**
 * Generic Graphics Interface
 *
 * @author Rafael Ibasco
 */
public interface Graphics {

    /**
     * Draws text on the buffer
     *
     * @param text
     *         The text to draw
     */
    void drawText(String text);

    /**
     * Sets the cursor on the physical display. Buffer cursor is not affected. See {@link #setCursor(int, int)}
     *
     * @param x
     *         The x-coordinate of the display
     * @param y
     *         The y-coordinate of the display
     */
    void setDisplayCursor(int x, int y);

    /**
     * Sets the x and y write coordinates of the internal buffer.
     *
     * @param x
     *         The x-coordinates of the buffer
     * @param y
     *         The y-coordinates of the buffer
     */
    void setCursor(int x, int y);

    /**
     * Set cursor blink state
     *
     * @param state
     *         {@code true} to set cursor blink on
     */
    void setCursorBlink(boolean state);

    /**
     * @return The current x-coordinate
     */
    int getCursorX();

    int getCursorY();

    int getWidth();

    int getHeight();

    /**
     * Clears the display/buffer
     */
    void clear();

    /**
     * Check if there are any changes made since the last time the buffer was flushed
     *
     * @return {@code true} if the buffer has been modified
     */
    boolean hasChanges();

    /**
     * Sends the buffer contents to the display
     */
    void flush();

    DisplayDriver getDriver();
}
