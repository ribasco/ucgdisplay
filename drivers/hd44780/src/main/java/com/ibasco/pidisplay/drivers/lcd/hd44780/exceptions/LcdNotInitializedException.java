package com.ibasco.pidisplay.drivers.lcd.hd44780.exceptions;

public class LcdNotInitializedException extends RuntimeException {
    public LcdNotInitializedException() {
        super("LCD has not yet been initialized. Please remember to call begin() first!");
    }
}
