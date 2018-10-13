package com.ibasco.pidisplay.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;

/**
 * For debugging/monitoring purposes only
 *
 * @author Rafael Ibasco
 */
public class EventQueueMonitor implements Runnable {

    public static final Logger log = LoggerFactory.getLogger(EventQueueMonitor.class);

    private final Deque<Event> eventQueue;

    private int threshold = 30;

    public EventQueueMonitor(final EventDispatchQueue eventDispatchQueue, int threshold) {
        this.eventQueue = eventDispatchQueue.getEventQueue();
        this.threshold = threshold;
    }

    @Override
    public void run() {
        if ((eventQueue.size() > 0) && ((eventQueue.size() % threshold) == 0)) {
            log.warn("EVENT_QUEUE_MONITOR => SIZE : {}", eventQueue.size());
        }
    }
}
