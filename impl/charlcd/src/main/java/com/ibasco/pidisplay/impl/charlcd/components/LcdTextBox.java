package com.ibasco.pidisplay.impl.charlcd.components;

import com.ibasco.pidisplay.core.CharGraphics;
import com.ibasco.pidisplay.core.components.DisplayTextBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
public class LcdTextBox extends DisplayTextBox<CharGraphics> {

    private static final Logger log = LoggerFactory.getLogger(LcdTextBox.class);

    private LcdText content;

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
    protected LcdText getContent() {
        if (this.content == null) {
            this.content = new LcdText(getLeftPos(), getTopPos(), getWidth(), getHeight(), "") {
                @Override
                protected void onInvalidatedText(String oldValue, String newValue) {
                    //LcdTextBox.this.onTextInvalidated(oldValue, newValue);
                }
            };
        }
        return this.content;
    }

    @Override
    protected void drawNode(CharGraphics graphics) {
        //log.debug("Drawing LcdTextBox (Text Cursor: x={}, y={})", graphics.getColOffset(), graphics.getRowOffset());
        //graphics.drawText(StringUtils.repeat('_', getWidth()));
    }
}
