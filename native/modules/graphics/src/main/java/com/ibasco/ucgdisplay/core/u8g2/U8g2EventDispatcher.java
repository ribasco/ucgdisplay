/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Graphics
 * %%
 * Copyright (C) 2018 - 2021 Universal Character/Graphics display library
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
package com.ibasco.ucgdisplay.core.u8g2;

import com.ibasco.ucgdisplay.common.drivers.DisplayDriver;
import com.ibasco.ucgdisplay.core.u8g2.exceptions.U8g2ByteEventException;
import com.ibasco.ucgdisplay.core.u8g2.exceptions.U8g2GpioEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Listening service for U8g2 byte and gpio events. Not thread-safe.
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
            throw new U8g2GpioEventException("Error occured on GPIO event listener", e);
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
            throw new U8g2ByteEventException("Error occured on BYTE event listener", e);
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

    private static boolean hasGpioListeners() {
        return gpioEventListeners.size() > 0;
    }

    private static boolean hasByteListeners() {
        return byteEventListeners.size() > 0;
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
