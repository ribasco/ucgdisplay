package com.ibasco.pidisplay.core.util.concurrent;

public class ThreadUtils {
    public static void sleepUninterrupted(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
        }
    }
}
