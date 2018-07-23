package com.ibasco.pidisplay.emulator;

import com.ibasco.pidisplay.core.gpio.GpioEvent;
import com.ibasco.pidisplay.core.gpio.GpioEventListener;

import java.util.ArrayList;
import java.util.List;

abstract public class GpioEventDispatcher {

    private List<GpioEventListener> listeners = new ArrayList<>();

    abstract protected void initialize();

    protected void dispatchGpioEvent(GpioEvent event) {
        for (GpioEventListener listener : listeners) {
            listener.onGpioEvent(event);
        }
    }

    public void addListener(GpioEventListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(GpioEventListener listener) {
        this.listeners.remove(listener);
    }
}
