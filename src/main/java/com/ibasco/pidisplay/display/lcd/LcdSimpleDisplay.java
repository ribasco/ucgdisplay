package com.ibasco.pidisplay.display.lcd;

import com.ibasco.pidisplay.core.Display;
import com.ibasco.pidisplay.core.enums.TextAlignment;

public class LcdSimpleDisplay implements Display<LcdGraphics> {
    @Override
    public void draw(LcdGraphics graphics) {
        graphics.drawText(1, "Simple Display", TextAlignment.CENTER);
    }
}
