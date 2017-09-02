package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.Graphics;
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

    protected ObservableProperty<String> textProperty = new ObservableProperty<>(StringUtils.EMPTY);

    protected ObservableProperty<TextAlignment> textAlignment = new ObservableProperty<>(TextAlignment.LEFT);

    protected ObservableProperty<RegexTextProcessor> textProcessor = new ObservableProperty<>(new RegexTextProcessor());

    protected ObservableProperty<String> abbreviateChar = new ObservableProperty<>(StringUtils.EMPTY);

    private EventHandler<ValueChangeEvent<String>> onTextChange;

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
        textProperty.addListener(this::onPropertyChange);
        textAlignment.addListener(this::onPropertyChange);
        abbreviateChar.addListener(this::onPropertyChange);
        setText(text);
        textProcessor.get().register("date", DateTimeUtils::formatCurrentDateTime);
    }

    private void onPropertyChange(ValueChangeEvent valueChangeEvent) {
        this.redraw();
    }

    public String getText() {
        return textProperty.get();
    }

    public void setText(String text) {
        this.textProperty.set(text);
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

    public String getAbbreviateChar() {
        return abbreviateChar.get();
    }

    public void setAbbreviateChar(String abbreviateChar) {
        this.abbreviateChar.set(abbreviateChar);
    }

    public void clear() {
        this.textProperty.set(StringUtils.repeat(" ", getWidth() * getHeight()));
    }

    public EventHandler<ValueChangeEvent<String>> getOnTextChange() {
        return onTextChange;
    }

    public void setOnTextChange(EventHandler<? super ValueChangeEvent> onTextChange) {
        this.textProperty.addListener(onTextChange);
    }

    @Override
    protected void onVisibilityChange(ValueChangeEvent valueChangeEvent) {
        if ((Boolean) valueChangeEvent.getNewValue()) {
            log.debug("Visible: {}", valueChangeEvent.getNewValue());
        }
    }

    @Override
    public void draw(T graphics) {
        super.draw(graphics);

        if (!textProperty.isSet() || !isVisible())
            return;

        int startX = getX(), curX = startX;
        int startY = getY(), curY = startY;
        int maxDisplayWidth = graphics.getWidth(), maxDisplayHeight = graphics.getHeight();
        int textWidth = getWidth(), textHeight = getHeight();
        int maxAllowableChars = textWidth * textHeight;

        //Process text
        String text = textProperty.getDefault(StringUtils.EMPTY);
        text = processText(text, maxAllowableChars);

        CharBuffer b = CharBuffer.wrap(text);

        //Set cursor position
        graphics.setCursor(startX, startY);

        int idx = 0;
        char[] tmp = new char[textWidth];
        Arrays.fill(tmp, ' ');

        while (b.hasRemaining()) {
            tmp[idx++] = b.get();
            if (idx > (textWidth - 1)) {
                graphics.drawText(alignText(new String(tmp), textAlignment.get(), textWidth));
                idx = 0;
                if (++curY <= (maxDisplayHeight - 1))
                    graphics.setCursor(startX, curY);
            }
        }
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
        if (TextAlignment.LEFT.equals(alignment))
            text = StringUtils.rightPad(text, maxWidth);
        else if (TextAlignment.RIGHT.equals(alignment))
            text = StringUtils.leftPad(text, maxWidth);
        else if (TextAlignment.CENTER.equals(alignment))
            text = StringUtils.center(text, maxWidth);
        return text;
    }
}
