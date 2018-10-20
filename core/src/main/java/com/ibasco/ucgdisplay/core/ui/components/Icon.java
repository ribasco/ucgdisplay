package com.ibasco.ucgdisplay.core.ui.components;

import com.ibasco.ucgdisplay.core.DisplayNode;
import com.ibasco.ucgdisplay.core.ui.Graphics;

abstract public class Icon<T extends Graphics> extends DisplayNode<T> {
    protected Icon(Integer width, Integer height) {
        super(width, height);
    }

    protected Icon(Integer x, Integer y, Integer width, Integer height) {
        super(x, y, width, height);
    }

    @Override
    protected void drawNode(T graphics) {

    }
}
