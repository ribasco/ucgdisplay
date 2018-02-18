package com.ibasco.pidisplay.impl.charlcd.components;

import com.ibasco.pidisplay.core.ui.CharDefinitions;
import com.ibasco.pidisplay.core.ui.CharGraphics;
import com.ibasco.pidisplay.core.ui.components.ListView;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class LcdListView<X> extends ListView<CharGraphics, X> {

    private static final Logger log = LoggerFactory.getLogger(LcdListView.class);

    public LcdListView() {
        this(null);
    }

    public LcdListView(List<X> items) {
        super(items);
    }

    @Override
    protected void initialize(CharGraphics graphics) {
        log.info("Initialize {}", this);
        graphics.charManager().register(CharDefinitions.LEFT_ARROW);
        graphics.charManager().register(CharDefinitions.LEFT_ARROW);
        graphics.charManager().register(CharDefinitions.RIGHT_ARROW);
        graphics.charManager().register(CharDefinitions.UP_ARROW);
        graphics.charManager().register(CharDefinitions.DOWN_ARROW);
    }

    @Override
    protected void drawNode(CharGraphics graphics) {
        if (getItems() == null || getItems().size() == 0)
            return;

        //Initialize default dimensions
        int width = super.width.getDefault(graphics.getWidth());
        int height = super.height.getDefault(graphics.getHeight());
        int top = super.topPos.getDefault(0);
        int left = super.leftPos.getDefault(0);

        for (int i = top; i < getItems().size() && i < height; i++) {
            graphics.setCursor(left, i);
            graphics.drawChar(CharDefinitions.RIGHT_ARROW);
            drawListItem(getItems().get(i), graphics);
        }
    }

    private void drawListItem(X item, CharGraphics graphics) {
        String text = StringUtils.left(Objects.toString(item, ""), graphics.getWidth());
        graphics.drawText(text);
    }
}
