package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.ui.Graphics;

public class ListViewEvent<T extends Graphics> extends DisplayEvent {

    public ListViewEvent(EventType<? extends Event> eventType, DisplayNode<T> display) {
        super(eventType, display);
    }
}
