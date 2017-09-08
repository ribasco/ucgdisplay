package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;

@SuppressWarnings("WeakerAccess")
abstract public class DisplayRegion implements Region {

    protected ObservableProperty<Integer> x = new ObservableProperty<>();

    protected ObservableProperty<Integer> y = new ObservableProperty<>();

    protected ObservableProperty<Integer> width = new ObservableProperty<>();

    protected ObservableProperty<Integer> height = new ObservableProperty<>();

    protected ObservableProperty<Integer> maxWidth = new ObservableProperty<>();

    protected ObservableProperty<Integer> maxHeight = new ObservableProperty<>();

    public DisplayRegion(int x, int y, int width, int height) {
        this.x.set(x, false);
        this.y.set(y, false);
        this.width.set(width, false);
        this.height.set(height, false);
        this.maxWidth.set(width, false);
        this.maxHeight.set(height, false);
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public void setY(int y) {
        this.y.set(y);
    }

    public void setWidth(int width) {
        this.width.set(width);
    }

    public void setHeight(int height) {
        this.height.set(height);
    }

    public int getMaxWidth() {
        return maxWidth.get();
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth.set(maxWidth);
    }

    public int getMaxHeight() {
        return maxHeight.get();
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight.set(maxHeight);
    }

    @Override
    public int getX() {
        return x.get();
    }

    @Override
    public int getY() {
        return y.get();
    }

    @Override
    public int getWidth() {
        return width.get();
    }

    @Override
    public int getHeight() {
        return height.get();
    }
}
