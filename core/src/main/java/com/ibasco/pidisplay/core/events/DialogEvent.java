package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Graphics;

public class DialogEvent<T extends Graphics> extends DisplayEvent<T> {

    public static final EventType<DisplayEvent> DISPLAY_SHOW = new EventType<>("DISPLAY_SHOW");

    public DialogEvent(EventType<? extends Event> eventType, DisplayNode<T> display) {
        super(eventType, display);
    }
}
