package com.ibasco.ucgdisplay.core;

import com.ibasco.ucgdisplay.core.beans.ObservableProperty;

abstract public class DisplayRegion implements Region {

    protected ObservableProperty<Integer> leftPos = new ObservableProperty<>();

    protected ObservableProperty<Integer> topPos = new ObservableProperty<>();

    protected ObservableProperty<Integer> width = new ObservableProperty<>();

    protected ObservableProperty<Integer> height = new ObservableProperty<>();

    protected ObservableProperty<Integer> minWidth = new ObservableProperty<>();

    protected ObservableProperty<Integer> minHeight = new ObservableProperty<>();

    protected ObservableProperty<Integer> maxWidth = new ObservableProperty<>();

    protected ObservableProperty<Integer> maxHeight = new ObservableProperty<>();

    public DisplayRegion(Integer left, Integer top, Integer width, Integer height) {
        this.leftPos.setValid(left);
        this.topPos.setValid(top);
        this.width.setValid(width);
        this.height.setValid(height);
        this.minWidth.setValid(width);
        this.minHeight.setValid(height);
        this.maxWidth.setValid(width);
        this.maxHeight.setValid(height);
    }

    public void setLeftPos(Integer leftPos) {
        this.leftPos.set(leftPos);
    }

    public void setTopPos(Integer topPos) {
        this.topPos.set(topPos);
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
    public Integer getLeftPos() {
        return leftPos.get();
    }

    @Override
    public Integer getTopPos() {
        return topPos.get();
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
