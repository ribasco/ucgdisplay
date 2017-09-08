package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayComponent;
import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayList<T extends Graphics> extends DisplayComponent<T> {

    private static final int DEFAULT_WIDTH = 5;

    private static final int DEFAULT_HEIGHT = 3;

    public DisplayList() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public void draw(T graphics) {

    }
}
