package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.events.EventDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("WeakerAccess")
abstract public class DisplayComponent<T extends Graphics>
        extends DisplayRegion implements Display<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayComponent.class);

    protected static final int DEFAULT_XPOS = 0;

    protected static final int DEFAULT_YPOS = 0;

    private ObservableProperty<Boolean> visible = new ObservableProperty<>(false);

    private List<DisplayComponent<T>> children = new ArrayList<>();

    private static final AtomicInteger idCtr = new AtomicInteger(0);

    private int id = idCtr.incrementAndGet();

    protected DisplayComponent(int width, int height) {
        this(DEFAULT_XPOS, DEFAULT_YPOS, width, height);
    }

    protected DisplayComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        redrawOnChange(visible);
    }

    public int getId() {
        return id;
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

    @Override
    public void draw(T graphics) {
        Objects.requireNonNull(graphics, "Graphics cannot be null");
        EventDispatcher.checkEventDispatchThread();
    }

    protected void redrawOnChange(ObservableProperty<?>... properties) {
        if (properties != null)
            for (ObservableProperty<?> property : properties)
                property.addListener(this::requestRedrawOnChange);
    }

    @SuppressWarnings("unused")
    private <A> void requestRedrawOnChange(A oldVal, A newVal) {
        this.redraw();
    }

    @Override
    public String toString() {
        return String.format("%s [idCtr: %d]", this.getClass().getSimpleName(), getId());
    }
}
