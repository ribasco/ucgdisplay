package com.ibasco.pidisplay.impl.charlcd.components;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.components.DisplayText;
import com.ibasco.pidisplay.core.util.RegexTextProcessor;
import com.ibasco.pidisplay.core.util.TextUtils;
import com.ibasco.pidisplay.core.util.date.DateTimeUtils;
import com.ibasco.pidisplay.impl.charlcd.CharGraphics;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * TODO: Add the following text effects
 * 1. Scroll from left to right (speed=500ms)
 * 2. Scroll from right to left (speed=500ms)
 * 3. Blink (speed=1sec)
 * 4. Typewriter Effect (
 */
public class LcdText extends DisplayText<CharGraphics> {

    private static final Logger log = LoggerFactory.getLogger(LcdText.class);

    private ObservableProperty<RegexTextProcessor> textProcessor = new ObservableProperty<>(new RegexTextProcessor());

    //region Constructor
    public LcdText() {
        this(StringUtils.EMPTY);
    }

    public LcdText(String text) {
        this(null, null, text);
    }

    public LcdText(Integer left, Integer top, String text) {
        this(left, top, null, null, text);
    }

    public LcdText(Integer left, Integer top, Integer width, Integer height, String text) {
        super(left, top, width, height, text);
        this.textProcessor.get().register("date", DateTimeUtils::formatCurrentDateTime);
    }
    //endregion

    @Override
    protected void drawNode(CharGraphics graphics) {
        //Calculate default dimensions
        calcPrefDimen(graphics);

        int startCol = this.leftPos.get();
        int startRow = this.topPos.get();
        int width = this.width.get();
        int height = this.height.get();

        //If visible = false or text empty, hide the node by filling the area with spaces.
        if (!isVisible() || StringUtils.isEmpty(getText())) {
            int rowOffsetLimit = calcMaxRowOffset(startRow, height, graphics.getHeight());
            for (int rowOffset = startRow; rowOffset <= rowOffsetLimit; rowOffset++) {
                graphics.setCursor(startCol, rowOffset);
                drawText(StringUtils.repeat(StringUtils.SPACE, width), width, graphics);
            }
            return;
        }

        //Pre-process text
        String text = processText(this.text.get());
        refreshLines(text);

        //calculate row offset limit relative to the height
        int rowOffsetLimit = (startRow + height) - 1;
        if (rowOffsetLimit > (graphics.getHeight() - 1))
            rowOffsetLimit = graphics.getHeight() - 1;

        //Set starting cursor
        graphics.setCursor(startCol, startRow);

        int topOffset = this.scrollTop.getDefault(0);
        int leftOffset = this.scrollLeft.getDefault(0);
        int rowOffset = startRow;
        int colOffset = 0;

        for (int lineCtr = 0, lineIdx = topOffset; (lineCtr < height) && (lineIdx < lines.size()); lineCtr++, lineIdx = lineCtr + topOffset) {
            if (lineCtr < lines.size()) {
                String line = lines.get(lineIdx);
                if ((leftOffset > 0) && (leftOffset < line.length()))
                    line = line.substring(leftOffset);
                colOffset += drawText(line, width, graphics) - 1;
            }
            //(rowOffset < (lines.size() - 1)
            if (++rowOffset <= rowOffsetLimit) {
                colOffset = 0;
                graphics.setCursor(startCol, rowOffset);
            }
        }
        //Set the last known cursor position
        //log.debug("Setting last known cursor: x={}, y={} (Graphics: x={}, y={})", colOffset, rowOffset - 1, graphics.getColOffset(), graphics.getRowOffset());

        graphics.setCursor(colOffset, ((rowOffset - 1) < 0) ? 0 : rowOffset - 1);
    }

    /**
     * Calculates the maximum allowable row offset relative to the height specified.
     *
     * @param startRow
     *         The starting row offset of this node
     * @param height
     *         The height property of this node
     * @param maxDisplayHeight
     *         The maximum height of the display
     *
     * @return The max row offset
     */
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
        String text = this.text.get();
        if (StringUtils.isEmpty(text) || text.length() <= maxWidth)
            return 1;
        int rem = ((text.length() % maxWidth) > 0) ? 1 : 0;
        return (text.length() / maxWidth) + rem;
    }

    /**
     * Calculates the preferred dimensions
     *
     * @param graphics
     *         The underlying {@link CharGraphics} driver
     */
    private void calcPrefDimen(CharGraphics graphics) {
        int maxDisplayWidth = graphics.getWidth();
        int maxDisplayHeight = graphics.getHeight();
        int textLength = defaultIfEmpty(this.text.get(), StringUtils.EMPTY).length();

        Integer x = defaultIfNull(this.leftPos.get(), 0);
        Integer y = defaultIfNull(this.topPos.get(), 0);
        Integer maxWidth = defaultIfNull(this.maxWidth.get(), maxDisplayWidth);
        Integer maxHeight = defaultIfNull(this.maxHeight.get(), maxDisplayHeight);
        Integer width = defaultIfNull(this.width.get(), textLength);
        Integer height = defaultIfNull(this.height.get(), calculateActualHeight(maxWidth));
        Integer minWidth = defaultIfNull(this.minWidth.get(), (textLength < maxWidth) ? textLength : 1);
        Integer minHeight = defaultIfNull(this.minHeight.get(), 1);

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

        this.leftPos.setValid(x);
        this.topPos.setValid(y);
        this.maxWidth.setValid(maxWidth);
        this.maxHeight.setValid(maxHeight);
        this.minWidth.setValid(minWidth);
        this.minHeight.setValid(minHeight);
        this.width.setValid(width);
        this.height.setValid(height);
    }

    private String processText(String text) {
        //Step #1: Extract then process text for variable substitution (if applicable)
        if (!StringUtils.isBlank(text) && textProcessor.isSet())
            text = textProcessor.get().process(text);
        return text;
    }

    private int drawText(String text, int maxWidth, CharGraphics graphics) {
        //Only align text if the property is set and if the node is
        //currently in visible state
        if (textAlignment.isSet() && isVisible())
            text = TextUtils.alignText(text.trim(), textAlignment.get(), maxWidth);
        graphics.drawText(text);
        return StringUtils.stripEnd(text, StringUtils.SPACE).length();
    }
}
