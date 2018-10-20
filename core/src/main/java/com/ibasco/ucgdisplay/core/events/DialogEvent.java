package com.ibasco.ucgdisplay.core.events;

import com.ibasco.ucgdisplay.core.DisplayNode;
import com.ibasco.ucgdisplay.core.Event;
import com.ibasco.ucgdisplay.core.EventType;
import com.ibasco.ucgdisplay.core.ui.Graphics;

public class DialogEvent<T extends Graphics, B> extends DisplayEvent<T> {

    public static final EventType<DialogEvent> ANY = new EventType<>(DisplayEvent.ANY, "ANY");

    public static final EventType<DialogEvent> DIALOG_RESULT = new EventType<>(ANY, "DIALOG_RESULT");

    private B result;

    public DialogEvent(EventType<? extends Event> eventType, DisplayNode<T> display, B result) {
        super(eventType, display);
        this.result = result;
    }

    public B getResult() {
        return result;
    }
}
