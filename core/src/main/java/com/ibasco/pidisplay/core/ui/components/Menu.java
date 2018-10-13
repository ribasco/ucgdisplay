package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.DisplayParent;
import com.ibasco.pidisplay.core.ui.Graphics;

abstract public class Menu<T extends Graphics> extends DisplayParent<T> {
    public Menu() {
        super(-1, -1);
    }
}
