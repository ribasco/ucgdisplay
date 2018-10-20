package com.ibasco.ucgdisplay.impl.charlcd.components;

import com.ibasco.ucgdisplay.core.DisplayNode;
import com.ibasco.ucgdisplay.core.beans.ObservableList;
import com.ibasco.ucgdisplay.core.ui.CharGraphics;
import com.ibasco.ucgdisplay.core.ui.components.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LcdPane extends Pane<CharGraphics> {

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
}
