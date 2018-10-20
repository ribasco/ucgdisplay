package com.ibasco.ucgdisplay.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for firing {@link Event} instances
 *
 * @author Rafael Ibasco
 */
public final class EventUtil {

    public static final Logger log = LoggerFactory.getLogger(EventUtil.class);

    /**
     * Contains a map of {@link EventDispatchQueue} for each {@link Controller} instance. Where the integer key
     * represents the controller id (obtained via {@link Controller#getId()})
     */
    private static final Map<Integer, EventDispatchQueue> eventQueueMap = new HashMap<>();

    /**
     * Returns an {@link EventDispatchQueue} associated with the {@link Controller}. A new instance will be
     * created if it does not yet exist on the internal static map.
     *
     * <p>
     * <strong>Note:</strong>
     * Only one instance of {@link EventDispatchQueue} may exist for each {@link Controller} instance
     * </p>
     *
     * @param controller
     *         The {@link Controller} to be used for reference lookup
     *
     * @return An {@link EventDispatchQueue} retrieved from the underlying map.
     */
    static EventDispatchQueue getEventDispatchQueue(final Controller controller) {
        if (controller == null)
            throw new NullPointerException("Controller cannot be null");
        return eventQueueMap.computeIfAbsent(controller.getId(), integer -> {
            log.debug("EVENT_UTIL => Creating new event queue for '{}'", controller);
            return new EventDispatchQueue();
        });
    }

    /**
     * Fires an event to the specified {@link EventTarget}. Events will be propagated from the root (Controller) down to
     * the {@link EventTarget}.
     *
     * @param eventTarget
     *         The {@link EventTarget} representing the destination of the event
     * @param event
     *         The {@link Event} instance containing the details of the specific event
     */
    public static void fireEvent(EventTarget eventTarget, Event event) {
        //Fix event target
        if (event.getTarget() != eventTarget)
            event = event.copyEvent(event.getSource(), eventTarget);
        if (event.getTarget() == null)
            throw new NullPointerException("Event target must not be null");
        EventDispatchQueue dispatchQueue = eventTarget.getEventDispatchQueue();
        if (dispatchQueue == null) {
            log.warn("Unable to fire event '{}' for '{}'. No event dispatch queue returned from target.", event, eventTarget);
            return;
        }
        //throw new NullPointerException("Dispatch Queue must not be null for event target: " + eventTarget);
        dispatchQueue.postEvent(event);
    }
}
