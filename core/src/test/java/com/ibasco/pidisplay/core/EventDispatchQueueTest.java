package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.events.DisplayEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.mock;

class EventDispatchQueueTest {

    public static final Logger log = LoggerFactory.getLogger(EventDispatchQueueTest.class);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void postEvent() {
        EventDispatchQueue queue = new EventDispatchQueue();
        EventDispatchQueue mockQueue = mock(EventDispatchQueue.class);
        log.debug("List: {}", mockQueue);
        for (int i = 0; i < 500; i++)
            queue.postEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_SHOW, null));
    }

}