package com.ibasco.pidisplay.core.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InputDeviceManager {

    private static ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private static Lock writeLock = rwLock.writeLock();

    private static Lock readLock = rwLock.readLock();

    @FunctionalInterface
    public interface RawInputEventListener {
        void onInputEvent(RawInputEvent data);
    }

    private static final List<RawInputEventListener> listeners = new ArrayList<>();

    static {
        System.loadLibrary("pidisp");
    }

    public static void addListener(RawInputEventListener listener) {
        writeLock.lock();
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        writeLock.unlock();
    }

    public static void removeListener(RawInputEventListener listener) {
        writeLock.lock();
        listeners.remove(listener);
        writeLock.unlock();
    }

    private static void inputEventCallback(RawInputEvent event) {
        readLock.lock();
        for (RawInputEventListener listener : listeners) {
            listener.onInputEvent(event);
        }
        readLock.unlock();
    }

    public static native void startMonitor();

    public static native void stopMonitor();

    public static native InputDevice queryDevice(String devicePath) throws IOException;

    public static native int open(String path) throws IOException;

    public static native void close(int fd) throws IOException;

    public static native InputDevice[] getInputDevices();
}
