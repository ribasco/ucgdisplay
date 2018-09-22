package com.ibasco.pidisplay.core.u8g2;

import com.ibasco.pidisplay.core.system.DisplayDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Listening service for Gpio events
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings("unused")
public class U8g2EventDispatcher {

    public static final Logger log = LoggerFactory.getLogger(U8g2EventDispatcher.class);

    private static final Map<DisplayDriver, U8g2GpioEventListener> gpioEventListeners = new HashMap<>();

    private static final Map<DisplayDriver, U8g2ByteEventListener> byteEventListeners = new HashMap<>();

    /**
     * Invoked by native library on low-level GPIO events
     *
     * @param id
     *         The instance id of the driver where the event originated
     * @param msg
     *         The GPIO message that occured
     * @param data
     *         The state of the GPIO data pin
     */
    private static void onGpioEvent(long id, int msg, int data) {
        try {
            U8g2GpioEventListener listener = findGpioListenerById(id);
            if (listener != null)
                listener.onGpioEvent(new U8g2GpioEvent(msg, data));
        } catch (Exception e) {
            log.error("Error occured during gpio event", e);
        }
    }

    /**
     * Invoked by native library during low-level Byte events
     *
     * @param id
     *         The instance id of the driver where the event originated
     * @param msg
     *         The GPIO message that occured
     * @param data
     *         The raw byte value which represents either a data or instruction of the display
     */
    private static void onByteEvent(long id, int msg, int data) {
        try {
            U8g2ByteEventListener listener = findByteListenerById(id);
            if (listener != null)
                listener.onByteEvent(new U8g2ByteEvent(msg, data));
        } catch (Exception e) {
            log.error("Error occured during byte event", e);
        }
    }

    public static void addGpioListener(DisplayDriver driver, U8g2GpioEventListener listener) {
        gpioEventListeners.put(driver, listener);
    }

    public static void removeGpioListener(DisplayDriver driver) {
        gpioEventListeners.remove(driver);
    }

    public static void addByteListener(DisplayDriver driver, U8g2ByteEventListener listener) {
        byteEventListeners.put(driver, listener);
    }

    public static void removeByteListener(DisplayDriver driver) {
        byteEventListeners.remove(driver);
    }

    private static U8g2GpioEventListener findGpioListenerById(long id) {
        for (Map.Entry<DisplayDriver, U8g2GpioEventListener> entry : gpioEventListeners.entrySet()) {
            if (id == entry.getKey().getId())
                return entry.getValue();
        }
        return null;
    }

    private static U8g2ByteEventListener findByteListenerById(long id) {
        for (Map.Entry<DisplayDriver, U8g2ByteEventListener> entry : byteEventListeners.entrySet()) {
            if (id == entry.getKey().getId())
                return entry.getValue();
        }
        return null;
    }
}
