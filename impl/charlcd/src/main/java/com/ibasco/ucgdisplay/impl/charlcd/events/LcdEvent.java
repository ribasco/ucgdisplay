package com.ibasco.ucgdisplay.impl.charlcd.events;

import com.ibasco.ucgdisplay.core.DisplayNode;
import com.ibasco.ucgdisplay.core.Event;
import com.ibasco.ucgdisplay.core.EventType;
import com.ibasco.ucgdisplay.core.events.DisplayEvent;
import com.ibasco.ucgdisplay.impl.charlcd.LcdCharGraphics;

public class LcdEvent extends DisplayEvent<LcdCharGraphics> {
    public LcdEvent(EventType<? extends Event> eventType, DisplayNode<LcdCharGraphics> display) {
        super(eventType, display);
    }
}
