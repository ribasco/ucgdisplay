package com.ibasco.pidisplay.impl.lcd.hitachi.components;

import com.ibasco.pidisplay.core.components.DisplayText;
import com.ibasco.pidisplay.impl.lcd.hitachi.LcdGraphics;
import org.apache.commons.lang3.StringUtils;

public class LcdText extends DisplayText<LcdGraphics> {
    public LcdText() {
        super();
    }

    public LcdText(String text) {
        this(text.length(), DEFAULT_HEIGHT, text);
    }

    public LcdText(int x, int y, String text) {
        this(x, y, text.length(), DEFAULT_HEIGHT, text);
    }

    public LcdText(int width, int height) {
        this(width, height, StringUtils.EMPTY);
    }

    public LcdText(int x, int y, int width, int height, String text) {
        super(x, y, width, height, text);
    }

    @Override
    public void draw(LcdGraphics graphics) {
        super.draw(graphics);
    }
}
