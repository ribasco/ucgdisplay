package com.ibasco.pidisplay.impl.charlcd.events;

import com.ibasco.pidisplay.core.DisplayNode;
import com.ibasco.pidisplay.core.Event;
import com.ibasco.pidisplay.core.EventType;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.impl.charlcd.LcdCharGraphics;

public class LcdEvent extends DisplayEvent<LcdCharGraphics> {

    public LcdEvent(EventType<? extends Event> eventType, DisplayNode<LcdCharGraphics> display) {
        super(eventType, display);
    }
}
