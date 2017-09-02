package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.Display;
import com.ibasco.pidisplay.core.DisplayRegion;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.events.EventDispatcher;
import com.ibasco.pidisplay.core.events.ValueChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract public class DisplayComponent<T extends Graphics>
        extends DisplayRegion implements Display<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayComponent.class);

    protected static final int DEFAULT_XPOS = 0;

    protected static final int DEFAULT_YPOS = 0;

    private ObservableProperty<Boolean> visible = new ObservableProperty<>(true);

    private List<DisplayComponent<T>> children = new ArrayList<>();

    protected DisplayComponent(int width, int height) {
        this(DEFAULT_XPOS, DEFAULT_YPOS, width, height);
    }

    protected DisplayComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        visible.addListener(this::onVisibilityChange);
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

    public boolean isVisible() {
        return this.visible.get();
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }

    protected void onVisibilityChange(ValueChangeEvent valueChangeEvent) {
        //to be implemented by sub-class
    }

    @Override
    public void draw(T graphics) {
        Objects.requireNonNull(graphics, "Graphics cannot be null");
        EventDispatcher.checkEventDispatchThread();
    }
}
