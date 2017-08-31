package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.beans.DisplayProperty;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.events.EventHandler;
import com.ibasco.pidisplay.core.events.ValueChangeEvent;
import com.ibasco.pidisplay.core.util.TextProcessor;
import org.apache.commons.lang3.StringUtils;

import java.nio.CharBuffer;

@SuppressWarnings("all")
abstract public class DisplayText<T extends Graphics> extends DisplayComponent<T> {

    protected static final int DEFAULT_WIDTH = 5;

    protected static final int DEFAULT_HEIGHT = 1;

    protected static final String DEFAULT_TRUNC_STR = "..";

    protected DisplayProperty<String> textProperty = new DisplayProperty<>(StringUtils.EMPTY);

    protected DisplayProperty<TextAlignment> textAlignment = new DisplayProperty<>(TextAlignment.LEFT);

    protected DisplayProperty<TextProcessor> textProcessor = new DisplayProperty<>(null);

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
        setText(text);
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

    public TextProcessor getTextProcessor() {
        return textProcessor.get();
    }

    public void setTextProcessor(TextProcessor textProcessor) {
        this.textProcessor.set(textProcessor);
    }

    public void clear() {
        this.textProperty.set(StringUtils.repeat(" ", getWidth()));
    }

    public EventHandler<ValueChangeEvent<String>> getOnTextChange() {
        return onTextChange;
    }

    public void setOnTextChange(EventHandler<? super ValueChangeEvent> onTextChange) {
        this.textProperty.addListener(onTextChange);
    }

    @Override
    public void draw(T graphics) {
        super.draw(graphics);

        if (StringUtils.isEmpty(textProperty.get()))
            return;

        String text = textProperty.get();
        if (textProcessor.isSet()) {
            text = textProcessor.get().process(text);
        }
        text = truncateText(text, DEFAULT_TRUNC_STR, graphics.getWidth());
        text = alignText(text, textAlignment.get(), graphics.getWidth());

        CharBuffer b = CharBuffer.wrap(text);

        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        graphics.setCursor(x, y);

        while (b.hasRemaining()) {
            byte c = (byte) b.get();
        }

        graphics.drawText(text);
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
