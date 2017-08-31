package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.Display;
import com.ibasco.pidisplay.core.DisplayRegion;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.events.EventDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    protected void add(DisplayComponent<T> component) {
        this.children.add(component);
    }

    protected void remove(DisplayComponent<T> component) {
        this.children.remove(component);
    }

    protected List<DisplayComponent<T>> getChildren() {
        return this.children;
    }

    @Override
    public void draw(T graphics) {
        Objects.requireNonNull(graphics, "Graphics cannot be null");
        EventDispatcher.checkEventDispatchThread();
    }
}
