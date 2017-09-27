package com.ibasco.pidisplay.impl.lcd.hitachi;

import com.ibasco.pidisplay.core.DisplayController;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;

import java.util.concurrent.ExecutorService;

public class LcdController extends DisplayController<LcdGraphics> {
    public LcdController(LcdDriver driver) {
        this(driver, null);
    }

    public LcdController(LcdDriver driver, ExecutorService executorService) {
        super(new LcdGraphics(driver), executorService);
    }
}
