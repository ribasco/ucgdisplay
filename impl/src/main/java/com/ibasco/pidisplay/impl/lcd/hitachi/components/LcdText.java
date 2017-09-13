package com.ibasco.pidisplay.impl.lcd.hitachi.components;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.components.DisplayText;
import com.ibasco.pidisplay.core.util.RegexTextProcessor;
import com.ibasco.pidisplay.core.util.date.DateTimeUtils;
import com.ibasco.pidisplay.impl.lcd.hitachi.LcdGraphics;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * TODO: Add the following text effects
 * 1. Scroll from left to right (speed=500ms)
 * 2. Scroll from right to left (speed=500ms)
 * 3. Blink (speed=1sec)
 * 4. Typewriter Effect (
 */
@SuppressWarnings("WeakerAccess")
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
    public void clear() {
        this.text.set(StringUtils.repeat(" ", getMaxWidth() * getMaxHeight()));
    }

    public RegexTextProcessor getTextProcessor() {
        return textProcessor.get();
    }

    public void setTextProcessor(RegexTextProcessor textProcessor) {
        this.textProcessor.set(textProcessor);
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

    @Override
    public void drawNode(LcdGraphics graphics) {
        calculatePrefDimensions(graphics);

        int startCol = this.x.get();
        int startRow = this.y.get();
        int width = this.width.get();
        int height = this.height.get();
        int maxCharSize = width * height;

        //calculate row offset limit relative to the height specified
        int rowOffsetLimit = (startRow + height) - 1;
        if (rowOffsetLimit > (graphics.getHeight() - 1))
            rowOffsetLimit = graphics.getHeight() - 1;

        //Pre-process text before rendering
        String text = processText(this.text.get(), maxCharSize);

        //Set start position
        graphics.setCursor(startCol, startRow);

        int colOffset = 0, rowOffset = startRow;

        //Buffer which holds the current character set for the current row
        char[] tmp = new char[width];
        Arrays.fill(tmp, ' ');

        int startPos = 0;
        for (int pos = startPos; pos < text.length(); pos++) {
            if (pos == maxCharSize)
                break;

            tmp[colOffset++] = text.charAt(pos);

            //skip bytes and draw immediately
            if (tmp[colOffset - 1] == '\n')
                colOffset = tmp.length;

            int remaining = (text.length() - 1) - pos;

            //Start drawing once the tmp buffer is filled
            if ((colOffset > (tmp.length - 1)) || remaining <= 0) {
                drawText(new String(tmp), tmp.length, graphics);
                //do not adjust row offset if we have reached the
                // max row offset allowable for the current node
                if (++rowOffset <= rowOffsetLimit)
                    graphics.setCursor(startCol, rowOffset);
                colOffset = 0;
                Arrays.fill(tmp, ' ');
            }
        }
    }

    /**
     * Calculates the preferred dimensions
     *
     * @param graphics
     *         The underlying {@link LcdGraphics} driver
     */
    private void calculatePrefDimensions(LcdGraphics graphics) {
        int maxDisplayWidth = graphics.getWidth();
        int maxDisplayHeight = graphics.getHeight();

        Integer x = this.x.getDefault(0);
        Integer y = this.y.getDefault(0);
        Integer maxWidth = this.maxWidth.getDefault(maxDisplayWidth);
        Integer maxHeight = this.maxHeight.getDefault(maxDisplayHeight);
        Integer width = this.width.getDefault(this.text.get().length());
        Integer height = this.height.getDefault(calculateActualHeight(maxWidth));

        if (maxWidth > maxDisplayWidth)
            maxWidth = maxDisplayWidth;
        if (maxHeight > maxDisplayHeight)
            maxHeight = maxDisplayHeight;
        if (width > maxWidth)
            width = maxWidth;
        if (height > maxHeight)
            height = maxHeight;

        this.x.setValid(x);
        this.y.setValid(y);
        this.maxWidth.setValid(maxWidth);
        this.maxHeight.setValid(maxHeight);
        this.width.setValid(width);
        this.height.setValid(height);
    }

    private String processText(String text, int maxCharSize) {
        //Step #1: Extract then process text for variable substitution (if applicable)
        if (!StringUtils.isBlank(text) && textProcessor.isSet())
            text = textProcessor.get().process(text);

        //Step #2: If visibility state is false or text is empty,
        // fill all text area with spaces to hide the text
        if (!isVisible() || StringUtils.isEmpty(text))
            text = StringUtils.repeat(' ', maxCharSize);

        //Step #3: If a start index has been specified, extract
        // the substring from the start position specified
        if (!StringUtils.isBlank(text)) {
            int startIndex = this.startIndex.get();
            int endIndex = startIndex + maxCharSize;
            if (endIndex >= text.length())
                text = text.substring(startIndex);
            else
                text = text.substring(startIndex, endIndex);
        }

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
