package com.ibasco.pidisplay.impl.charlcd.components;

import com.ibasco.pidisplay.core.CharGraphics;
import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.beans.ObservableList;
import com.ibasco.pidisplay.core.components.DisplayPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcdPane extends DisplayPane<CharGraphics> {

    public static final Logger log = LoggerFactory.getLogger(LcdPane.class);

    public LcdPane() {
        super();
    }

    @Override
    public ObservableList<DisplayNode<CharGraphics>> getChildren() {
        return super.getChildren();
    }

    public void add(DisplayNode<CharGraphics> component) {
        super.add(component);
    }

    @Override
    public void remove(DisplayNode<CharGraphics> component) {
        super.remove(component);
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren();
    }

    @Override
    protected void onActivate() {
        log.debug("{} activated.", this);
    }
}
