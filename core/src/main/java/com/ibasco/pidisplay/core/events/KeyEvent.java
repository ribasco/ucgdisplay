package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventTarget;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.enums.InputEventCode;

public class KeyEvent extends Event {

    public static final EventType<KeyEvent> ANY = new EventType<>(Event.ANY, "KEY_ANY");

    public static final EventType<KeyEvent> KEY_PRESS = new EventType<>(ANY, "KEY_PRESS");

    private InputEventCode inputEventCode;

    private Character charCode;

    public KeyEvent(EventType<? extends Event> eventType, InputEventCode inputEventCode, Character charCode) {
        this(null, null, eventType, inputEventCode, charCode);
    }

    public KeyEvent(Object source, EventTarget target, EventType<? extends Event> eventType, InputEventCode inputEventCode, Character charCode) {
        super(source, target, eventType);
        this.inputEventCode = inputEventCode;
        this.charCode = charCode;
    }

    public InputEventCode getInputEventCode() {
        return inputEventCode;
    }

    public Character getCharCode() {
        return charCode;
    }
}
