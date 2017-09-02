package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.EventDispatcher;

@FunctionalInterface
public interface Display<T extends Graphics> {
    void draw(T graphics);

    default void redraw() {
        EventDispatcher.dispatch(new DisplayEvent(DisplayEvent.DISPLAY_DRAW, this));
    }
}
