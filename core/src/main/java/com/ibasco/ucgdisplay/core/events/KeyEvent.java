package com.ibasco.ucgdisplay.core.events;

import com.ibasco.ucgdisplay.core.Event;
import com.ibasco.ucgdisplay.core.EventType;
import com.ibasco.ucgdisplay.core.system.RawInputEvent;

public class KeyEvent extends InputEvent {

    public static final EventType<KeyEvent> ANY = new EventType<>(InputEvent.ANY, "KEY_ANY");

    public static final EventType<KeyEvent> KEY_PRESSED = new EventType<>(ANY, "KEY_PRESSED");

    public static final EventType<KeyEvent> KEY_RELEASED = new EventType<>(ANY, "KEY_RELEASED");

    public static final EventType<KeyEvent> KEY_REPEAT = new EventType<>(ANY, "KEY_REPEAT");

    private boolean shiftDown;

    private boolean altDown;

    private boolean ctrlDown;

    public KeyEvent(EventType<? extends Event> eventType, RawInputEvent rawInputData) {
        this(eventType, rawInputData, false, false, false);
    }

    public KeyEvent(EventType<? extends Event> eventType, RawInputEvent rawInputData, boolean shiftDown, boolean altDown, boolean ctrlDown) {
        super(eventType, rawInputData);
        this.shiftDown = shiftDown;
        this.altDown = altDown;
        this.ctrlDown = ctrlDown;
    }

    public boolean isShiftDown() {
        return shiftDown;
    }

    public boolean isAltDown() {
        return altDown;
    }

    public boolean isCtrlDown() {
        return ctrlDown;
    }
}
