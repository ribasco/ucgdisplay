package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.beans.PropertyChangeListener;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.enums.TextWrapStyle;
import com.ibasco.pidisplay.core.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
abstract public class DisplayText<T extends Graphics> extends DisplayNode<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayText.class);

    //region Properties
    protected ObservableProperty<String> text = new ObservableProperty<>(StringUtils.EMPTY);

    protected ObservableProperty<TextAlignment> textAlignment = new ObservableProperty<>(TextAlignment.LEFT);

    protected ObservableProperty<Integer> startIndex = new ObservableProperty<>(0);

    protected ObservableProperty<TextWrapStyle> textWrapStyle = new ObservableProperty<>(TextWrapStyle.CONTINUOUS);

    protected List<String> lines = new ArrayList<>();
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

    protected DisplayText(Integer left, Integer top, Integer width, Integer height, String text) {
        super(left, top, width, height);
        this.text.setValid(text);
    }
    //endregion

    //region Property Getters/Setters
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
        if (!this.width.isSet())
            this.width.setValid(text.length());
        text = (args == null || args.length == 0) ? text : String.format(text, args);
        this.text.set(text);
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

    @Override
    protected List<ObservableProperty> getChangeListeners() {
        List<ObservableProperty> changeListeners = super.getChangeListeners();
        changeListeners.add(this.text);
        changeListeners.add(this.textAlignment);
        changeListeners.add(this.textWrapStyle);
        changeListeners.add(this.startIndex);
        return changeListeners;
    }

    /**
     * Clears the text property
     */
    public void clear() {
        this.text.set(StringUtils.EMPTY);
    }

    /**
     * @return Returns the total number of lines in the text
     */
    public int getLineCount() {
        return lines.size();
    }

    /**
     * @return Returns the total number of words detected within the text
     */
    public int wordCount() {
        return TextUtils.countWords(this.text.get());
    }

    /**
     * Refresh the line buffer contents. This will initially wrap the text based on the width of this component, split
     * it into an array of lines (using carraige return character) and store them in an internal list.
     *
     * @param text
     *         The {@link String} to process
     */
    protected void refreshLines(String text) {
        int width = this.width.get();
        text = TextUtils.wrapText(text, width, textWrapStyle.get());
        lines = Arrays.asList(StringUtils.splitPreserveAllTokens(text, "\n"));
    }

    @Override
    public String toString() {
        return String.format("%s Text: %s", super.toString(), StringUtils.abbreviate(StringUtils.defaultIfBlank(text.getInvalid(), "N/A"), 20));
    }
}
