package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayInput;
import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayTextBox<T extends Graphics> extends DisplayText<T> implements DisplayInput {

    @Override
    public void clear() {

    }

    @Override
    protected void drawNode(T graphics) {

    }
}
