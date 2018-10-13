package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventTarget;
import com.ibasco.pidisplay.core.EventType;

public class ListViewItemEvent extends Event {

    public static final EventType<ListViewItemEvent> ANY = new EventType<>(Event.ANY, "LV_ITEM_ANY");

    public static final EventType<ListViewItemEvent> ITEM_SELECTED = new EventType<>(ANY, "LV_ITEM_SELECTED");

    public static final EventType<ListViewItemEvent> ITEM_DESELECTED = new EventType<>(ANY, "LV_ITEM_DESELECTED");

    public static final EventType<ListViewItemEvent> ITEM_ENTER_FOCUS = new EventType<>(ANY, "LV_ITEM_ENTER_FOCUS");

    public static final EventType<ListViewItemEvent> ITEM_EXIT_FOCUS = new EventType<>(ANY, "LV_ITEM_EXIT_FOCUS");

    private int index;

    public ListViewItemEvent(EventType<? extends Event> eventType, int realIndex) {
        this(null, null, eventType, realIndex);
    }

    public ListViewItemEvent(Object source, EventTarget target, EventType<? extends Event> eventType, int realIndex) {
        super(source, target, eventType);
        this.index = realIndex;
    }

    public int getIndex() {
        return index;
    }
}
