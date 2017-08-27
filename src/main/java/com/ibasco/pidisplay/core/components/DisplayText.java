package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayComponent;
import com.ibasco.pidisplay.core.Graphics;
import org.apache.commons.lang3.StringUtils;

abstract public class DisplayText<T extends Graphics> extends DisplayComponent<T> {

    private String text = StringUtils.EMPTY;

    protected static final int DEFAULT_WIDTH = 5;

    protected static final int DEFAULT_HEIGHT = 1;

    protected DisplayText() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, StringUtils.EMPTY);
    }

    protected DisplayText(int width, int height, String text) {
        this(DEFAULT_XPOS, DEFAULT_YPOS, width, height, text);
    }

    protected DisplayText(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(T graphics) {
        graphics.drawText(this.text);
    }
}
