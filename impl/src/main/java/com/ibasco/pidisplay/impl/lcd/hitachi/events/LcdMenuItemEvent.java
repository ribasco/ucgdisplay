package com.ibasco.pidisplay.impl.lcd.hitachi.events;

import com.ibasco.pidisplay.core.Display;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventType;
import com.ibasco.pidisplay.core.util.Node;

import java.util.Objects;

public class LcdMenuItemEvent extends LcdEvent {

    public static final EventType<LcdMenuItemEvent> LCD_ITEM_SELECTED = new EventType<>("LCD_ITEM_SELECTED");

    public static final EventType<LcdMenuItemEvent> LCD_ITEM_FOCUS = new EventType<>("LCD_ITEM_FOCUS");

    private Node<String> selectedItem;

    private int selectedIndex;

    public LcdMenuItemEvent(EventType<? extends Event> eventType, Node<String> selectedItem, int selectedIndex, Display<? extends Graphics> display) {
        super(eventType, display);
        this.selectedItem = selectedItem;
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public Node<String> getSelectedItem() {
        return selectedItem;
    }

    @Override
    public String toString() {
        return Objects.toString(selectedItem, "N/A");
    }
}
