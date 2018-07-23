package com.ibasco.pidisplay.core.gpio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Listening service for Gpio events
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings("unused")
public class GpioEventService {

    public static final Logger log = LoggerFactory.getLogger(GpioEventService.class);

    private static final List<GpioEventListener> listeners = new ArrayList<>();

    /**
     * Invoked by native library during low-level GPIO events
     *
     * @param event
     *         The GPIO message that occured
     */
    private static void onGpioEvent(GpioEvent event) {
        try {
            for (GpioEventListener listener : listeners) {
                listener.onGpioEvent(event);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void addListener(GpioEventListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(GpioEventListener listener) {
        listeners.remove(listener);
    }

    public static native void startListening();

    public static native void stoptListening();
}
