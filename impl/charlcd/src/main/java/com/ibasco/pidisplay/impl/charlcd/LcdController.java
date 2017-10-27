package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.core.Controller;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;

/**
 * The character display controller
 *
 * @author Rafael Ibasco
 */
public class LcdController extends Controller<LcdCharGraphics> {
    public LcdController(LcdDriver driver) {
        super(new LcdCharGraphics(driver));
    }
}
