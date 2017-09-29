package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventHandler;
import com.ibasco.pidisplay.core.events.EventType;

import java.util.Collection;

/**
 * The Event Manager interface that is used to register/unregister {@link EventHandler} for specific {@link EventType}.
 *
 * @author Rafael Ibasco
 */
public interface EventManager {
    <T extends Event> void register(final EventType<T> eventType, final EventHandler<? super T> handler);

    <T extends Event> void unregister(final EventType<T> eventType, final EventHandler<? super T> handler);

    <T extends Event> Collection<EventHandler<? extends Event>> getHandler(EventType<T> eventType);
}
