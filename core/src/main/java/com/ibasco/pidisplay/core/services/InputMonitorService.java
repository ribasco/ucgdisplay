package com.ibasco.pidisplay.core.services;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ibasco.pidisplay.core.EventHandlerManager;
import com.ibasco.pidisplay.core.EventTarget;
import com.ibasco.pidisplay.core.beans.InputEventData;
import com.ibasco.pidisplay.core.events.RawInputEvent;
import com.ibasco.pidisplay.core.exceptions.NoHIDAvailableException;
import com.ibasco.pidisplay.core.exceptions.NoPermissionToAccessException;
import com.ibasco.pidisplay.core.util.ByteUtils;
import com.pi4j.io.file.LinuxFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.ibasco.pidisplay.core.EventUtil.fireEvent;

/**
 * The class responsible for dispatching Raw System Input Events. There can only be one active listener at a time.
 *
 * @author Rafael Ibasco
 */
//TODO: Add ability to monitor multiple HIDs
//TODO: Each HID should be in it's own thread
public class InputMonitorService {

    private static Logger log = LoggerFactory.getLogger(InputMonitorService.class);

    private ExecutorService inputMonitorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("pd-input-%d").setDaemon(true).build());

    private AtomicBoolean shutdown = new AtomicBoolean(true);

    private File devFs;

    private EventTarget activeTarget = null;

    private Lock lock = new ReentrantLock();

    private static class LazyHolder {
        private static final InputMonitorService INSTANCE = new InputMonitorService();
    }

    private InputMonitorService() {
        startMonitoring();
    }

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

    public void shutdown() {
        try {
            stopMonitoring();
            inputMonitorService.shutdownNow();
            inputMonitorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void initialize() throws IOException {
        //TODO: We need to add a way to automatically detect which /dev/input/x is the default HID device. For now, this is hardcoded
        devFs = new File("/dev/input/event0");
        if (!devFs.exists()) {
            log.warn("No HID connected to the system. Monitoring service will not start.");
            throw new NoHIDAvailableException();
        }
        if (!devFs.canRead()) {
            String msg = String.format("You do not have permission to access '%s'", devFs.getCanonicalPath());
            log.error(msg);
            throw new NoPermissionToAccessException(devFs);
        }
    }

    private void startMonitoring() {
        if (shutdown.compareAndSet(true, false)) {
            log.debug("Trying to initialize");
            try {
                initialize();
                inputMonitorService.execute(this::inputMonitor);
            } catch (IOException e) {
                shutdown.set(true);
                log.warn("Unable to initialize input service");
                throw new RuntimeException("Unable to initialize monitoring service", e);
            }
        } else
            log.debug("Could not set shutdown flag");
    }

    private void stopMonitoring() {
        shutdown.set(true);
    }

    private EventHandlerManager eventHandlerManager;

    public EventHandlerManager eventHandlerManager() {
        if (eventHandlerManager == null) {
            eventHandlerManager = new EventHandlerManager(this);
        }
        return eventHandlerManager;
    }

    /**
     * Main method responsible for monitoring system input events
     */
    private void inputMonitor() {
        try (LinuxFile file = new LinuxFile(devFs.getCanonicalPath(), "r")) {
            log.debug("INPUT => Entering input monitoring event loop");
            while (!shutdown.get()) {
                byte[] data = new byte[16];
                //Block until we receive bytes
                int bytesRead = file.read(data, 0, data.length);
                try {
                    lock.lock();
                    //Process the event data
                    if (activeTarget == null)
                        continue;
                    if (bytesRead > 0) {
                        InputEventData inputEventData = createInputEventData(data);
                        fireEvent(activeTarget, new RawInputEvent(RawInputEvent.RAW_INPUT, inputEventData));
                    } else {
                        log.debug("Unable to obtain read lock for input monitor. A write-operation is in-progress.");
                    }
                } finally {
                    lock.unlock();
                }
                //ThreadUtils.sleep(5);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        log.debug("INPUT => Exiting Input monitoring event loop");
    }

    @SuppressWarnings("Duplicates")
    private InputEventData createInputEventData(byte[] data) {
        ByteBuffer buffer = ByteUtils.wrapDirectBuffer(data);
        int tvSeconds = buffer.getInt();
        int tvMicroSeconds = buffer.getInt();
        short type = buffer.getShort();
        short code = buffer.getShort();
        int value = buffer.getInt();
        return new InputEventData(tvSeconds, tvMicroSeconds, type, code, value);
    }

    public static InputMonitorService getInstance() {
        return LazyHolder.INSTANCE;
    }
}
