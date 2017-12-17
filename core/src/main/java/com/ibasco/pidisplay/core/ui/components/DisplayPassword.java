package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.ui.Graphics;

abstract public class DisplayPassword<T extends Graphics> extends DisplayTextBox<T> {

    protected DisplayPassword(Integer width, Integer height) {
        super(width, height);
    }

    protected DisplayPassword(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }
}
