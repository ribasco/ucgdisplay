package com.ibasco.ucgdisplay.core.ui.components;

import com.ibasco.ucgdisplay.core.ui.Graphics;

abstract public class Password<T extends Graphics> extends TextInput<T> {

    protected Password(Integer width, Integer height) {
        super(width, height);
    }

    protected Password(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }
}
