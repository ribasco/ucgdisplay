package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayComponent;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.beans.ChangeListener;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.events.EventHandler;
import com.ibasco.pidisplay.core.events.ValueChangeEvent;
import com.ibasco.pidisplay.core.util.RegexTextProcessor;
import com.ibasco.pidisplay.core.util.date.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.CharBuffer;
import java.util.Arrays;

@SuppressWarnings("all")
abstract public class DisplayText<T extends Graphics> extends DisplayComponent<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayText.class);

    protected static final int DEFAULT_WIDTH = -1;

    protected static final int DEFAULT_HEIGHT = 1;

    //region Properties
    protected ObservableProperty<String> text = new ObservableProperty<>(StringUtils.EMPTY);

    protected ObservableProperty<TextAlignment> textAlignment = new ObservableProperty<>(TextAlignment.LEFT);

    protected ObservableProperty<RegexTextProcessor> textProcessor = new ObservableProperty<>(new RegexTextProcessor());

    protected ObservableProperty<String> abbreviateChar = new ObservableProperty<>(StringUtils.EMPTY);
    //endregion

    //region Event Handlers
    private EventHandler<ValueChangeEvent<String>> onTextChange;
    //endregion

    //region Constructor
    protected DisplayText() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, StringUtils.EMPTY);
    }

    protected DisplayText(String text) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, text);
    }

    protected DisplayText(int width, int height, String text) {
        this(DEFAULT_XPOS, DEFAULT_YPOS, width, height, text);
    }

    protected DisplayText(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        redrawOnChange(this.text, textAlignment, abbreviateChar);
        this.maxHeight.set(calculateHeight(text), false);
        this.text.set(text, false);
        this.textProcessor.get().register("date", DateTimeUtils::formatCurrentDateTime);
    }
    //endregion

    private int calculateHeight(String text) {
        if (StringUtils.isEmpty(text) || text.length() <= maxWidth.get())
            return 1;
        int rem = ((text.length() % maxWidth.get()) > 0) ? 1 : 0;
        return (text.length() / maxWidth.get()) + rem;
    }

    //region Property Getters/Setters
    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public void setText(String text, Object... args) {
        this.text.set(String.format(text, args));
    }

    public TextAlignment getTextAlignment() {
        return textAlignment.get();
    }

    public void setTextAlignment(TextAlignment textAlignment) {
        this.textAlignment.set(textAlignment);
    }

    public RegexTextProcessor getTextProcessor() {
        return textProcessor.get();
    }

    public void setTextProcessor(RegexTextProcessor textProcessor) {
        this.textProcessor.set(textProcessor);
    }
    //endregion

    public void clear() {
        this.text.set(StringUtils.repeat(" ", getWidth() * getHeight()));
    }

    public EventHandler<ValueChangeEvent<String>> getOnTextChange() {
        return onTextChange;
    }

    public void setOnTextChange(ChangeListener<String> onTextChange) {
        this.text.addListener(onTextChange);
    }

    @Override
    public void draw(T graphics) {
        super.draw(graphics);

        String text = this.text.get(StringUtils.EMPTY);

        if (StringUtils.isEmpty(text))
            return;

        int startCol = getX();
        int startRow = getY();
        int maxDisplayWidth = graphics.getWidth();
        int maxDisplayHeight = graphics.getHeight();
        int textWidth = (!width.isSet()) ? maxDisplayWidth : getWidth();
        int textHeight = (!height.isSet()) ? maxDisplayHeight : getHeight();
        int maxAllowableChars = textWidth * textHeight;

        //Process text
        text = processText(text, maxAllowableChars);

        //Set start position
        graphics.setCursor(startCol, startRow);

        int colOffset = 0, rowOffset = startRow;

        char[] tmpBuffer = new char[textWidth];
        resetBuffer(tmpBuffer);

        CharBuffer cBuffer = CharBuffer.wrap(text);
        while (cBuffer.hasRemaining()) {
            //Do not continue if we have reached the maximum allowed characters
            if (cBuffer.position() == maxAllowableChars)
                break;

            tmpBuffer[colOffset++] = cBuffer.get();
            if (tmpBuffer[colOffset - 1] == '\n')
                colOffset = textWidth;

            if (colOffset > (textWidth - 1) || (cBuffer.remaining() == 0)) {
                drawText(new String(tmpBuffer), colOffset, rowOffset, textWidth, graphics);
                if (++rowOffset <= (maxDisplayHeight - 1))
                    graphics.setCursor(startCol, rowOffset);
                colOffset = 0;
                resetBuffer(tmpBuffer);
            }
        }
    }

    protected void drawText(String text, int colOffset, int rowOffset, int maxWidth, T graphics) {
        text = alignText(text.trim(), textAlignment.get(), maxWidth);
        graphics.drawText(text);
    }

    private void resetBuffer(char[] buffer) {
        Arrays.fill(buffer, ' ');
    }

    protected String processText(String text, int maxAllowableChars) {
        String tmp = text;
        if (textProcessor.isSet())
            text = textProcessor.get().process(tmp);
        tmp = truncateText(tmp, abbreviateChar.get(), maxAllowableChars);
        //tmp = alignText(tmp, textAlignment.get(), getWidth());
        return tmp;
    }


    protected String truncateText(String text, String truncStr, int maxWidth) {
        return StringUtils.abbreviate(text, truncStr, maxWidth);
    }

    protected String alignText(String text, TextAlignment alignment, int maxWidth) {
        //log.debug("Align Text: {}, Alignment: {}, Max Width: {}", text, alignment, maxWidth);
        if (TextAlignment.LEFT.equals(alignment))
            return StringUtils.rightPad(text, maxWidth);
        else if (TextAlignment.RIGHT.equals(alignment))
            return StringUtils.leftPad(text, maxWidth);
        else if (TextAlignment.CENTER.equals(alignment))
            return StringUtils.center(text, maxWidth);
        return text;
    }


}
