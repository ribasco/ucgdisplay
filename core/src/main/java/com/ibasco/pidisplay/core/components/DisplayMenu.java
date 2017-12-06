package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayParent;
import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayMenu<T extends Graphics> extends DisplayParent<T> {

    public DisplayMenu() {
        super(-1, -1);
    }

    @Override
    public void drawNode(T graphics) {
    }
}
