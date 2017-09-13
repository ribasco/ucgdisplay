package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

public class DisplayKeyboard<T extends Graphics> extends DisplayNode<T> {
    protected DisplayKeyboard(int width, int height) {
        super(width, height);
    }

    protected DisplayKeyboard(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void drawNode(T graphics) {

    }
}
