package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.EventDispatcher;
import com.ibasco.pidisplay.core.events.InvocationEvent;

//TODO: Merge this to DisplayNode and rename DisplayNode to Display
@FunctionalInterface
public interface Display<T extends Graphics> {
    void draw(T graphics);

    default void redraw() {
        EventDispatcher.dispatch(new DisplayEvent<>(DisplayEvent.DISPLAY_DRAW, this));
    }

    static void invokeLater(Runnable runnable) {
        EventDispatcher.dispatch(new InvocationEvent(InvocationEvent.INVOKE_LATER, runnable));
    }
}
