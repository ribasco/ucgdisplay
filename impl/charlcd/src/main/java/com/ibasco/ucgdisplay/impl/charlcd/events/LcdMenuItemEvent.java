package com.ibasco.ucgdisplay.impl.charlcd.events;

import com.ibasco.ucgdisplay.core.DisplayNode;
import com.ibasco.ucgdisplay.core.Event;
import com.ibasco.ucgdisplay.core.EventType;
import com.ibasco.ucgdisplay.core.util.Node;
import com.ibasco.ucgdisplay.impl.charlcd.LcdCharGraphics;

import java.util.Objects;

public class LcdMenuItemEvent extends LcdEvent {

    public static final EventType<LcdMenuItemEvent> LCD_ITEM_SELECTED = new EventType<>("LCD_ITEM_SELECTED");

    public static final EventType<LcdMenuItemEvent> LCD_ITEM_FOCUS = new EventType<>("LCD_ITEM_FOCUS");

    private Node<String> selectedItem;

    private int selectedIndex;

    public LcdMenuItemEvent(EventType<? extends Event> eventType, Node<String> selectedItem, int selectedIndex, DisplayNode<LcdCharGraphics> display) {
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
