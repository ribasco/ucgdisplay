package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.ui.Graphics;

abstract public class DateTime<T extends Graphics> extends DisplayNode<T> {
    protected DateTime(Integer width, Integer height) {
        super(width, height);
    }

    protected DateTime(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }
}
