package com.ibasco.pidisplay.drivers.clcd;

import com.ibasco.pidisplay.drivers.clcd.enums.LcdPin;
import com.ibasco.pidisplay.drivers.clcd.exceptions.InvalidPinMappingException;
import com.pi4j.io.gpio.Pin;

/**
 * Base class for {@link LcdGpioAdapter}
 *
 * @author Rafael Ibasco
 */
abstract public class BaseLcdGpioAdapter implements LcdGpioAdapter {
    private LcdPinMapConfig pinMapConfig;

    public BaseLcdGpioAdapter(LcdPinMapConfig pinMapConfig) {
        this.pinMapConfig = pinMapConfig;
        validate(this.pinMapConfig);
    }

    /**
     * Verify that we have the required lcd pins mapped
     *
     * @throws IllegalArgumentException
     *         Thrown if one of the required LCD Pins are not mapped
     */
    abstract protected void validate(LcdPinMapConfig pinMapConfig) throws InvalidPinMappingException;

    /**
     * Check if the {@link LcdPin} is mapped to a {@link Pin}
     *
     * @param pin
     *         The {@link LcdPin} to check
     *
     * @return Returns {@code true} if the {@link LcdPin} is mapped
     */
    protected boolean isMapped(LcdPin pin) {
        return pinMapConfig.isMapped(pin);
    }

    /**
     * Retrieve the mapped {@link LcdPin}
     *
     * @param pin
     *         The {@link LcdPin} to use for lookup
     *
     * @return Returns the mapped {@link Pin} instance
     */
    protected Pin getMappedPin(LcdPin pin) {
        return pinMapConfig.getMappedPin(pin);
    }
}
