package com.ibasco.pidisplay.emulator.gpio;

import com.ibasco.pidisplay.core.gpio.GpioEvent;
import com.ibasco.pidisplay.core.gpio.GpioEventService;
import com.ibasco.pidisplay.emulator.GpioEventDispatcher;

/**
 * Attaches to the {@link GpioEventService} to listen for GPIO events emitted by the native library
 *
 * @author Rafael Ibasco
 */
public class JniGpioEventDispatcher extends GpioEventDispatcher {

    @Override
    protected void initialize() {
        GpioEventService.addListener(this::gpioEvent);
    }

    private void gpioEvent(GpioEvent gpioEvent) {
        dispatchGpioEvent(gpioEvent);
    }
}
