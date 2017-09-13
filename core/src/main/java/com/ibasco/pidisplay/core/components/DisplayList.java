package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayList<T extends Graphics> extends DisplayNode<T> {

    public DisplayList() {
        super(null, null);
    }

    @Override
    public void drawNode(T graphics) {

    }
}
