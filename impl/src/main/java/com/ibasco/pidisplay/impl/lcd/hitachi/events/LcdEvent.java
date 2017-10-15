package com.ibasco.pidisplay.impl.lcd.hitachi.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.impl.lcd.hitachi.CharGraphics;

public class LcdEvent extends DisplayEvent<CharGraphics> {

    public LcdEvent(EventType<? extends Event> eventType, DisplayNode<CharGraphics> display) {
        super(eventType, display);
    }
}
