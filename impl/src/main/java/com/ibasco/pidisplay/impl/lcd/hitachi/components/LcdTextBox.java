package com.ibasco.pidisplay.impl.lcd.hitachi.components;

import com.ibasco.pidisplay.core.components.DisplayText;
import com.ibasco.pidisplay.core.components.DisplayTextBox;
import com.ibasco.pidisplay.impl.lcd.hitachi.CharGraphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
public class LcdTextBox extends DisplayTextBox<CharGraphics> {

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
    protected DisplayText<CharGraphics> createContentContainer() {
        return new LcdText(0, 0, "This is a test") {
            @Override
            protected void drawNode(CharGraphics graphics) {
                log.debug("Drawing Content");
                super.drawNode(graphics);
            }
        };
    }

    @Override
    protected void drawNode(CharGraphics graphics) {
        log.debug("Drawing LcdTextBox (Text Cursor: x={}, y={})", graphics.getColOffset(), graphics.getRowOffset());
    }
}
