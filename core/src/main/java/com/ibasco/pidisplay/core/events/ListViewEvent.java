package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.ui.Graphics;

public class ListViewEvent<T extends Graphics> extends DisplayEvent {
    public static final EventType<ListViewEvent> ANY = new EventType<>(ListViewEvent.ANY, "LV_ANY");

    public static final EventType<ListViewEvent> ITEM_SELECTED = new EventType<>(ANY, "LV_ITEM_SELECTED");

    public static final EventType<ListViewEvent> ITEM_DESELECTED = new EventType<>(ANY, "LV_ITEM_DESELECTED");

    public ListViewEvent(EventType<? extends Event> eventType, DisplayNode<T> display) {
        super(eventType, display);
    }
}
