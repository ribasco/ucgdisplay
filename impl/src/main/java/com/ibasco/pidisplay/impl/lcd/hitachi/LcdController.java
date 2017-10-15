package com.ibasco.pidisplay.impl.lcd.hitachi;

import com.ibasco.pidisplay.core.DisplayController;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;

import java.util.concurrent.ExecutorService;

/**
 * The character display controller
 *
 * @author Rafael Ibasco
 */
public class LcdController extends DisplayController<CharGraphics> {
    public LcdController(LcdDriver driver) {
        this(driver, null);
    }

    public LcdController(LcdDriver driver, ExecutorService executorService) {
        super(new CharGraphics(driver), executorService);
    }
}
