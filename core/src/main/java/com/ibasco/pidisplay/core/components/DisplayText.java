package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.beans.PropertyChangeListener;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.enums.TextWrapStyle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class DisplayText<T extends Graphics> extends DisplayNode<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayText.class);

    //region Properties
    protected ObservableProperty<String> text = new ObservableProperty<>(StringUtils.EMPTY);

    protected ObservableProperty<TextAlignment> textAlignment = new ObservableProperty<>(TextAlignment.LEFT);

    protected ObservableProperty<Integer> startIndex = new ObservableProperty<>(0);

    protected ObservableProperty<TextWrapStyle> textWrapStyle = new ObservableProperty<>(TextWrapStyle.CONTINUOUS);

    protected ObservableProperty<Integer> scrollTop = new ObservableProperty<>(0);

    protected ObservableProperty<Integer> scrollLeft = new ObservableProperty<>(0);
    //endregion

    //region Constructor
    protected DisplayText() {
        this(StringUtils.EMPTY);
    }

    protected DisplayText(String text) {
        this(null, null, text);
    }

    protected DisplayText(Integer width, Integer height, String text) {
        this(null, null, width, height, text);
    }

    protected DisplayText(Integer x, Integer y, Integer width, Integer height, String text) {
        super(x, y, width, height);
        redrawOnChange(this.text, this.textAlignment, this.startIndex, this.scrollTop, this.scrollLeft);
        this.text.setValid(text);
    }
    //endregion

    //region Property Getters/Setters
    public Integer getScrollTop() {
        return scrollTop.get();
    }

    public void setScrollTop(Integer scrollTop) {
        this.scrollTop.set(scrollTop);
    }

    public Integer getScrollLeft() {
        return scrollLeft.get();
    }

    public void setScrollLeft(Integer scrollLeft) {
        this.scrollLeft.set(scrollLeft);
    }

    public TextWrapStyle getTextWrapStyle() {
        return textWrapStyle.get();
    }

    public void setTextWrapStyle(TextWrapStyle textWrapStyle) {
        this.textWrapStyle.set(textWrapStyle);
    }

    public Integer getStartIndex() {
        return startIndex.get();
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex.set(startIndex);
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        setText(text, new Object[]{});
    }

    public void setText(String text, Object... args) {
        if (!this.width.isSet()) {
            this.width.setValid(text.length());
        }
        this.text.set((args == null || args.length == 0) ? text : String.format(text, args));
    }

    public TextAlignment getTextAlignment() {
        return textAlignment.get();
    }

    public void setTextAlignment(TextAlignment textAlignment) {
        this.textAlignment.set(textAlignment);
    }

    //endregion

    //region Event Handler Getter/Setters
    public void setOnTextChange(PropertyChangeListener<String> onTextChange) {
        this.text.addListener(onTextChange);
    }
    //endregion

    /**
     * Clears the text property
     */
    public void clear() {
        this.text.set(StringUtils.repeat(" ", getMaxWidth() * getMaxHeight()));
    }

    protected String alignText(String text, TextAlignment alignment, int maxWidth) {
        if (TextAlignment.LEFT.equals(alignment))
            return StringUtils.rightPad(text, maxWidth);
        else if (TextAlignment.RIGHT.equals(alignment))
            return StringUtils.leftPad(text, maxWidth);
        else if (TextAlignment.CENTER.equals(alignment))
            return StringUtils.center(text, maxWidth);
        return text;
    }
}
