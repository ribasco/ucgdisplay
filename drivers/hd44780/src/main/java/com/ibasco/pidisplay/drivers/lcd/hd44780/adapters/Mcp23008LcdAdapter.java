package com.ibasco.pidisplay.drivers.lcd.hd44780.adapters;

import com.ibasco.pidisplay.drivers.lcd.hd44780.BaseLcdGpioAdapter;
import com.ibasco.pidisplay.drivers.lcd.hd44780.LcdPinMapConfig;
import com.ibasco.pidisplay.drivers.lcd.hd44780.enums.LcdReadWriteState;
import com.ibasco.pidisplay.drivers.lcd.hd44780.enums.LcdRegisterSelectState;
import com.ibasco.pidisplay.drivers.lcd.hd44780.exceptions.InvalidPinMappingException;
import com.pi4j.gpio.extension.mcp.MCP23008GpioProvider;
import com.pi4j.io.gpio.PinState;

/**
 * MCP23008 LCD Adapter
 *
 * @author Rafael Ibasco
 */
public class Mcp23008LcdAdapter extends BaseLcdGpioAdapter {

    private MCP23008GpioProvider provider;

    public Mcp23008LcdAdapter(MCP23008GpioProvider provider, LcdPinMapConfig pinMapConfig) {
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
