package com.ibasco.pidisplay.impl.lcd.hitachi.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventType;
import com.ibasco.pidisplay.impl.lcd.hitachi.LcdGraphics;

public class LcdEvent extends DisplayEvent<LcdGraphics> {

    public LcdEvent(EventType<? extends Event> eventType, DisplayNode<LcdGraphics> display) {
        super(eventType, display);
    }
}
