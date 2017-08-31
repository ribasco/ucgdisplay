package com.ibasco.pidisplay.core.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(EventDispatcher.class);

    private BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();

    private final Multimap<EventType, EventHandler> handlers = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    private AtomicBoolean done = new AtomicBoolean();

    private AtomicBoolean started = new AtomicBoolean(false);

    public static final String THREAD_NAME = "pi-event-dispatcher";

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("pi-events");

    private Thread dispatchThread;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    protected final Lock readLock = readWriteLock.readLock();
    protected final Lock writeLock = readWriteLock.writeLock();

    private static class Dispatcher {
        private static final EventDispatcher INSTANCE = new EventDispatcher();

        static {
            log.info("Auto-starting event dispatcher");
            INSTANCE.start();
        }
    }

    private EventDispatcher() {
    }

    public static <T extends Event> void addHandler(final EventType<T> eventType, final EventHandler<? super T> handler) {
        log.debug("Adding Event Handler for: {}", eventType.getName());
        Dispatcher.INSTANCE.add(eventType, handler);
    }

    public static <T extends Event> void removeHandler(final EventType<T> eventType, final EventHandler<? super T> handler) {
        log.debug("Removing Event Handler: {}", eventType);
        Dispatcher.INSTANCE.remove(eventType, handler);
    }

    public static <T extends Event> void dispatch(T event) {
        Dispatcher.INSTANCE.eventQueue.add(event);
    }

    private <T extends Event> void add(final EventType<T> eventType, final EventHandler<? super T> handler) {
        writeLock.lock();
        try {
            handlers.put(eventType, handler);
        } finally {
            writeLock.unlock();
        }
    }

    private <T extends Event> void remove(final EventType<T> eventType, final EventHandler<? super T> handler) {
        writeLock.lock();
        try {
            handlers.remove(eventType, handler);
        } finally {
            this.writeLock.unlock();
        }
    }

    private void start() {
        if (started.get())
            throw new RuntimeException("Dispatcher already started");
        dispatchThread = new Thread(THREAD_GROUP, this::processEvents, THREAD_NAME);
        dispatchThread.start();
        log.debug("Event Dispatcher Started");
    }

    public static boolean isEventThread() {
        return Dispatcher.INSTANCE.dispatchThread == Thread.currentThread();
    }

    public static void checkEventDispatchThread() {
        if (!isEventThread())
            throw new IllegalStateException("Not currently in event dispatcher thread");
    }

    public static void shutdown() {
        log.debug("Shutting down event dispatcher");
        EventDispatcher dispatcher = Dispatcher.INSTANCE;
        dispatcher.done.set(true);
        dispatcher.started.set(false);
        dispatcher.dispatchThread.interrupt();
        dispatcher.dispatchThread = null;
    }

    public static EventDispatcher getInstance() {
        return Dispatcher.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private void processEvents() {
        log.debug("Processing of Events Started");
        while (!done.get()) {
            try {
                Event event = eventQueue.take();
                log.debug("EVENT FIRED: {}", event.getEventType());
                Collection<EventHandler> handlers = this.handlers.get(event.getEventType());
                this.readLock.lock();
                try {
                    handlers.forEach(handler -> handler.handle(event));
                } finally {
                    this.readLock.unlock();
                }
            } catch (InterruptedException e) {
                log.debug("Dispatcher Interrupted");
            } catch (Throwable e) {
                log.error("Error: ", e);
            }
        }
        log.debug("Exiting dispatcher");
    }
}
