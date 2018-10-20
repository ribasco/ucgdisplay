package com.ibasco.ucgdisplay.core.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtils {

    private static final Logger log = LoggerFactory.getLogger(ThreadUtils.class);

    public static void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
            log.debug(ignored.getMessage(), ignored);
        }
    }

    public static void sleepInterrupted(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}
