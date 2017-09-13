package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.Dialog;
import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

abstract public class DisplayDialog<T extends Graphics> extends DisplayNode<T> implements Dialog {

    protected DisplayDialog(Integer x, Integer y, Integer width, Integer height) {
        super(x, y, width, height);
    }

    @Override
    protected void drawNode(T graphics) {

    }
}
