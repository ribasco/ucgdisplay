package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayIcon<T extends Graphics> extends DisplayNode<T> {
    protected DisplayIcon(Integer width, Integer height) {
        super(width, height);
    }

    protected DisplayIcon(Integer x, Integer y, Integer width, Integer height) {
        super(x, y, width, height);
    }

    @Override
    protected void drawNode(T graphics) {

    }
}
