package com.ibasco.pidisplay.drivers.lcd.adapters;

import com.ibasco.pidisplay.drivers.lcd.BaseLcdGpioAdapter;
import com.ibasco.pidisplay.drivers.lcd.LcdPinMapConfig;
import com.ibasco.pidisplay.drivers.lcd.enums.LcdReadWriteState;
import com.ibasco.pidisplay.drivers.lcd.enums.LcdRegisterSelectState;
import com.ibasco.pidisplay.drivers.lcd.exceptions.InvalidPinMappingException;
import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.gpio.PinState;

import java.io.IOException;

/**
 * PCF8574 Adapter
 *
 * @author Rafael Ibasco
 */
public class Pcf8574TLcdAdapter extends BaseLcdGpioAdapter {

    private PCF8574GpioProvider provider;

    public Pcf8574TLcdAdapter(PCF8574GpioProvider provider, LcdPinMapConfig pinMapConfig) {
        super(pinMapConfig);
        this.provider = provider;
    }

    @Override
    protected void validate(LcdPinMapConfig pinMapConfig) throws InvalidPinMappingException {
        //TODO: Add implementation
    }

    @Override
    public void initialize() {
        //TODO: Add implementation
    }

    @Override
    public void write4Bits(byte value) throws IOException {
        //TODO: Add implementation
    }

    @Override
    public void write8Bits(byte value) throws IOException {
        //TODO: Add implementation
    }

    @Override
    public void setRegSelectState(LcdRegisterSelectState state) {
        //TODO: Add implementation
    }

    @Override
    public void setReadWriteState(LcdReadWriteState state) {
        //TODO: Add implementation
    }

    @Override
    public void setEnableState(PinState state) {
        //TODO: Add implementation
    }
}
