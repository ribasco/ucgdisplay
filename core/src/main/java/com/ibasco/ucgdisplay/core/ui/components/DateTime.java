package com.ibasco.ucgdisplay.core.ui.components;

import com.ibasco.ucgdisplay.core.DisplayNode;
import com.ibasco.ucgdisplay.core.ui.Graphics;

abstract public class DateTime<T extends Graphics> extends DisplayNode<T> {
    protected DateTime(Integer width, Integer height) {
        super(width, height);
    }

    protected DateTime(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }
}
