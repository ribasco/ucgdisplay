package com.ibasco.pidisplay.impl.lcd.hitachi.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventType;
import com.ibasco.pidisplay.core.util.Node;
import com.ibasco.pidisplay.impl.lcd.hitachi.LcdGraphics;

public class LcdMenuNavEvent extends LcdEvent {
    public static final EventType<LcdMenuNavEvent> LCD_NAV_NEXT = new EventType<>("LCD_NAV_NEXT");

    public static final EventType<LcdMenuNavEvent> LCD_NAV_PREVIOUS = new EventType<>("LCD_NAV_PREVIOUS");

    public static final EventType<LcdMenuNavEvent> LCD_NAV_ENTER = new EventType<>("LCD_NAV_ENTER");

    public static final EventType<LcdMenuNavEvent> LCD_NAV_EXIT = new EventType<>("LCD_NAV_EXIT");

    private Node selectedNode;

    public LcdMenuNavEvent(EventType<? extends Event> eventType, Node selectedNode, DisplayNode<LcdGraphics> display) {
        super(eventType, display);
        this.selectedNode = selectedNode;
    }

    public Node getSelectedNode() {
        return selectedNode;
    }
}
