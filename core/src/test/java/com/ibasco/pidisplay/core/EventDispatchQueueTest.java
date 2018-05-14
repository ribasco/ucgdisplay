package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.InvocationEvent;
import com.ibasco.pidisplay.core.util.concurrent.ThreadUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EventDispatchQueueTest {

    public static final Logger log = LoggerFactory.getLogger(EventDispatchQueueTest.class);

    EventDispatchQueue queue;

    @BeforeEach
    void setUp() {
        queue = new EventDispatchQueue();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        queue.shutdown();
    }

    @Test
    void testPostEvent() {
        Runnable displayUi = () -> {
            log.debug("DISPLAY UI RENDER");
            ThreadUtils.sleep(100);
        };
        registerRunnable(displayUi, queue, false);
        for (int i = 0; i < 500; i++) {
            queue.postEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_SHOW, null));
            ThreadUtils.sleep(50);
        }
    }

    private void registerRunnable(Runnable runnable, EventDispatchQueue queue, boolean invokeOnce) {
        if (invokeOnce)
            queue.postEvent(new InvocationEvent(InvocationEvent.INVOKE_ONCE, runnable));
        else
            queue.postEvent(new InvocationEvent(InvocationEvent.INVOKE_REPEAT_START, runnable));
    }
}