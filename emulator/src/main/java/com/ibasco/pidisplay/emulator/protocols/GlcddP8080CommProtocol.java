package com.ibasco.pidisplay.emulator.protocols;

import com.ibasco.pidisplay.core.gpio.GpioEvent;
import com.ibasco.pidisplay.emulator.GlcdCommProtocol;

public class GlcddP8080CommProtocol extends GlcdCommProtocol {

    public GlcddP8080CommProtocol(ByteEventHandler byteEventHandler) {
        super(byteEventHandler);
    }

    @Override
    public void decode(GpioEvent event) {

    }
}
