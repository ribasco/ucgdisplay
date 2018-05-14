package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventTarget;
import com.ibasco.pidisplay.core.EventType;

public class ListViewItemEvent extends Event {

    public static final EventType<ListViewItemEvent> ITEM_SELECTED = new EventType<>(ANY, "LV_IE_SELECTED");

    public static final EventType<ListViewItemEvent> ITEM_FOCUSED = new EventType<>(ANY, "LV_IE_FOCUSED");

    private int index;

    public ListViewItemEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public ListViewItemEvent(Object source, EventTarget target, EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }

    public int getIndex() {
        return index;
    }
}
