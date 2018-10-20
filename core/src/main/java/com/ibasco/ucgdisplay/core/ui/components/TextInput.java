package com.ibasco.ucgdisplay.core.ui.components;

import com.ibasco.ucgdisplay.core.DisplayParent;
import com.ibasco.ucgdisplay.core.EventHandler;
import com.ibasco.ucgdisplay.core.beans.ObservableProperty;
import com.ibasco.ucgdisplay.core.enums.TextAlignment;
import com.ibasco.ucgdisplay.core.events.FocusEvent;
import com.ibasco.ucgdisplay.core.events.KeyEvent;
import com.ibasco.ucgdisplay.core.ui.Graphics;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
abstract public class TextInput<T extends Graphics> extends DisplayParent<T> {

    private static final Logger log = LoggerFactory.getLogger(TextInput.class);

    protected final EventHandler<KeyEvent> rawInputEventHandler = this::onKeyEvent;

    protected ObservableProperty<Integer> caretPos = createProperty(0);

    protected TextInput(Integer width, Integer height) {
        this(null, null, width, height);
    }

    protected TextInput(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
        add(getContent());
        setFocusable(true);
    }

    @Override
    protected void onFocusEnter(FocusEvent focusEvent) {
        if (!this.isFocusable())
            return;
        log.debug("FOCUS_ENTER => {}", this);
        setFocused(true);
        focusEvent.getGraphics().setCursorBlink(true);
        updateCaretPos();
        addEventHandler(KeyEvent.ANY, rawInputEventHandler, CAPTURE);
    }

    @Override
    protected void onFocusExit(FocusEvent focusEvent) {
        log.debug("FOCUS_EXIT => {}", this);
        setFocused(false);
        focusEvent.getGraphics().setCursorBlink(false);
        removeEventHandler(KeyEvent.ANY, rawInputEventHandler, CAPTURE);
    }

    public ObservableProperty<Integer> caretProperty() {
        return this.caretPos;
    }

    public void setCaret(int pos) {
        this.caretPos.set(fixCaretPos(pos));
        updateCaretPos();
    }

    public int getCaret() {
        return this.caretPos.get();
    }

    public int getCaretX() {
        return calcXOffset();
    }

    public int getCaretY() {
        return calcYOffset();
    }

    protected void onKeyEvent(KeyEvent keyEvent) {
        /*if (!isFocused())
            return;
        Character charCode = keyEvent.getCharCode();
        InputEventCode code = keyEvent.getInputEventCode();
        boolean capitalize = false;
        if (InputEventCode.KEY_DELETE == code || InputEventCode.KEY_BACKSPACE == code)
            deleteText();
        if (InputEventCode.KEY_LEFTSHIFT == code || InputEventCode.KEY_RIGHTSHIFT == code) {
            capitalize = true;
        }
        if (charCode != null) {
            String c = capitalize ? charCode.toString().toUpperCase() : charCode.toString();
            getContent().insertText(this.caretPos.get(), c);
        }
        updateCaretPos();*/
    }

    public void setText(String text) {
        getContent().setText(text);
        setCaret((text != null) ? getContent().getText().length() - 1 : 0);
    }

    public void insertText(String text) {
        insertText(this.caretPos.get(), text);
    }

    public void insertText(int offset, String text) {
        getContent().insertText(offset, text);
    }

    public void appendText(String text) {
        if (StringUtils.isEmpty(text))
            return;
        getContent().appendText(text);
        incrementCaret(text.length());
    }

    private void incrementCaret(int length) {
        int maxpos = (getWidth() * getHeight()) - 1;
        this.caretPos.set(((this.caretPos.get() + length) >= maxpos) ? maxpos : this.caretPos.get() + length);
        log.debug("Setting caret to : {} for length {} (max = {})", caretPos.get(), length, maxpos);
        updateCaretPos();
    }

    private void decrementCaret(int length) {
        caretPos.set(caretPos.get() - length);
        caretPos.set(fixCaretPos(caretPos.get()));
        updateCaretPos();
    }

    public void deleteText(int start, int end) {
        getContent().deleteText(start, end);
        setCaret(start);
    }

    public void deleteText() {
        deleteText(1);
    }

    public void deleteText(int length) {
        if (StringUtils.isEmpty(getContent().getText()) || getContent().getText().length() == 0)
            return;
        //fail fast
        if (getContent().getLength() < length)
            throw new StringIndexOutOfBoundsException();
        for (int i = 0; i < length; i++) {
            getContent().deleteText(this.caretPos.get());
            decrementCaret(1);
        }
    }

    public void setTextAlignment(TextAlignment textAlignment) {
        getContent().setTextAlignment(textAlignment);
    }

    public void clear() {
        getContent().clear();
    }

    abstract protected int fixCaretPos(int pos);

    abstract protected Text<T> getContent();

    abstract protected void updateCaretPos();

    abstract protected int calcXOffset();

    abstract protected int calcYOffset();

    @Override
    protected void postFlush(T graphics) {
        log.debug("POST_FLUSH = {}", this);
        if (isFocused()) {
            updateCaretPos();
        }
    }
}
