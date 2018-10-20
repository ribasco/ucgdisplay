package com.ibasco.ucgdisplay.core;


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

    /**
     * Dispatches the event in capture and bubbling phases.
     *
     * @param event
     * @param tail
     *
     * @return
     */
    @Override
    public Event dispatchEvent(Event event, final EventDispatchChain tail) {
        log.trace("Dispatching Capturing Event: {}", event);
        event = dispatchEvent(event, EventDispatchPhase.CAPTURE);
        if (event.isConsumed()) {
            return null;
        }
        event = tail.dispatchEvent(event);
        if (event != null) {
            log.trace("Dispatching Bubbling Event: {}", event);
            event = dispatchEvent(event, EventDispatchPhase.BUBBLE);
            if (event.isConsumed()) {
                return null;
            }
        }
        return event;
    }

    public Event dispatchEvent(Event event, EventDispatchPhase dispatchType) {
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
