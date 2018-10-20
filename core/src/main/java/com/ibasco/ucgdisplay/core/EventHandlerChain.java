package com.ibasco.ucgdisplay.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a chain of {@link EventHandler} instances
 *
 * @param <T>
 *         {@link Event} class
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings("ALL")
public class EventHandlerChain<T extends Event> {

    private static final Logger log = LoggerFactory.getLogger(EventHandlerChain.class);

    private SetMultimap<EventDispatchPhase, EventHandler<? super T>> eventHandlers = HashMultimap.create();

    //region Event Handler Registration
    public void addEventHandler(final EventHandler<? super T> eventHandler) {
        addEventHandler(eventHandler, EventDispatchPhase.CAPTURE);
        addEventHandler(eventHandler, EventDispatchPhase.BUBBLE);
    }

    public void addEventHandler(final EventHandler<? super T> eventHandler, EventDispatchPhase dispatchType) {
        eventHandlers.put(dispatchType, eventHandler);
    }

    public void removeEventHandler(final EventHandler<? super T> eventHandler, EventDispatchPhase dispatchType) {
        eventHandlers.remove(dispatchType, eventHandler);
    }

    public void removeEventHandler(final EventHandler<? super T> eventHandler) {
        eventHandlers.entries().removeIf(p -> p.getValue() == eventHandler);
    }
    //endregion

    /**
     * Dispatches the {@link Event} to the underlying {@link EventHandler}s
     *
     * @param event
     * @param dispatchType
     */
    public void dispatchEvent(final Event event, EventDispatchPhase dispatchType) {
        final T evt = (T) event;
        for (EventHandler<? super T> handler : eventHandlers.get(dispatchType)) {
            //Removed garbage-colected handlers
            if (handler instanceof WeakEventHandler && ((WeakEventHandler) handler).isRemoved()) {
                log.debug("EVENT_HANDLER_CHAIN => Weak Event Handler Removed");
                eventHandlers.remove(dispatchType, handler);
            } else
                handler.handle(evt);
        }
        if (EventDispatchPhase.POST_DISPATCH == dispatchType && eventHandlers.containsKey(dispatchType))
            eventHandlers.get(dispatchType).forEach(h -> h.handle(evt));
    }
}
