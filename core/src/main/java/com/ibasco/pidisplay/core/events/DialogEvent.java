package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.Graphics;

public class DialogEvent<T extends Graphics, B> extends DisplayEvent<T> {

    public static final EventType<DialogEvent> ANY = new EventType<>(Event.ANY, "ANY");

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
