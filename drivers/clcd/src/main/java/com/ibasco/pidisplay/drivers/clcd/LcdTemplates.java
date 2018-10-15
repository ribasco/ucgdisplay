package com.ibasco.pidisplay.drivers.clcd;

import com.ibasco.pidisplay.drivers.clcd.enums.LcdPin;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;

/**
 * Provided as a convenience for popular lcd modules. This contains their pin mapping configuration
 *
 * @author Rafael Ibasco
 */
public class LcdTemplates {
    /**
     * Pin mapping configuration for the popular adafruit LCD I2C using MCP23017
     */
    public static final LcdPinMapConfig ADAFRUIT_I2C_RGBLCD_MCP23017 = new LcdPinMapConfig()
            .map(LcdPin.RS, MCP23017Pin.GPIO_B7)
            .map(LcdPin.EN, MCP23017Pin.GPIO_B5)
            .map(LcdPin.DATA_4, MCP23017Pin.GPIO_B4)
            .map(LcdPin.DATA_5, MCP23017Pin.GPIO_B3)
            .map(LcdPin.DATA_6, MCP23017Pin.GPIO_B2)
            .map(LcdPin.DATA_7, MCP23017Pin.GPIO_B1)
            .map(LcdPin.BACKLIGHT, MCP23017Pin.GPIO_B6);
            /*.map(LcdPin.BUTTON_1, MCP23017Pin.GPIO_A0)
            .map(LcdPin.BUTTON_2, MCP23017Pin.GPIO_A1)
            .map(LcdPin.BUTTON_3, MCP23017Pin.GPIO_A2)
            .map(LcdPin.BUTTON_4, MCP23017Pin.GPIO_A3)*/
}
