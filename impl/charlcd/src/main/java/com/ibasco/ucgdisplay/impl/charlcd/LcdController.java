package com.ibasco.ucgdisplay.impl.charlcd;

import com.ibasco.ucgdisplay.core.Controller;
import com.ibasco.ucgdisplay.core.ui.CharGraphics;
import com.ibasco.ucgdisplay.drivers.clcd.CharDisplayDriver;

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
