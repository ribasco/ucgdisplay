package com.ibasco.pidisplay.emulator.protocols;

import com.ibasco.pidisplay.core.gpio.GpioEvent;
import com.ibasco.pidisplay.emulator.GlcdCommProtocol;

public class GlcdP6800CommProtocol extends GlcdCommProtocol {

    public GlcdP6800CommProtocol(ByteEventHandler byteEventHandler) {
        super(byteEventHandler);
    }

    @Override
    public void decode(GpioEvent event) {

    }
}
