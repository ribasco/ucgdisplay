package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.ui.Graphics;

abstract public class Button<T extends Graphics> extends DisplayNode<T> {

    protected Button(Integer width, Integer height) {
        super(width, height);
    }

    protected Button(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }
}
