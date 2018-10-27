/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native Library
 * Filename: InputDeviceManager.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
package com.ibasco.ucgdisplay.core.input;

import com.ibasco.ucgdisplay.common.utils.NativeLibraryLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Monitors events generated from Input Devices
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class InputDeviceManager {

    public static final Logger log = LoggerFactory.getLogger(InputDeviceManager.class);

    private static ReadWriteLock deviceStateEventRwLock = new ReentrantReadWriteLock();

    private static ReadWriteLock inputEventRwLock = new ReentrantReadWriteLock();

    private static Lock inputEventWriteLock = inputEventRwLock.writeLock();

    private static Lock inputEventReadLock = inputEventRwLock.readLock();

    private static Lock deviceStateEventWriteLock = deviceStateEventRwLock.writeLock();

    private static Lock deviceStateEventReadLock = deviceStateEventRwLock.readLock();

    private static final List<RawInputEventListener> inputEventListeners = new ArrayList<>();

    private static final List<DeviceStateEventListener> deviceStateEventListeners = new ArrayList<>();

    @FunctionalInterface
    public interface RawInputEventListener {
        void onInputEvent(RawInputEvent data);
    }

    @FunctionalInterface
    public interface DeviceStateEventListener {
        void onDeviceStateChangeEvent(DeviceStateEvent event);
    }

    static {
        try {
            NativeLibraryLoader.loadLibrary("ucgdisp");
        } catch (Exception e) {
            log.error("Unable to load required native library", e);
        }
    }

    public static void addInputEventListener(RawInputEventListener listener) {
        inputEventWriteLock.lock();
        if (!inputEventListeners.contains(listener)) {
            inputEventListeners.add(listener);
        }
        inputEventWriteLock.unlock();
    }

    public static void removeInputEventListener(RawInputEventListener listener) {
        try {
            inputEventWriteLock.lock();
            inputEventListeners.remove(listener);
        } finally {
            inputEventWriteLock.unlock();
        }
    }

    public static void addDeviceStateEventListener(DeviceStateEventListener listener) {
        deviceStateEventWriteLock.lock();
        if (!deviceStateEventListeners.contains(listener)) {
            deviceStateEventListeners.add(listener);
        }
        deviceStateEventWriteLock.unlock();
    }

    public static void removeDeviceStateEventListener(DeviceStateEventListener listener) {
        try {
            deviceStateEventWriteLock.lock();
            deviceStateEventListeners.remove(listener);
        } finally {
            deviceStateEventWriteLock.unlock();
        }
    }

    private static void inputEventCallback(RawInputEvent event) {
        try {
            inputEventReadLock.lock();
            for (RawInputEventListener listener : inputEventListeners) {
                listener.onInputEvent(event);
            }
        } finally {
            inputEventReadLock.unlock();
        }
    }

    private static void deviceStateEventCallback(DeviceStateEvent event) {
        try {
            deviceStateEventReadLock.lock();
            for (DeviceStateEventListener listener : deviceStateEventListeners) {
                listener.onDeviceStateChangeEvent(event);
            }
        } finally {
            deviceStateEventReadLock.unlock();
        }
    }

    public static native void startInputEventMonitor();

    public static native void stopInputEventMonitor();

    public static native void refreshDeviceCache();

    public static native InputDevice queryDevice(String devicePath) throws IOException;

    public static native InputDevice[] getInputDevices();
}
