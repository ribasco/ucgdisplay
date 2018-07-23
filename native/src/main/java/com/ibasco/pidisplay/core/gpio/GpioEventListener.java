package com.ibasco.pidisplay.core.gpio;

@FunctionalInterface
public interface GpioEventListener {
    void onGpioEvent(GpioEvent event);
}
