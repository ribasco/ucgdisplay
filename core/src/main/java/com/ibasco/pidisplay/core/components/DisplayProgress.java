package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayComponent;
import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayProgress extends DisplayComponent {

    private static final int DEFAULT_WIDTH = 4;

    private static final int DEFAULT_HEIGHT = 1;

    public DisplayProgress() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public void draw(Graphics graphics) {

    }
}
