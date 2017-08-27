package com.ibasco.pidisplay.impl.lcd.hitachi.events;

import com.ibasco.pidisplay.core.Display;
import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventType;

public class LcdEvent extends DisplayEvent {

    public LcdEvent(EventType<? extends Event> eventType, Display<? extends Graphics> display) {
        super(eventType, display);
    }
}
