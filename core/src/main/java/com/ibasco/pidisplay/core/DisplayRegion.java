package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;

abstract public class DisplayRegion implements Region {

    protected ObservableProperty<Integer> x = new ObservableProperty<>();

    protected ObservableProperty<Integer> y = new ObservableProperty<>();

    protected ObservableProperty<Integer> width = new ObservableProperty<>();

    protected ObservableProperty<Integer> height = new ObservableProperty<>();

    protected ObservableProperty<Integer> minWidth = new ObservableProperty<>();

    protected ObservableProperty<Integer> minHeight = new ObservableProperty<>();

    protected ObservableProperty<Integer> maxWidth = new ObservableProperty<>();

    protected ObservableProperty<Integer> maxHeight = new ObservableProperty<>();

    public DisplayRegion(Integer x, Integer y, Integer width, Integer height) {
        this.x.setValid(x);
        this.y.setValid(y);
        this.width.setValid(width);
        this.height.setValid(height);
        this.minWidth.setValid(width);
        this.minHeight.setValid(height);
        this.maxWidth.setValid(width);
        this.maxHeight.setValid(height);
    }

    public void setX(Integer x) {
        this.x.set(x);
    }

    public void setY(Integer y) {
        this.y.set(y);
    }

    public void setWidth(Integer width) {
        this.width.set(width);
    }

    public void setHeight(Integer height) {
        this.height.set(height);
    }

    @Override
    public Integer getMaxWidth() {
        return maxWidth.get();
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth.set(maxWidth);
    }

    @Override
    public Integer getMaxHeight() {
        return maxHeight.get();
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight.set(maxHeight);
    }

    public void setMinWidth(Integer minWidth) {
        if (minWidth > maxWidth.get())
            throw new IllegalArgumentException(String.format("Minimum width cannot be greater than the maximum width (min: %d, max: %d)", minWidth, maxWidth.get()));
        this.minWidth.set(minWidth);
    }

    public void setMinHeight(Integer minHeight) {
        if (minHeight > maxHeight.get())
            throw new IllegalArgumentException(String.format("Minimum height cannot be greater than the maximum height (min: %d, max: %d)", minHeight, maxHeight.get()));
        this.minHeight.set(minHeight);
    }

    @Override
    public Integer getMinWidth() {
        return this.minWidth.get();
    }

    @Override
    public Integer getMinHeight() {
        return this.minHeight.get();
    }

    @Override
    public Integer getX() {
        return x.get();
    }

    @Override
    public Integer getY() {
        return y.get();
    }

    @Override
    public Integer getWidth() {
        return width.get();
    }

    @Override
    public Integer getHeight() {
        return height.get();
    }
}
