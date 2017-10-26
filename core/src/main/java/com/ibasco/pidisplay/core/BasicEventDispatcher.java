package com.ibasco.pidisplay.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides basic functionalities for the {@link EventDispatcher} interface
 */
@SuppressWarnings("ALL")
abstract public class BasicEventDispatcher implements EventDispatcher {

    public static final Logger log = LoggerFactory.getLogger(BasicEventDispatcher.class);

    private BasicEventDispatcher previousDispatcher;
    private BasicEventDispatcher nextDispatcher;

    @Override
    public Event dispatchEvent(Event event, final EventDispatchChain tail) {
        log.trace("Dispatching Pre-Event (capture): {}", event);
        event = dispatchEvent(event, EventDispatchType.CAPTURE);
        if (event.isConsumed()) {
            return null;
        }
        event = tail.dispatchEvent(event);
        if (event != null) {
            log.trace("Dispatching Post-Event (bubble): {}", event);
            event = dispatchEvent(event, EventDispatchType.BUBBLE);
            if (event.isConsumed()) {
                return null;
            }
        }
        return event;
    }

    public Event dispatchEvent(Event event, EventDispatchType dispatchType) {
        return event;
    }

    public final BasicEventDispatcher getPreviousDispatcher() {
        return previousDispatcher;
    }

    public final BasicEventDispatcher getNextDispatcher() {
        return nextDispatcher;
    }

    public final void insertNextDispatcher(final BasicEventDispatcher newDispatcher) {
        if (nextDispatcher != null)
            nextDispatcher.previousDispatcher = newDispatcher;
        newDispatcher.nextDispatcher = nextDispatcher;
        newDispatcher.previousDispatcher = this;
        nextDispatcher = newDispatcher;
    }
}
