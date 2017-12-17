package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.beans.PropertyChangeListener;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.enums.TextWrapStyle;
import com.ibasco.pidisplay.core.ui.Graphics;
import com.ibasco.pidisplay.core.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
abstract public class DisplayText<T extends Graphics> extends DisplayNode<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayText.class);

    //region Properties
    protected StringBuilder buffer = new StringBuilder();

    protected ObservableProperty<String> text = createProperty(true, StringUtils.EMPTY, this::onInvalidatedText);

    protected ObservableProperty<TextAlignment> textAlignment = createProperty(true, TextAlignment.LEFT);

    protected ObservableProperty<TextWrapStyle> textWrapStyle = createProperty(true, TextWrapStyle.CONTINUOUS);

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
        setText(text);
    }
    //endregion

    //region Property Getters/Setters
    public ObservableProperty<String> textProperty() {
        return text;
    }

    public ObservableProperty<TextAlignment> textAlignmentProperty() {
        return textAlignment;
    }

    public ObservableProperty<TextWrapStyle> textWrapStyleProperty() {
        return textWrapStyle;
    }

    public TextWrapStyle getTextWrapStyle() {
        return textWrapStyle.get();
    }

    public void setTextWrapStyle(TextWrapStyle textWrapStyle) {
        this.textWrapStyle.set(textWrapStyle);
    }

    public int getLength() {
        return Objects.toString(text.get(), "").length();
    }

    public String getText() {
        return text.get();
    }

    public void deleteText(int offset) {
        buffer.deleteCharAt(offset);
        updateText();
    }

    public void deleteText(int start, int end) {
        buffer.delete(start, end);
        updateText();
    }

    public void appendText(String text) {
        buffer.append(text);
        updateText();
    }

    public void insertText(int offset, String text) {
        buffer.insert(offset, text);
        updateText();
    }

    public void setText(String text) {
        setText(text, new Object[]{});
    }

    public void setText(String text, Object... args) {
        if (text == null) {
            this.text.set(null);
            return;
        }
        if (!this.width.isSet())
            this.width.setValid(text.length());
        text = (args == null || args.length == 0) ? text : String.format(text, args);
        buffer.setLength(0);
        buffer.append(text);
        this.text.set(buffer.toString());
    }

    private void updateText() {
        setText(buffer.toString());
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
        this.buffer.setLength(0);
        updateText();
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
    public int getWordCount() {
        return TextUtils.countWords(this.text.get());
    }

    /**
     * Refresh the line buffer contents. This will initially wrap the text based on the width of this component, split
     * it into an array of lines (using carraige return character) and store them in an internal list.
     *
     * @param text
     *         The {@link String} to process
     */
    protected void refreshLines(String text, int charWidth) {
        text = TextUtils.wrapText(text, charWidth, textWrapStyle.get());
        lines = Arrays.asList(StringUtils.splitPreserveAllTokens(text, "\n"));
    }

    /**
     * Called when text has been modified
     *
     * @param oldValue
     *         The old text value
     * @param newValue
     *         The new text value
     */
    protected void onInvalidatedText(String oldValue, String newValue) {
        //no implementation
    }

    @Override
    public String toString() {
        return String.format("%s Text: %s", super.toString(), StringUtils.abbreviate(StringUtils.defaultIfBlank(text.get(), "N/A"), 20));
    }
}
