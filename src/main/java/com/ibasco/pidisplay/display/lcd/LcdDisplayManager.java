package com.ibasco.pidisplay.display.lcd;

import com.ibasco.pidisplay.core.DisplayManager;
import com.ibasco.pidisplay.drivers.lcd.LcdDriver;

public class LcdDisplayManager extends DisplayManager<LcdGraphics> {
    public LcdDisplayManager(LcdDriver driver) {
        super(new LcdGraphics(driver));
    }
}
