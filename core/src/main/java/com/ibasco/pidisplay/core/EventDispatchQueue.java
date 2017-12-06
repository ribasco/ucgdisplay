package com.ibasco.pidisplay.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ibasco.pidisplay.core.events.InvocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventDispatchQueue {

    public static final Logger log = LoggerFactory.getLogger(EventDispatchQueue.class);

    //region Private Fields
    private BlockingDeque<Event> queue = new LinkedBlockingDeque<>();

    private Set<InvocationRecord> runnables = new LinkedHashSet<>();

    private CountDownLatch shutdownLatch = new CountDownLatch(1);

    private Thread uiThread;

    private static final ThreadGroup threadGroup = new ThreadGroup("pid-events");

    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setDaemon(true)
            .setThreadFactory(r -> {
                uiThread = new Thread(threadGroup, r);
                return uiThread;
            })
            .setUncaughtExceptionHandler(this::exceptionHandler)
            .setNameFormat("pid-edt-%d")
            .build();

    private void exceptionHandler(Thread thread, Throwable throwable) {
        log.error("Error occured in Thread {} : ", thread);
        log.error(throwable.getMessage(), throwable);
    }

    private final ExecutorService dispatchService = Executors.newSingleThreadExecutor(threadFactory);

    private AtomicBoolean started = new AtomicBoolean(false);

    /**
     * Holds a thread-local instance for {@link EventDispatchChainRecord}. Only one record instance per thread may
     * exist.
     */
    private static final ThreadLocal<EventDispatchChainRecord> tlDispatchChainRecord = ThreadLocal.withInitial(EventDispatchChainRecord::new);

    /**
     * Contains information on registered {@link Runnable} instances to be invoked within the UI/Event
     * Loop thread.
     */
    private static class InvocationRecord {
        private boolean invokeOnce = false;
        private Runnable runnable;
        private boolean running = false;

        public InvocationRecord(Runnable runnable, boolean invokeOnce) {
            if (runnable == null)
                throw new NullPointerException("Runnable cannot be null");
            this.invokeOnce = invokeOnce;
            this.runnable = runnable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InvocationRecord that = (InvocationRecord) o;
            return runnable != null ? runnable.equals(that.runnable) : that.runnable == null;
        }

        @Override
        public int hashCode() {
            return runnable != null ? runnable.hashCode() : 0;
        }
    }

    /**
     * Holds a record of {@link EventDispatchChain} instance
     */
    private static class EventDispatchChainRecord {
        private final AtomicBoolean inUse = new AtomicBoolean(false);
        private final EventDispatchChainImpl dispatchChain = new EventDispatchChainImpl();

        /**
         * Tries to acquire an {@link EventDispatchChain} instance from the current record obtained from the
         * thread local map. If it is currently in-use, a new instance of {@link EventDispatchChain} will be returned.
         *
         * @return An {@link EventDispatchChain} instance.
         */
        private EventDispatchChain acquire() {
            if (inUse.getAndSet(true)) {
                EventDispatchChainImpl impl = new EventDispatchChainImpl();
                log.debug("EVENT_QUEUE => [{}] Dispatch chain in-use. Creating new instance (#{})", Thread.currentThread().getName(), impl.hashCode());
                return impl;
            }
            log.trace("Re-using dispatch chain instance : {}", dispatchChain.hashCode());
            return dispatchChain;
        }

        /**
         * Release the {@link EventDispatchChain} by setting the in-use flag to false. This MUST be called after calling
         * {@link #acquire()} otherwise multiple instances of {@link EventDispatchChain} will be returned.
         */
        private void release() {
            inUse.set(false);
            dispatchChain.reset();
        }
    }
    //endregion

    //region Package Private Methods

    /**
     * Automatically start the event-loop thread
     */
    EventDispatchQueue() {
        started.set(true);
        dispatchService.execute(this::eventLoop);
    }

    /**
     * @return Returns {@code true} if the calling thread belongs to the event dispatch thread
     */
    boolean isDispatchThread() {
        if (uiThread == null)
            throw new IllegalStateException("Event Dispatch Thread not yet started");
        return Thread.currentThread().equals(uiThread);
    }

    Deque<Event> getEventQueue() {
        return this.queue;
    }

    /**
     * Register a runnable that will be invoked continuously within the UI/Event Loop thread. Please note that
     * long-running tasks will block the event/ui thread.
     *
     * <blockquote>
     * Note: This is the same as calling {@code registerRunnable(Runnable, true)}
     * </blockquote>
     *
     * @param runnables
     *         The {@link Runnable} instance
     */
    void registerRunnable(Runnable... runnables) {
        registerRunnable(false, runnables);
    }

    void registerRunnable(boolean invokeOnce, Runnable... runnables) {
        if (runnables == null)
            return;
        for (Runnable runnable : runnables)
            this.runnables.add(new InvocationRecord(runnable, invokeOnce));
    }

    void unregisterRunnable(Runnable... runnables) {
        if (runnables == null)
            return;
        for (Runnable runnable : runnables)
            this.runnables.removeIf(r -> r.runnable.equals(runnable));
    }
    //endregion

    /**
     * The primary event-loop
     */
    private void eventLoop() {
        log.trace("EVENT_QUEUE => [{}] Entering ", Thread.currentThread().getName());
        while (started.get()) {
            //Return a thread-specific singleton instance of EventDispatchChainRecord
            try {
                Event event = queue.takeLast();
                //1) Process events
                processEvent(event);
                //2) Process Runnables
                processRunnables();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        log.trace("EVENT_QUEUE => [{}] Exiting", Thread.currentThread().getName());
        shutdownLatch.countDown();
    }

    private void processRunnables() {
        //try to obtain a read lock, if not available try again later.
        if (runnables.isEmpty())
            return;
        Iterator<InvocationRecord> it = runnables.iterator();
        while (it.hasNext()) {
            InvocationRecord record = it.next();
            if (!record.running) {
                record.running = true;
                record.runnable.run();
                record.running = false;
                if (record.invokeOnce) {
                    it.remove();
                }
            }
        }
    }

    private void processEvent(Event event) {
        if (event == null)
            return;
        log.debug("EVENT_QUEUE_RECV => Processing '{}' (Remaining: {})", event, queue.size());

        //Handle invocation events
        if (event instanceof InvocationEvent) {
            InvocationEvent ie = (InvocationEvent) event;
            if (InvocationEvent.INVOKE_REPEAT_START == event.getEventType()) {
                registerRunnable(ie.getRunnable());
                ie.consume();
            } else if (InvocationEvent.INVOKE_REPEAT_END == event.getEventType()) {
                unregisterRunnable(ie.getRunnable());
                ie.consume();
            } else if (InvocationEvent.INVOKE_ONCE == event.getEventType()) {
                registerRunnable(true, ie.getRunnable());
                ie.consume();
            }
        }

        //if event not yet consumed, propagate it down the chain
        if (!event.isConsumed()) {
            log.debug("EVENT_DISPATCH_ACQUIRE => Acquiring record for event : {}", event);
            EventDispatchChainRecord record = tlDispatchChainRecord.get();
            try {
                EventDispatchChain eventDispatchChain = record.acquire();
                EventTarget eventTarget = event.getTarget();
                log.debug("EVENT_DISPATCH_START => Event '{}' for target '{}'", event, eventTarget);
                if (eventTarget == null) {
                    log.warn("Event target is null for event : {}", event);
                    return;
                }
                EventDispatchChain chain = eventTarget.buildEventTargetPath(eventDispatchChain);
                log.debug("EVENT_DISPATCH_PATH => {} (Target: {})", chain, event.getTarget());
                chain.dispatchEvent(event);
                log.debug("EVENT_DISPATCH_END => Event : {}", event);
            } finally {
                if (!event.isConsumed())
                    event.consume();
                record.release();
                log.debug("EVENT_DISPATCH_RELEASE => Event : {}", event);
            }
        }
    }

    /**
     * Posts an {@link Event} to the internal event-queue
     *
     * @param event
     *         The {@link Event} to be posted
     */
    public void postEvent(Event event) {
        queue.push(event);
        log.debug("EVENT_QUEUE_POST => Post {} (New Size: {})", event, queue.size());
    }

    public boolean isStarted() {
        return started.get();
    }

    /**
     * Performs a graceful shutdown of the Event Loop
     */
    public void shutdown() throws InterruptedException {
        started.set(false);
        shutdownLatch.await(1, TimeUnit.MINUTES);
        runnables.clear();
        dispatchService.shutdown(); //just in case ?
    }
}
