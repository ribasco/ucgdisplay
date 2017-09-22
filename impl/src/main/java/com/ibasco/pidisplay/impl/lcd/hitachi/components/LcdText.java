package com.ibasco.pidisplay.impl.lcd.hitachi.components;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.components.DisplayText;
import com.ibasco.pidisplay.core.util.RegexTextProcessor;
import com.ibasco.pidisplay.core.util.date.DateTimeUtils;
import com.ibasco.pidisplay.impl.lcd.hitachi.LcdGraphics;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Add the following text effects
 * 1. Scroll from left to right (speed=500ms)
 * 2. Scroll from right to left (speed=500ms)
 * 3. Blink (speed=1sec)
 * 4. Typewriter Effect (
 */
public class LcdText extends DisplayText<LcdGraphics> {

    private static final Logger log = LoggerFactory.getLogger(LcdText.class);

    private ObservableProperty<RegexTextProcessor> textProcessor = new ObservableProperty<>(new RegexTextProcessor());

    //region Constructor
    public LcdText() {
        this(StringUtils.EMPTY);
    }

    public LcdText(String text) {
        this(null, null, text);
    }

    public LcdText(Integer x, Integer y, String text) {
        this(x, y, null, null, text);
    }

    public LcdText(Integer x, Integer y, Integer width, Integer height, String text) {
        super(x, y, width, height, text);
        this.textProcessor.get().register("date", DateTimeUtils::formatCurrentDateTime);
    }
    //endregion

    @Override
    protected void drawNode(LcdGraphics graphics) {
        //Calculate default dimensions
        calcPrefDimen(graphics);

        int startCol = this.x.get();
        int startRow = this.y.get();
        int width = this.width.get();
        int height = this.height.get();

        //If visibility state is false or text is empty,
        // fill all text area with spaces to hide the text
        if (!isVisible() || StringUtils.isEmpty(getText())) {
            int rowOffsetLimit = calcMaxRowOffset(startRow, height, graphics.getHeight());
            for (int rowOffset = startRow; rowOffset <= rowOffsetLimit; rowOffset++) {
                graphics.setCursor(startCol, rowOffset);
                drawText(StringUtils.repeat(StringUtils.SPACE, width), width, graphics);
            }
            return;
        }

        //Pre-process text
        String text = processText(this.text.get(), width);
        refreshLines(text);

        //calculate row offset limit relative to the height
        int rowOffsetLimit = (startRow + height) - 1;
        if (rowOffsetLimit > (graphics.getHeight() - 1))
            rowOffsetLimit = graphics.getHeight() - 1;

        graphics.setCursor(startCol, startRow);

        int topOffset = this.scrollTop.getDefault(0);
        int leftOffset = this.scrollLeft.getDefault(0);
        int rowOffset = startRow;

        for (int i = 0, lineIdx = topOffset; (i < height) && (lineIdx < lines.size()); i++, lineIdx = i + topOffset) {
            if (i < lines.size()) {
                String line = lines.get(lineIdx);
                if ((leftOffset > 0) && (leftOffset < line.length()))
                    line = line.substring(leftOffset);
                drawText(line, width, graphics);
            }
            if (++rowOffset <= rowOffsetLimit)
                graphics.setCursor(startCol, rowOffset);
        }
    }

    private int calcMaxRowOffset(int startRow, int height, int maxDisplayHeight) {
        int rowOffsetLimit = startRow + (height - 1);
        if (rowOffsetLimit > (maxDisplayHeight - 1))
            rowOffsetLimit = maxDisplayHeight - 1;
        return rowOffsetLimit;
    }

    /**
     * Calculates the actual height for the current text
     *
     * @return The actual height calculated for the given text
     */
    private int calculateActualHeight(int maxWidth) {
        if (StringUtils.isEmpty(text.get()) || text.get().length() <= maxWidth)
            return 1;
        int rem = ((text.get().length() % maxWidth) > 0) ? 1 : 0;
        return (text.get().length() / maxWidth) + rem;
    }

    /**
     * Calculates the preferred dimensions
     *
     * @param graphics
     *         The underlying {@link LcdGraphics} driver
     */
    private void calcPrefDimen(LcdGraphics graphics) {
        int maxDisplayWidth = graphics.getWidth();
        int maxDisplayHeight = graphics.getHeight();
        int textLength = this.text.getDefault(StringUtils.EMPTY).length();

        Integer x = this.x.getDefault(0);
        Integer y = this.y.getDefault(0);
        Integer maxWidth = this.maxWidth.getDefault(maxDisplayWidth);
        Integer maxHeight = this.maxHeight.getDefault(maxDisplayHeight);
        Integer width = this.width.getDefault(textLength);
        Integer height = this.height.getDefault(calculateActualHeight(maxWidth));
        Integer minWidth = this.minWidth.getDefault((textLength < maxWidth) ? textLength : 1);
        Integer minHeight = this.minHeight.getDefault(1);

        if (maxWidth > maxDisplayWidth)
            maxWidth = maxDisplayWidth;
        if (maxHeight > maxDisplayHeight)
            maxHeight = maxDisplayHeight;
        if (minWidth > maxWidth)
            minWidth = 1;
        if (minHeight > maxHeight)
            minHeight = 1;
        if (width > maxWidth)
            width = maxWidth;
        if (height > maxHeight)
            height = maxHeight;
        if (width < minWidth)
            width = minWidth;
        if (height < minHeight)
            height = minHeight;

        this.x.setValid(x);
        this.y.setValid(y);
        this.maxWidth.setValid(maxWidth);
        this.maxHeight.setValid(maxHeight);
        this.minWidth.setValid(minWidth);
        this.minHeight.setValid(minHeight);
        this.width.setValid(width);
        this.height.setValid(height);
    }

    private String processText(String text, int maxCharWidth) {
        //Step #1: Extract then process text for variable substitution (if applicable)
        if (!StringUtils.isBlank(text) && textProcessor.isSet())
            text = textProcessor.get().process(text);
        return text;
    }

    private void drawText(String text, int maxWidth, LcdGraphics graphics) {
        //Only align text if the property is set and if the node is
        //currently in visible state
        if (textAlignment.isSet() && isVisible())
            text = alignText(text.trim(), textAlignment.get(), maxWidth);
        graphics.drawText(text);
    }
}
