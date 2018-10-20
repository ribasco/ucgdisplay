package com.ibasco.ucgdisplay.core.events;

import com.ibasco.ucgdisplay.core.DisplayNode;
import com.ibasco.ucgdisplay.core.Event;
import com.ibasco.ucgdisplay.core.EventType;
import com.ibasco.ucgdisplay.core.ui.Graphics;

public class ListViewEvent<T extends Graphics> extends DisplayEvent {

    public ListViewEvent(EventType<? extends Event> eventType, DisplayNode<T> display) {
        super(eventType, display);
    }
}
