package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.Graphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A container for display components
 *
 * @param <T>
 *         Implementation Type of {@link Graphics}
 */
abstract public class DisplayGroup<T extends Graphics> extends DisplayComponent<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayGroup.class);

    private static final int DEFAULT_WIDTH = 5;

    private static final int DEFAULT_HEIGHT = 2;

    public DisplayGroup() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    protected DisplayGroup(int width, int height) {
        super(width, height);
    }

    @Override
    public void add(DisplayComponent<T> component) {
        super.add(component);
    }

    @Override
    public void remove(DisplayComponent<T> component) {
        super.remove(component);
    }

    @Override
    public List<DisplayComponent<T>> getChildren() {
        return super.getChildren();
    }

    @Override
    public void draw(T graphics) {
        log.debug("Drawing Display Tree: ");
        drawTree(this, graphics, 0);
    }

    protected void drawTree(DisplayComponent<T> component, T graphics, int depth) {
        for (DisplayComponent<T> i : component.getChildren()) {
            i.draw(graphics);
            drawTree(i, graphics, depth + 1);
        }
    }
}
