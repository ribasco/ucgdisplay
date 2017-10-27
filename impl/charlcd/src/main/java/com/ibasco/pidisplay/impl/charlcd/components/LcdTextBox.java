package com.ibasco.pidisplay.impl.charlcd.components;

import com.ibasco.pidisplay.core.components.DisplayText;
import com.ibasco.pidisplay.core.components.DisplayTextBox;
import com.ibasco.pidisplay.impl.charlcd.LcdCharGraphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
public class LcdTextBox extends DisplayTextBox<LcdCharGraphics> {

    private static final Logger log = LoggerFactory.getLogger(LcdTextBox.class);

    public LcdTextBox() {
        this(null, null);
    }

    public LcdTextBox(Integer width, Integer height) {
        this(null, null, width, height);
    }

    public LcdTextBox(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }

    @Override
    protected DisplayText<LcdCharGraphics> createContent() {
        return new LcdText(0, 0, "Text Box") {
            @Override
            protected void drawNode(LcdCharGraphics graphics) {
                super.drawNode(graphics);
                if (blinkCursor.get()) {
                    graphics.cursorBlink(blinkCursor.get());
                }
            }
        };
    }

    @Override
    protected void drawNode(LcdCharGraphics graphics) {
        //log.debug("Drawing LcdTextBox (Text Cursor: x={}, y={})", graphics.getColOffset(), graphics.getRowOffset());
    }
}
