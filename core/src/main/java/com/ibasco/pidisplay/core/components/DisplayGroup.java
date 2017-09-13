package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

import java.util.List;

/**
 * A simple container for display components
 *
 * @param <T>
 *         An implementation of the {@link Graphics} interface
 *
 * @author Rafael Ibasco
 */
abstract public class DisplayGroup<T extends Graphics> extends DisplayNode<T> {

    public DisplayGroup() {
        super(null, null);
    }

    protected DisplayGroup(int width, int height) {
        super(width, height);
    }

    @Override
    public void add(DisplayNode<T> component) {
        super.add(component);
    }

    @Override
    public void remove(DisplayNode<T> component) {
        super.remove(component);
    }

    @Override
    public List<DisplayNode<T>> getChildren() {
        return super.getChildren();
    }

    @Override
    protected void drawNode(T graphics) {
        //no implementation
    }
}
