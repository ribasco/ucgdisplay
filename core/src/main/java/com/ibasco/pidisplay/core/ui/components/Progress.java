package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.enums.ProgressStyle;
import com.ibasco.pidisplay.core.ui.Graphics;

abstract public class Progress extends DisplayNode {

    private static final int DEFAULT_WIDTH = 4;

    private static final int DEFAULT_HEIGHT = 1;

    protected ObservableProperty<ProgressStyle> progressStyle = new ObservableProperty<>(ProgressStyle.CONTINUOUS);

    protected ObservableProperty<Integer> value = new ObservableProperty<>(0);

    protected ObservableProperty<Integer> minValue = new ObservableProperty<>(0);

    protected ObservableProperty<Integer> maxValue = new ObservableProperty<>(100);

    public Progress() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public void drawNode(Graphics graphics) {

    }
}
