package com.ibasco.ucgdisplay.core.input;

/**
 * Base interface for all Display Devices
 *
 * @author Rafael Ibasco
 */
public interface DisplayDriver {
    /**
     * <p>Returns the height of the display.</p>
     *
     * @return The height of the display.
     *
     * @see #getWidth()
     */
    int getHeight();

    /**
     * <p>Returns the width of the display.</p>
     *
     * @return The width of the display.
     *
     * @see #getWidth()
     */
    int getWidth();

    /**
     * <p>Clears the display screen</p>
     */
    void clear();

    /**
     * <p>Sets the current position on the display</p>
     *
     * @param x
     *         The X-coordinate
     * @param y
     *         The Y-coordinate
     */
    void setCursor(int x, int y);

    /**
     * <p>Writes data to the display</p>
     *
     * @param data
     *         The byte(s) to send to the display
     */
    void write(byte... data);

    /**
     * Returns a unique identifier for this driver instance
     *
     * @return An unsigned long reperesenting the driver id
     */
    long getId();
}
