package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.events.Event;

/**
 * The {@link EventDispatcher} interface
 *
 * @author Rafael Ibasco
 */
public interface EventDispatcher {
    <T extends Event> void dispatch(T event);

    void shutdown();

    boolean isEventDispatchThread();
}
