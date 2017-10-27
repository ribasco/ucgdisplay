package com.ibasco.pidisplay.impl.charlcd.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.beans.ObservableList;
import com.ibasco.pidisplay.core.components.DisplayPane;
import com.ibasco.pidisplay.impl.charlcd.LcdCharGraphics;

public class LcdPane extends DisplayPane<LcdCharGraphics> {
    public LcdPane() {
        super();
    }

    @Override
    public ObservableList<DisplayNode<LcdCharGraphics>> getChildren() {
        return super.getChildren();
    }

    @Override
    public void add(DisplayNode<LcdCharGraphics> component) {
        super.add(component);
    }

    @Override
    public void remove(DisplayNode<LcdCharGraphics> component) {
        super.remove(component);
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren();
    }
}
