package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.ui.Graphics;

abstract public class Password<T extends Graphics> extends TextBox<T> {

    protected Password(Integer width, Integer height) {
        super(width, height);
    }

    protected Password(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }
}
