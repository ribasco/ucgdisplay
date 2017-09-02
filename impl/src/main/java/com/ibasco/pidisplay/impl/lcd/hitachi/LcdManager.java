package com.ibasco.pidisplay.impl.lcd.hitachi;

import com.ibasco.pidisplay.core.DisplayManager;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;

public class LcdManager extends DisplayManager<LcdGraphics> {
    public LcdManager(LcdDriver driver) {
        super(new LcdGraphics(driver));
    }
}
