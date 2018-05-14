package com.ibasco.pidisplay.drivers.lcd.hd44780.exceptions;


import com.ibasco.pidisplay.drivers.lcd.hd44780.LcdPinMapConfig;

/**
 * Thrown to indicate an invalid pin mappingConfig configuration for the LCD Interface
 *
 * @author Rafael Ibasco
 */
public class InvalidPinMappingException extends RuntimeException {

    private final LcdPinMapConfig mappingConfig;

    public InvalidPinMappingException(String message, LcdPinMapConfig mappingConfig) {
        super(message);
        this.mappingConfig = mappingConfig;
    }

    public InvalidPinMappingException(LcdPinMapConfig mappingConfig) {
        super(String.format("Invalid pin mapping configuration found. Make sure you have the right " +
                "pins for the Provider you specified (Pins: %s)", mappingConfig.toString()));
        this.mappingConfig = mappingConfig;
    }

    public LcdPinMapConfig getMappingConfig() {
        return mappingConfig;
    }
}
