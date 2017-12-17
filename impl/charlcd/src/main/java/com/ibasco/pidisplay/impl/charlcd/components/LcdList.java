package com.ibasco.pidisplay.impl.charlcd.components;

import com.ibasco.pidisplay.core.ui.CharGraphics;
import com.ibasco.pidisplay.core.ui.components.DisplayList;

import java.util.List;
import java.util.Objects;

public class LcdList<X> extends DisplayList<CharGraphics, X> {

    public LcdList(Integer width, Integer height) {
        super(width, height);
    }

    public LcdList(Integer width, Integer height, List<X> items) {
        super(width, height, items);
    }

    @Override
    protected void drawNode(CharGraphics graphics) {
        for (X item : getItems()) {
            graphics.drawText(Objects.toString(item, ""));
        }
    }
}
