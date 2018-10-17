package com.ibasco.ucgdisplay.drivers.clcd.adapters;

import com.ibasco.ucgdisplay.drivers.clcd.BaseLcdGpioAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.LcdPinMapConfig;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdReadWriteState;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdRegisterSelectState;
import com.ibasco.ucgdisplay.drivers.clcd.exceptions.InvalidPinMappingException;
import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.io.gpio.PinState;

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
    public void write4Bits(byte value) {
        //TODO: Add implementation
    }

    @Override
    public void write8Bits(byte value) {
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
