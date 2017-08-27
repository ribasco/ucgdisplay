package com.ibasco.pidisplay.drivers.lcd.pins;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

/**
 * A collection of valid Shift Register Output Pins
 *
 * @author Rafael Ibasco
 */
public final class ShiftOutRegPin {
    public static final Pin QA = createPin(0, "QA");
    public static final Pin QB = createPin(1, "QB");
    public static final Pin QC = createPin(2, "QC");
    public static final Pin QD = createPin(3, "QD");
    public static final Pin QE = createPin(4, "QE");
    public static final Pin QF = createPin(5, "QF");
    public static final Pin QG = createPin(6, "QG");
    public static final Pin QH = createPin(7, "QH");

    public static final String PROVIDER_NAME = "ShiftOutReg";

    private static Pin createPin(int address, String name) {
        return new PinImpl(PROVIDER_NAME, address, name, EnumSet.of(PinMode.DIGITAL_OUTPUT, PinMode.GPIO_CLOCK));
    }
}
