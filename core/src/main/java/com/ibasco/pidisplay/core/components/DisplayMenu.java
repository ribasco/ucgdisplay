package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayMenu<T extends Graphics> extends DisplayComponent<T> {

    public DisplayMenu() {
        super(-1, -1);
    }

    @Override
    public void draw(T graphics) {
        setWidth(graphics.getWidth());
        setHeight(graphics.getHeight());
    }
}
