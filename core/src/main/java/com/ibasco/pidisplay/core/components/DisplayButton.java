package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayButton<T extends Graphics> extends DisplayNode<T> {

    protected DisplayButton(Integer width, Integer height) {
        super(width, height);
    }

    protected DisplayButton(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }
}
