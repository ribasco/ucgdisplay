package com.ibasco.pidisplay.emulator.protocols;

import com.ibasco.pidisplay.core.gpio.GpioEvent;
import com.ibasco.pidisplay.emulator.GlcdCommProtocol;

public class GlcdI2CCommProtocol extends GlcdCommProtocol {

    public GlcdI2CCommProtocol(ByteEventHandler byteEventHandler) {
        super(byteEventHandler);
    }

    @Override
    public void decode(GpioEvent event) {

    }
}
