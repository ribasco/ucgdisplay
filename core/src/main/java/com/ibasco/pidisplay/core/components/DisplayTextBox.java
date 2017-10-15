package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.InputListener;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.events.DisplayInputEvent;
import com.ibasco.pidisplay.core.events.RawInputEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
abstract public class DisplayTextBox<T extends Graphics>
        extends DisplayNode<T> implements InputListener {

    private static final Logger log = LoggerFactory.getLogger(DisplayTextBox.class);

    protected DisplayText<T> content;

    protected ObservableProperty<Boolean> blinkCursor = createProperty(false);

    protected DisplayTextBox(Integer width, Integer height) {
        this(null, null, width, height);
    }

    protected DisplayTextBox(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
        content = createContentContainer();
        add(content);
        addEventHandler(DisplayInputEvent.ENTER_FOCUS, this::onFocusEvent);
        addEventHandler(DisplayInputEvent.EXIT_FOCUS, this::onFocusEvent);
    }

    private void onFocusEvent(DisplayInputEvent e) {
        if (DisplayInputEvent.ENTER_FOCUS == e.getEventType()) {
            blinkCursor.set(true);
            addEventHandler(RawInputEvent.KEY_PRESS, this::onInputEvent);
        } else if (DisplayInputEvent.EXIT_FOCUS == e.getEventType()) {
            blinkCursor.set(false);
            removeEventHandler(RawInputEvent.KEY_PRESS, this::onInputEvent);
        }
    }

    private void onInputEvent(RawInputEvent inputEvent) {
        int code = inputEvent.getRawInputEventData().getCode().getCode();
        log.debug("Received Raw Input: {}", inputEvent.getRawInputEventData());
        this.appendText("X");
    }

    /**
     * The textbox uses a {@link DisplayText} implementation to store the content.
     *
     * @return The {@link DisplayText} implementation that will be used to store the content.
     */
    protected abstract DisplayText<T> createContentContainer();

    public void setText(String text) {
        content.setText(text);
    }

    public void appendText(String text) {
        if (StringUtils.isEmpty(text))
            return;
        content.setText(content.getText() + text);
    }

    public boolean isBlinkCursor() {
        return blinkCursor.get();
    }

    public void setBlinkCursor(boolean blinkCursor) {
        this.blinkCursor.set(blinkCursor);
    }

    public void setTextAlignment(TextAlignment textAlignment) {
        this.content.setTextAlignment(textAlignment);
    }

    public void clear() {
        this.content.clear();
    }

    @Override
    public void onInput() {
        log.debug("Received Input Data");
    }
}
