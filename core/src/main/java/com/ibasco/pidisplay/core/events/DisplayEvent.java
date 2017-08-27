package com.ibasco.pidisplay.core.events;

import com.ibasco.pidisplay.core.Display;
import com.ibasco.pidisplay.core.Graphics;

public class DisplayEvent extends Event {
    public static final EventType<DisplayEvent> DISPLAY_SHOW = new EventType<>("DISPLAY_SHOW");

    public static final EventType<DisplayEvent> DISPLAY_HIDE = new EventType<>("DISPLAY_HIDE");

    public static final EventType<DisplayEvent> DISPLAY_DRAW = new EventType<>("DISPLAY_DRAW");

    private Display display;

    public DisplayEvent(EventType<? extends Event> eventType, Display<? extends Graphics> display) {
        super(eventType);
        this.display = display;
    }

    public <T extends Graphics> Display<T> getDisplay() {
        return display;
    }
}
