package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayComponent;
import com.ibasco.pidisplay.core.Graphics;

public class DisplayGrid<T extends Graphics> extends DisplayComponent<T> {
    protected DisplayGrid(int width, int height) {
        super(width, height);
    }

    protected DisplayGrid(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
}
