/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Character LCD driver
 * Filename: Pcf8574LcdAdapter.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 - 2019 Universal Character/Graphics display library
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
public class Pcf8574LcdAdapter extends BaseLcdGpioAdapter {

    private PCF8574GpioProvider provider;

    public Pcf8574LcdAdapter(PCF8574GpioProvider provider, LcdPinMapConfig pinMapConfig) {
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
