package com.ibasco.pidisplay.drivers.lcd.hd44780.enums;

import com.pi4j.io.gpio.PinState;

public enum LcdRegisterSelectState {
    /**
     * LCD Instruction for Data Processing. Equivalent to {@link PinState#HIGH}
     */
    DATA(PinState.HIGH),
    /**
     * LCD Instruction for Command Processing. Equivalent to {@link PinState#LOW}
     */
    COMMAND(PinState.LOW);

    private PinState state;

    LcdRegisterSelectState(PinState state) {
        this.state = state;
    }

    public PinState getPinState() {
        return state;
    }

    @Override
    public String toString() {
        return "LcdRegisterSelectState{" +
                "state=" + state +
                '}';
    }
}
