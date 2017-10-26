package com.ibasco.pidisplay.drivers.lcd.hitachi.enums;

public enum LcdInstruction {
    /**
     * Sets the core functions of the LCD Display (e.g. Setting to 4/8 bit mode etc)
     */
    DISPLAY_FUNCTION,
    /**
     * Sets the mode of display on the LCD (e.g. Setting autoscroll, Text flow direction etc)
     */
    DISPLAY_MODE,
    /**
     * Set Control functions such as (Setting Cursor On/Off etc)
     */
    DISPLAY_CONTROL
}
