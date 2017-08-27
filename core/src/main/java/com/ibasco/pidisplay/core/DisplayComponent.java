package com.ibasco.pidisplay.core;

import java.util.ArrayList;
import java.util.List;

abstract public class DisplayComponent<T extends Graphics>
        extends DisplayRegion implements Display<T> {

    protected static final int DEFAULT_XPOS = 0;

    protected static final int DEFAULT_YPOS = 0;

    private List<DisplayComponent<T>> children = new ArrayList<>();

    protected DisplayComponent(int width, int height) {
        this(DEFAULT_XPOS, DEFAULT_YPOS, width, height);
    }

    protected DisplayComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    protected void addComponent(DisplayComponent<T> component) {
        this.children.add(component);
    }

    protected void removeComponent(DisplayComponent<T> component) {
        this.children.remove(component);
    }

    protected List<DisplayComponent<T>> getChildren() {
        return this.children;
    }
}
