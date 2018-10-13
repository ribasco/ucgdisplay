package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.core.Controller;
import com.ibasco.pidisplay.core.ui.CharGraphics;
import com.ibasco.pidisplay.drivers.lcd.hd44780.CharDisplayDriver;

/**
 * The character display controller
 *
 * @author Rafael Ibasco
 */
public class LcdController extends Controller<CharGraphics> {
    public LcdController(CharDisplayDriver driver) {
        super(new LcdCharGraphics(driver));
    }

    @Override
    public CharGraphics getGraphics() {
        return super.getGraphics();
    }
}
