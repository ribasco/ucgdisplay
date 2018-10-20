package com.ibasco.ucgdisplay.core.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ObservableListWrapperTest {

    public static final Logger log = LoggerFactory.getLogger(ObservableListWrapperTest.class);

    private AtomicBoolean changeReceived;
    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        changeReceived = new AtomicBoolean(false);
        latch = new CountDownLatch(1);
    }

    @Test
    @DisplayName("Test add listener operation")
    void testAddListener() {
        CountDownLatch latch = new CountDownLatch(10);

        try {
            ObservableListWrapper<String> observableList = new ObservableListWrapper<>();

            ArrayList<ListChangeListener.Change> changeDetails = new ArrayList<>();

            observableList.addListener(change -> {
                changeDetails.add(change);
                latch.countDown();
            });

            for (int i = 0; i < 10; i++) {
                observableList.add("test: " + i);
            }

            if (!latch.await(3, TimeUnit.SECONDS)) {
                fail("Uncaught event");
            }

            log.debug("Total change events received: {}", changeDetails.size(), latch.getCount());

            assertEquals(0, latch.getCount());
            assertTrue(observableList.size() > 0);
            assertFalse(changeDetails.isEmpty());
            assertEquals(10, changeDetails.size());
            assertEquals(10, observableList.size());
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Test remove listener operation")
    void testRemoveListener() throws InterruptedException {
        ObservableListWrapper<String> observableList = new ObservableListWrapper<>();

        ListChangeListener<String> listener = this::listChangeListener;

        observableList.addListener(listener);
        observableList.add("test-add");

        latch.await(3, TimeUnit.SECONDS);

        assertTrue(changeReceived.get());

        //reset properties
        changeReceived.set(false);
        latch = new CountDownLatch(1);

        observableList.removeListener(listener);

        //Trigger another add change event
        observableList.add("test-add-2");

        latch.await(3, TimeUnit.SECONDS);

        assertFalse(changeReceived.get());
    }

    private void listChangeListener(ListChangeListener.Change<? extends String> change) {
        log.debug("Change in list occured : {}", change);
        changeReceived.set(true);
        latch.countDown();
    }
}