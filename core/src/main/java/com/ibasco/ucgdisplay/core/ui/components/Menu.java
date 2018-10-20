package com.ibasco.ucgdisplay.core.ui.components;

import com.ibasco.ucgdisplay.core.DisplayParent;
import com.ibasco.ucgdisplay.core.ui.Graphics;

abstract public class Menu<T extends Graphics> extends DisplayParent<T> {
    public Menu() {
        super(-1, -1);
    }
}
