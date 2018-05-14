package com.ibasco.pidisplay.core;

/**
 * An {@code EventDispatcher} which represents a chain of event dispatchers, but
 * can still be set or replaced as a single entity.
 */
abstract public class MultiEventDispatcher extends BasicEventDispatcher {
    abstract public BasicEventDispatcher getFirstDispatcher();

    abstract public BasicEventDispatcher getLastDispatcher();

    @Override
    public Event dispatchEvent(Event event, EventDispatchPhase dispatchType) {
        BasicEventDispatcher childDispatcher = (EventDispatchPhase.CAPTURE == dispatchType) ? getFirstDispatcher() : getLastDispatcher();
        while (childDispatcher != null) {
            event = childDispatcher.dispatchEvent(event, dispatchType);
            if (event.isConsumed()) {
                break;
            }
            childDispatcher = (EventDispatchPhase.CAPTURE == dispatchType) ? childDispatcher.getNextDispatcher() : childDispatcher.getPreviousDispatcher();
        }
        return event;
    }
}
