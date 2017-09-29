package com.ibasco.pidisplay.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventHandler;
import com.ibasco.pidisplay.core.events.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A Basic implementation of the {@link EventManager} interface providing thread-safe access to events registration.
 *
 * @author Rafael Ibasco
 */
public class DefaultEventManager implements EventManager {
    private static final Logger log = LoggerFactory.getLogger(DefaultEventManager.class);

    private final Multimap<EventType, EventHandler<? extends Event>> handlers = HashMultimap.create();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    @Override
    public <T extends Event> void register(final EventType<T> eventType, final EventHandler<? super T> handler) {
        log.debug("Adding Event Handler for: {}", eventType.getName());
        try {
            writeLock.lock();
            handlers.put(eventType, handler);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public <T extends Event> void unregister(final EventType<T> eventType, final EventHandler<? super T> handler) {
        log.debug("Removing Event Handler: {}", eventType);
        try {
            writeLock.lock();
            handlers.remove(eventType, handler);
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public <T extends Event> Collection<EventHandler<? extends Event>> getHandler(EventType<T> eventType) {
        try {
            this.readLock.lock();
            return handlers.get(eventType);
        } finally {
            this.readLock.unlock();
        }
    }
}
