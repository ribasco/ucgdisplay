package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.DisplayParent;
import com.ibasco.pidisplay.core.ui.Graphics;

abstract public class DisplayMenu<T extends Graphics> extends DisplayParent<T> {
    public DisplayMenu() {
        super(-1, -1);
    }
}
