/*-
 * ========================START=================================
 * UCGDisplay :: Character LCD driver
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
package com.ibasco.ucgdisplay.drivers.clcd.adapters;

import com.ibasco.ucgdisplay.drivers.clcd.BaseLcdGpioAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.LcdPinMapConfig;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdPin;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdReadWriteState;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdRegisterSelectState;
import com.ibasco.ucgdisplay.drivers.clcd.exceptions.InvalidPinMappingException;
import com.ibasco.ucgdisplay.drivers.clcd.exceptions.PinNotSupportedException;
import com.ibasco.ucgdisplay.drivers.clcd.providers.MCP23017GpioProviderExt;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import static com.pi4j.wiringpi.Gpio.delayMicroseconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * LCD GPIO Adapter for the MCP23017 Port Expander.
 *
 * @author Rafael Ibasco
 */
public class Mcp23017LcdAdapter extends BaseLcdGpioAdapter {
    private static final Logger log = LoggerFactory.getLogger(Mcp23017LcdAdapter.class);
    private MCP23017GpioProviderExt provider;
    private byte[] dataPins = new byte[8];
    private byte enablePin;

    public Mcp23017LcdAdapter(MCP23017GpioProviderExt provider, LcdPinMapConfig pinMapConfig) {
        super(pinMapConfig);
        this.provider = provider;

        //Retrieve the local pin addresses of each data pin
        LcdPin[] lcdDataPins = LcdPin.values();
        for (int i = 0; i < 8; i++) {
            dataPins[i] = pinToLocalAddr(lcdDataPins[i]);
            log.debug("Mapped '{}' Pin to Local Address = {}", lcdDataPins[i].name(), dataPins[i]);
        }
        enablePin = pinToLocalAddr(LcdPin.EN); //enable pin
        log.debug("Mapped 'Enable' Pin to Local Address = {}", enablePin);
    }

    @Override
    protected void validate(LcdPinMapConfig pinMapConfig) throws InvalidPinMappingException {
        for (Map.Entry<LcdPin, Pin> mappedPin : pinMapConfig.getAllPins()) {
            if (!MCP23017GpioProviderExt.NAME.equals(mappedPin.getValue().getProvider())) {
                throw new InvalidPinMappingException(pinMapConfig);
            }
        }
    }

    @Override
    public void initialize() {
        pinMode(LcdPin.RS, PinMode.DIGITAL_OUTPUT);
        pinMode(LcdPin.EN, PinMode.DIGITAL_OUTPUT);
        pinMode(LcdPin.BACKLIGHT, PinMode.DIGITAL_OUTPUT);
        pinMode(LcdPin.DATA_4, PinMode.DIGITAL_OUTPUT);
        pinMode(LcdPin.DATA_5, PinMode.DIGITAL_OUTPUT);
        pinMode(LcdPin.DATA_6, PinMode.DIGITAL_OUTPUT);
        pinMode(LcdPin.DATA_7, PinMode.DIGITAL_OUTPUT);
        if (isMapped(LcdPin.RW))
            pinMode(LcdPin.RW, PinMode.DIGITAL_OUTPUT);
    }

    @Override
    public void write4Bits(byte value) throws IOException {
        //Read the state of all 16 pins
        short out = provider.getState();

        //Since we are in 4-bit mode, just read the last four data pins of the array
        for (int i = 4; i < 8; i++) {
            out &= ~(1 << dataPins[i]);
            out |= ((value >> (i - 4)) & 0x1) << dataPins[i]; //i - 4 (offset)
        }

        out &= ~(1 << enablePin); //enable: low

        //update the state
        provider.setState(out);

        //Pulse enable for the changes to take effect
        out |= (1 << enablePin); //enable: high
        provider.setState(out);
        out &= ~(1 << enablePin); //enable: low
        provider.setState(out);
        delayMicroseconds(100);
    }

    @Override
    public void write8Bits(byte value) {
        for (int i = 0; i < 8; i++) {
            pinMode(dataPins[i], PinMode.DIGITAL_OUTPUT);
            digitalWrite(dataPins[i], (value >> i) & 0x01);
        }
        pulseEnable();
    }

    @Override
    public void setRegSelectState(LcdRegisterSelectState state) {
        digitalWrite(LcdPin.RS, state.getPinState());
    }

    @Override
    public void setReadWriteState(LcdReadWriteState state) {
        //Only write to pin if it is mapped
        if (isMapped(LcdPin.RW))
            digitalWrite(LcdPin.RW, state.getPinState());
    }

    @Override
    public void setEnableState(PinState state) {
        digitalWrite(LcdPin.EN, state);
    }

    /**
     * Convenience method to pulse the enable pin of the Lcd
     */
    private void pulseEnable() {
        digitalWrite(LcdPin.EN, PinState.LOW);
        digitalWrite(LcdPin.EN, PinState.HIGH);
        digitalWrite(LcdPin.EN, PinState.LOW);
        delayMicroseconds(100);
    }

    /**
     * Computes the pin index based on the specified {@link LcdPin}
     *
     * @param pin
     *         The {@link Pin} of the interface
     *
     * @return The computed index of the MCP {@link Pin}
     */
    private byte pinToLocalAddr(LcdPin pin) {
        //get the actual pin that is mapped to the lcd pin
        Pin mPin = getMappedPin(pin);
        return mPin != null ? pinToLocalAddr(mPin) : -1;
    }

    /**
     * Computes the pin index based on the specified {@link Pin}.
     *
     * @param pin
     *         The {@link Pin} of the interface
     *
     * @return The computed index of the MCP {@link Pin}
     */
    private byte pinToLocalAddr(Pin pin) {
        //2^(n-1) = b       #where n = index (0 to 15)
        //n = logb/log(2)   #find n index
        if (!MCP23017GpioProviderExt.NAME.equals(pin.getProvider()))
            throw new PinNotSupportedException(pin, MCP23017GpioProviderExt.NAME);
        int idx = (int) (Math.log(((pin.getAddress() > 1000) ? pin.getAddress() - 1000 : pin.getAddress())) / Math.log(2) + 1);
        return (byte) ((pin.getAddress() > 1000 ? idx + 8 : idx) - 1);
    }

    private void digitalWrite(LcdPin pin, PinState state) {
        provider.setState(getMappedPin(pin), state);
    }

    private void digitalWrite(byte localAddr, int state) {
        if (localAddr > (MCP23017Pin.ALL.length - 1))
            throw new IllegalArgumentException("Invalid local address");
        provider.setState(MCP23017Pin.ALL[localAddr], (state == 0) ? PinState.LOW : PinState.HIGH);
    }

    private void pinMode(LcdPin pin, PinMode mode) {
        if (isMapped(pin))
            provider.setMode(getMappedPin(pin), mode);
    }

    private void pinMode(byte localAddr, PinMode mode) {
        provider.setMode(MCP23017Pin.ALL[localAddr], mode);
    }
}
