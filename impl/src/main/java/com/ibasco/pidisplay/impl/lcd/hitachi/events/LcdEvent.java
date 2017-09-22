package com.ibasco.pidisplay.impl.lcd.hitachi.events;

import com.ibasco.pidisplay.core.Display;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventType;
import com.ibasco.pidisplay.impl.lcd.hitachi.LcdGraphics;

public class LcdEvent extends DisplayEvent<LcdGraphics> {

    public LcdEvent(EventType<? extends Event> eventType, Display<LcdGraphics> display) {
        super(eventType, display);
    }
}
