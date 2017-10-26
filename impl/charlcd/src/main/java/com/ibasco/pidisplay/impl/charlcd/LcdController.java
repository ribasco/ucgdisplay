package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.core.Controller;
import com.ibasco.pidisplay.core.components.DisplayDialog;
import com.ibasco.pidisplay.drivers.lcd.hitachi.LcdDriver;

import java.util.Optional;

/**
 * The character display controller
 *
 * @author Rafael Ibasco
 */
public class LcdController extends Controller<CharGraphics> {
    public LcdController(LcdDriver driver) {
        super(new CharGraphics(driver));
    }

    @Override
    public <A> Optional<A> showAndWait(DisplayDialog<CharGraphics, A> displayDialog) {
        return super.showAndWait(displayDialog);
    }
}
