package com.ibasco.pidisplay.core;

import java.util.List;

/**
 * A container for display components
 *
 * @param <T>
 *         Implementation Type of {@link Graphics}
 */
abstract public class DisplayContainer<T extends Graphics> extends DisplayComponent<T> {

    private static final int DEFAULT_WIDTH = 5;

    private static final int DEFAULT_HEIGHT = 2;

    public DisplayContainer() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    protected DisplayContainer(int width, int height) {
        super(width, height);
    }

    @Override
    public void addComponent(DisplayComponent<T> component) {
        super.addComponent(component);
    }

    @Override
    public void removeComponent(DisplayComponent<T> component) {
        super.removeComponent(component);
    }

    @Override
    public List<DisplayComponent<T>> getChildren() {
        return super.getChildren();
    }

    @Override
    public void draw(T graphics) {

    }
}
