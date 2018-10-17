package com.ibasco.ucgdisplay.examples;

import com.ibasco.ucgdisplay.drivers.clcd.HD44780DisplayDriver;
import com.ibasco.ucgdisplay.drivers.clcd.LcdGpioAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.LcdPinMapConfig;
import com.ibasco.ucgdisplay.drivers.clcd.adapters.GpioLcdAdapter;
import com.ibasco.ucgdisplay.drivers.clcd.enums.LcdPin;
import com.pi4j.io.gpio.RaspiPin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HD44780Example {

    private static final Logger log = LoggerFactory.getLogger(HD44780Example.class);

    public static void main(String[] args) {
        try {
            LcdPinMapConfig config = new LcdPinMapConfig()
                    .map(LcdPin.RS, RaspiPin.GPIO_02)
                    .map(LcdPin.EN, RaspiPin.GPIO_03)
                    .map(LcdPin.DATA_4, RaspiPin.GPIO_04)
                    .map(LcdPin.DATA_5, RaspiPin.GPIO_05)
                    .map(LcdPin.DATA_6, RaspiPin.GPIO_06)
                    .map(LcdPin.DATA_7, RaspiPin.GPIO_07);

            //GPIO adapter
            LcdGpioAdapter adapter = new GpioLcdAdapter(config);

            //Shift register adapter
            //LcdGpioAdapter adapter = new ShiftRegisterLcdAdapter(GpioFactory.getDefaultProvider(), RaspiPin.GPIO_02, RaspiPin.GPIO_03, RaspiPin.GPIO_04, config);

            //MCP23017 I2C adapter (Using built-in templates)
            //MCP23017GpioProviderExt mcp23017GpioProvider = new MCP23017GpioProviderExt(I2CBus.BUS_1, 0x15);
            //LcdGpioAdapter adapter = new Mcp23017LcdAdapter(mcp23017GpioProvider, LcdTemplates.ADAFRUIT_I2C_RGBLCD_MCP23017);

            HD44780DisplayDriver driver = new HD44780DisplayDriver(adapter, 20, 4);
            driver.home();
            driver.write("Hello World".getBytes());
        } catch (Exception e) {
            log.error("Error occured", e);
        }
    }
}
