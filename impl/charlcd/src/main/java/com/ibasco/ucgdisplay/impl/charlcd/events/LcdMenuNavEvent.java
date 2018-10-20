package com.ibasco.ucgdisplay.impl.charlcd.events;

import com.ibasco.ucgdisplay.core.DisplayNode;
import com.ibasco.ucgdisplay.core.Event;
import com.ibasco.ucgdisplay.core.EventType;
import com.ibasco.ucgdisplay.core.util.Node;
import com.ibasco.ucgdisplay.impl.charlcd.LcdCharGraphics;

public class LcdMenuNavEvent extends LcdEvent {
    public static final EventType<LcdMenuNavEvent> LCD_NAV_NEXT = new EventType<>("LCD_NAV_NEXT");

    public static final EventType<LcdMenuNavEvent> LCD_NAV_PREVIOUS = new EventType<>("LCD_NAV_PREVIOUS");

    public static final EventType<LcdMenuNavEvent> LCD_NAV_ENTER = new EventType<>("LCD_NAV_ENTER");

    public static final EventType<LcdMenuNavEvent> LCD_NAV_EXIT = new EventType<>("LCD_NAV_EXIT");

    private Node selectedNode;

    public LcdMenuNavEvent(EventType<? extends Event> eventType, Node selectedNode, DisplayNode<LcdCharGraphics> display) {
        super(eventType, display);
        this.selectedNode = selectedNode;
    }

    public Node getSelectedNode() {
        return selectedNode;
    }
}
