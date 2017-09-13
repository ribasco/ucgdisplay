package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.enums.ProgressStyle;

abstract public class DisplayProgress extends DisplayNode {

    private static final int DEFAULT_WIDTH = 4;

    private static final int DEFAULT_HEIGHT = 1;

    protected ObservableProperty<ProgressStyle> progressStyle = new ObservableProperty<>(ProgressStyle.CONTINUOUS);

    protected ObservableProperty<Integer> value = new ObservableProperty<>(0);

    protected ObservableProperty<Integer> minValue = new ObservableProperty<>(0);

    protected ObservableProperty<Integer> maxValue = new ObservableProperty<>(100);

    public DisplayProgress() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public void drawNode(Graphics graphics) {

    }
}
