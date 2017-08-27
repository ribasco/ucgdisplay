package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.display.lcd.LcdDisplayManager;
import com.ibasco.pidisplay.drivers.lcd.LcdDriver;

public class DisplayManagerFactory {
    public static LcdDisplayManager createLcdManager(LcdDriver lcdDriver) {
        return new LcdDisplayManager(lcdDriver);
    }
}
