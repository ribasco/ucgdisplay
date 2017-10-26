package com.ibasco.pidisplay.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class EventHandlerManager extends BasicEventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(EventHandlerManager.class);

    private final Map<EventType<? extends Event>, EventHandlerChain<? extends Event>> eventHandlerMap;

    private final Object eventSource;

    public EventHandlerManager(final Object eventSource) {
        this.eventSource = eventSource;
        eventHandlerMap = new HashMap<>();
    }

    //region Registration Methods
    public final <T extends Event> void addEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler, EventDispatchType dispatchType) {
        if (dispatchType == null)
            throw new NullPointerException("Event dispatch type must not be null");
        EventHandlerChain<T> eventHandlerChain = (EventHandlerChain<T>) eventHandlerMap.computeIfAbsent(eventType, eventType12 -> new EventHandlerChain<>());
        eventHandlerChain.addEventHandler(eventHandler, dispatchType);
    }

    public final <T extends Event> void removeEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler) {
        EventHandlerChain<T> eventHandlerChain = (EventHandlerChain<T>) eventHandlerMap.get(eventType);
        if (eventHandlerChain != null)
            eventHandlerChain.removeEventHandler(eventHandler);
    }

    public final <T extends Event> void removeEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler, EventDispatchType dispatchType) {
        EventHandlerChain<T> eventHandlerChain = (EventHandlerChain<T>) eventHandlerMap.get(eventType);
        if (eventHandlerChain != null) {
            eventHandlerChain.removeEventHandler(eventHandler, dispatchType);
        }
    }

    public void clear() {
        eventHandlerMap.clear();
    }
    //endregion

    @Override
    public final Event dispatchEvent(Event event, EventDispatchType dispatchType) {
        EventType<? extends Event> eventType = event.getEventType();
        //iterate through the event parent type and dispatch the event
        while (eventType != null) {
            final EventHandlerChain<? extends Event> eventHandlerChain = eventHandlerMap.get(eventType);
            if (eventHandlerChain != null) {
                event = (event.getSource() != eventSource) ? event.copyEvent(eventSource, event.getTarget()) : event;
                eventHandlerChain.dispatchEvent(event, dispatchType);
            }
            eventType = eventType.getParentType();
        }
        return event;
    }

    protected Object getEventSource() {
        return eventSource;
    }

    @Override
    public String toString() {
        if (eventSource != null) {
            return this.getClass().getSimpleName() + "=" + eventSource.getClass().getSimpleName();
        }
        return this.toString();
    }
}
