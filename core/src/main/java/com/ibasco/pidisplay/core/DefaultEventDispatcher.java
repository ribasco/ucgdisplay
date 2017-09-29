package com.ibasco.pidisplay.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ibasco.pidisplay.core.events.Event;
import com.ibasco.pidisplay.core.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Simple {@link EventDispatcher} implementation
 *
 * @author Rafael Ibasco
 */
public class DefaultEventDispatcher implements EventDispatcher {

    private static final Logger log = LoggerFactory.getLogger(DefaultEventDispatcher.class);

    private static final String THREAD_NAME_FORMAT = "pi-display-edt-%d";

    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("pi-display");

    private AtomicBoolean started = new AtomicBoolean(false);

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();

    private ExecutorService dispatchExecService;

    private Thread eventDispatchThread;

    private EventManager eventManager;

    DefaultEventDispatcher(EventManager eventManager, ExecutorService executorService) {
        this.eventManager = Objects.requireNonNull(eventManager, "Event Manager cannot be null");
        if (executorService == null) {
            ThreadFactory factory = new ThreadFactoryBuilder()
                    .setDaemon(true)
                    .setThreadFactory(r -> {
                        Thread t = new Thread(THREAD_GROUP, r);
                        DefaultEventDispatcher.this.eventDispatchThread = t;
                        log.debug("Dispatch thread created : {}", t);
                        return t;
                    })
                    .setNameFormat(THREAD_NAME_FORMAT)
                    .setUncaughtExceptionHandler(this::uncaughtException)
                    .build();
            this.dispatchExecService = Executors.newSingleThreadExecutor(factory);
        } else
            this.dispatchExecService = executorService;
        start();
    }

    /**
     * Logs uncaught exceptions thrown from the Event Dispatch Thread
     *
     * @param t
     *         The {@link Thread} where the {@link Exception} was thrown
     * @param e
     *         The {@link Throwable} instance containing the details of the exception
     */
    private void uncaughtException(Thread t, Throwable e) {
        log.error("Uncaught Exception in Executor Service (Thread: {})", t.getName(), e.getMessage());
    }

    /**
     * Puts the {@link Event} into a queue to be later processed by the {@link DefaultEventDispatcher}
     *
     * @param event
     *         The {@link Event} instance to be dispatched
     * @param <T>
     *         The captured type of the {@link Event}
     */
    @Override
    public <T extends Event> void dispatch(T event) {
        log.debug("Dispatching Event: {}", event);
        this.eventQueue.add(event);
    }

    public void invokeLater(Runnable runnable) {

    }

    private void start() {
        if (started.get())
            throw new RuntimeException("Dispatcher already started");
        dispatchExecService.execute(this::dispatchEvents);
        log.debug("Event Dispatcher Started");
        started.set(true);
    }

    /**
     * @return Returns {@code true} if the current thread is the event dispatch thread
     */
    @Override
    public boolean isEventDispatchThread() {
        return this.eventDispatchThread == Thread.currentThread();
    }

    /**
     * Shutdown the underlying {@link ExecutorService}
     */
    @Override
    public void shutdown() {
        log.debug("Shutting down event dispatcher");
        this.shutdown.set(true);
        this.started.set(false);
        try {
            this.dispatchExecService.shutdownNow();
            this.dispatchExecService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("Shutdown interrupted", e);
        }
    }

    /**
     * Dispatch Events
     */
    private void dispatchEvents() {
        log.debug("Processing of Events Started");
        while (!shutdown.get()) {
            try {
                if (Thread.interrupted()) {
                    log.debug("Dispatch Thread Interrupted. Exiting");
                    break;
                }
                Event event = eventQueue.take();
                Collection<EventHandler<? extends Event>> handlers = eventManager.getHandler(event.getEventType());
                for (EventHandler handler : handlers) {
                    if (event.isConsumed()) {
                        log.debug("Event '{}' already consumed", event);
                        break;
                    }
                    //noinspection unchecked
                    handler.handle(event);
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
