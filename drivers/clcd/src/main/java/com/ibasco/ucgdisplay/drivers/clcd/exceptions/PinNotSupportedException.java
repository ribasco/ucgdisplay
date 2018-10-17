package com.ibasco.ucgdisplay.drivers.clcd.exceptions;

import com.pi4j.io.gpio.Pin;

public class PinNotSupportedException extends RuntimeException {

    private Pin pin;

    private String expectedProvider;

    public PinNotSupportedException(Pin pin, String expectedProvider) {
        super(String.format("Pin is not supported. Expected Pin Provider: %s, Actual Pin Provider: %s", expectedProvider, pin.getProvider()));
        this.pin = pin;
        this.expectedProvider = expectedProvider;
    }

    public Pin getPin() {
        return pin;
    }

    public String getExpectedProvider() {
        return expectedProvider;
    }
}
