package com.ibasco.ucgdisplay.core;

/**
 * A chain of {@code EventDispatcher}s
 *
 * @author Rafael Ibasco
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
