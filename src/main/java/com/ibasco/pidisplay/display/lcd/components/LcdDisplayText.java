package com.ibasco.pidisplay.display.lcd.components;

import com.ibasco.pidisplay.core.components.DisplayText;
import com.ibasco.pidisplay.display.lcd.LcdGraphics;
import org.apache.commons.lang3.StringUtils;

public class LcdDisplayText extends DisplayText<LcdGraphics> {
    public LcdDisplayText() {
        super();
    }

    public LcdDisplayText(String text) {

    }

    public LcdDisplayText(int width, int height) {
        this(width, height, StringUtils.EMPTY);
    }

    public LcdDisplayText(int width, int height, String text) {
        super(width, height, text);
    }

    public LcdDisplayText(int x, int y, int width, int height, String text) {
        super(x, y, width, height, text);
    }
}
