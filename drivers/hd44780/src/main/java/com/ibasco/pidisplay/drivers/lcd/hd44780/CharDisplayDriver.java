package com.ibasco.pidisplay.drivers.lcd.hd44780;


import com.ibasco.pidisplay.core.system.DisplayDriver;
import com.ibasco.pidisplay.drivers.lcd.hd44780.enums.ScrollDirection;
import com.ibasco.pidisplay.drivers.lcd.hd44780.enums.TextDirection;

/**
 * Interface for Character based devices (e.e. HD44780)
 *
 * @author Rafael Ibasco
 */
public interface CharDisplayDriver extends DisplayDriver {
    /**
     * Resets the cursor to its original position
     */
    void home();

    /**
     * Turn on/off the LCD display
     *
     * @param state
     *         <code>true</code> to turn on display
     */
    void display(boolean state);

    void blink(boolean state);

    void cursor(boolean state);

    void autoscroll(boolean state);

    void scrollDisplay(ScrollDirection scrollDirection);

    void textDirection(TextDirection textDirection);

    void createChar(int num, byte[] charData);

    /**
     * Not applicable for this driver type
     */
    @Override
    default long getId() {
        return -1;
    }
}
