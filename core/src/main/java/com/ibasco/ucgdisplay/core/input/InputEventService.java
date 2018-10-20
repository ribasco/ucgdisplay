package com.ibasco.ucgdisplay.core.input;

import com.ibasco.ucgdisplay.core.EventHandlerManager;
import com.ibasco.ucgdisplay.core.EventTarget;
import com.ibasco.ucgdisplay.core.events.InputDeviceEvent;
import com.ibasco.ucgdisplay.core.events.InputEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.ibasco.ucgdisplay.core.EventUtil.fireEvent;

/**
 * Dispatches input/device events. There can only be one active listener at a time.
 *
 * @author Rafael Ibasco
 */
public class InputEventService {

    private static final Logger log = LoggerFactory.getLogger(InputEventService.class);

    private EventTarget activeTarget = null;

    private Lock lock = new ReentrantLock();

    private EventHandlerManager eventHandlerManager;

    private static class LazyHolder {
        private static final InputEventService INSTANCE = new InputEventService();
    }

    private InputEventService() {
        //Register for input events
        InputDeviceManager.addInputEventListener(this::rawInputEventListener);
        InputDeviceManager.addDeviceStateEventListener(this::deviceStateEventListener);
        startMonitoring();
        log.debug("InputMonitorService Started");
    }

    /**
     * Callback for listening to device state changes (add/remove actions)
     *
     * @param deviceStateEvent
     *         Contains information of the Input Device that generated the event
     */
    private void deviceStateEventListener(DeviceStateEvent deviceStateEvent) {
        log.debug("Device state event: {}", deviceStateEvent);
        try {
            lock.lock();
            if (activeTarget != null) {
                if (DeviceStateEvent.DEVICE_ACTION_ADDED.equals(deviceStateEvent.getAction())) {
                    fireEvent(activeTarget, new InputDeviceEvent(InputDeviceEvent.DEVICE_ADDED, deviceStateEvent));
                } else if (DeviceStateEvent.DEVICE_ACTION_REMOVED.equals(deviceStateEvent.getAction())) {
                    fireEvent(activeTarget, new InputDeviceEvent(InputDeviceEvent.DEVICE_REMOVED, deviceStateEvent));
                } else {
                    log.warn("Unknown device state: {}", deviceStateEvent);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Callback for Input Device events
     *
     * @param rawInputEvent
     *         Contains information about the Input Event that occured
     */
    private void rawInputEventListener(RawInputEvent rawInputEvent) {
        try {
            lock.lock();
            if (activeTarget != null)
                fireEvent(activeTarget, new InputEvent(InputEvent.RAW_INPUT, rawInputEvent));
            else
                log.debug("No active target for input event");
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the default target of the Raw Input Events
     *
     * @param target
     *         The default {@link EventTarget} instance
     *
     * @return Returns <code>true</code> if the operation is successful.
     */
    public boolean setActiveTarget(EventTarget target) {
        if (lock.tryLock()) {
            try {
                this.activeTarget = target;
            } finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    private void startMonitoring() {
        log.debug("Starting input device manager");
        InputDeviceManager.startInputEventMonitor();
    }

    private void stopMonitoring() {
        log.debug("Stopping input device manager");
        InputDeviceManager.stopInputEventMonitor();
    }

    public EventHandlerManager eventHandlerManager() {
        if (eventHandlerManager == null) {
            eventHandlerManager = new EventHandlerManager(this);
        }
        return eventHandlerManager;
    }

    public static InputEventService getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    protected void finalize() {
        stopMonitoring();
    }
}
