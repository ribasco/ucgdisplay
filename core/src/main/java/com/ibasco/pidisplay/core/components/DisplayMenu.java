package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayMenu<T extends Graphics> extends DisplayNode<T> {

    public DisplayMenu() {
        super(-1, -1);
    }

    @Override
    public void drawNode(T graphics) {
    }
}
