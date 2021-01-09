/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
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
//
// THIS IS AN AUTO-GENERATED CODE!! DO NOT MODIFY (Last updated: Sat, 4 Jul 2020 15:43:14 +0800)
//
package com.ibasco.ucgdisplay.drivers.glcd.enums;

public enum GlcdCommInterfaceInfo {
    U8X8_SETPIN_4WIRE_SW_SPI("4W_SW_SPI", "u8x8_SetPin_4Wire_SW_SPI", "u8x8_byte_arduino_4wire_sw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t clock, uint8_t data, uint8_t cs, uint8_t dc, uint8_t reset = U8X8_PIN_NONE", "clock, data, cs, dc, reset", "clock, data, cs, dc [, reset]", "u8x8_byte_4wire_sw_spi"),

    SPI_HW_4WIRE("2ND_4W_HW_SPI", "u8x8_SetPin_4Wire_HW_SPI", "u8x8_byte_arduino_2nd_hw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t cs, uint8_t dc, uint8_t reset = U8X8_PIN_NONE", "cs, dc, reset", "cs, dc [, reset]", "uC specific"),

    U8X8_SETPIN_8BIT_6800("6800", "u8x8_SetPin_8Bit_6800", "u8x8_byte_8bit_6800mode", "u8x8_gpio_and_delay_arduino", "uint8_t d0, uint8_t d1, uint8_t d2, uint8_t d3, uint8_t d4, uint8_t d5, uint8_t d6, uint8_t d7, uint8_t enable, uint8_t cs, uint8_t dc, uint8_t reset = U8X8_PIN_NONE", "d0, d1, d2, d3, d4, d5, d6, d7, enable, cs, dc, reset", "d0, d1, d2, d3, d4, d5, d6, d7, enable, cs, dc [, reset]", "u8x8_byte_8bit_6800mode"),

    U8X8_SETPIN_8BIT_8080("8080", "u8x8_SetPin_8Bit_8080", "u8x8_byte_arduino_8bit_8080mode", "u8x8_gpio_and_delay_arduino", "uint8_t d0, uint8_t d1, uint8_t d2, uint8_t d3, uint8_t d4, uint8_t d5, uint8_t d6, uint8_t d7, uint8_t enable, uint8_t cs, uint8_t dc, uint8_t reset = U8X8_PIN_NONE", "d0, d1, d2, d3, d4, d5, d6, d7, enable, cs, dc, reset", "d0, d1, d2, d3, d4, d5, d6, d7, enable, cs, dc [, reset]", "u8x8_byte_8bit_8080mode"),

    U8X8_SETPIN_3WIRE_SW_SPI("SW_SPI", "u8x8_SetPin_3Wire_SW_SPI", "u8x8_byte_arduino_4wire_sw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t clock, uint8_t data, uint8_t cs, uint8_t reset = U8X8_PIN_NONE", "clock, data, cs, reset", "clock, data, cs [, reset]", "u8x8_byte_4wire_sw_spi"),

    U8X8_SETPIN_3WIRE_HW_SPI("3W_HW_SPI", "u8x8_SetPin_3Wire_HW_SPI", "u8x8_byte_arduino_3wire_hw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t cs, uint8_t reset = U8X8_PIN_NONE", "cs, reset", "cs [, reset]", "uC specific"),

    U8X8_SETPIN_SW_I2C("SW_I2C", "u8x8_SetPin_SW_I2C", "u8x8_byte_arduino_sw_i2c", "u8x8_gpio_and_delay_arduino", "uint8_t clock, uint8_t data, uint8_t reset = U8X8_PIN_NONE", "clock,  data,  reset", "clock,  data [,  reset]", "u8x8_byte_sw_i2c"),

    U8X8_SETPIN_HW_I2C("2ND_HW_I2C", "u8x8_SetPin_HW_I2C", "u8x8_byte_arduino_2nd_hw_i2c", "u8x8_gpio_and_delay_arduino", "uint8_t reset = U8X8_PIN_NONE", "reset", "[reset]", "uC specific"),

    U8X8_SETPIN_ST7920_HW_SPI("2ND_HW_SPI", "u8x8_SetPin_ST7920_HW_SPI", "u8x8_byte_arduino_2nd_hw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t cs, uint8_t reset = U8X8_PIN_NONE", "cs, reset", "cs [, reset]", "uC specific"),

    U8X8_SETPIN_KS0108("", "u8x8_SetPin_KS0108", "u8x8_byte_arduino_ks0108", "u8x8_gpio_and_delay_arduino", "uint8_t d0, uint8_t d1, uint8_t d2, uint8_t d3, uint8_t d4, uint8_t d5, uint8_t d6, uint8_t d7, uint8_t enable, uint8_t dc, uint8_t cs0, uint8_t cs1, uint8_t cs2, uint8_t reset = U8X8_PIN_NONE", "d0, d1, d2, d3, d4, d5, d6, d7, enable, dc, cs0, cs1, cs2, reset", "d0, d1, d2, d3, d4, d5, d6, d7, enable, dc, cs0, cs1, cs2 [, reset]", "u8x8_byte_ks0108"),

    U8X8_SETPIN_SED1520("", "u8x8_SetPin_SED1520", "u8x8_byte_sed1520", "u8x8_gpio_and_delay_arduino", "uint8_t d0, uint8_t d1, uint8_t d2, uint8_t d3, uint8_t d4, uint8_t d5, uint8_t d6, uint8_t d7, uint8_t dc, uint8_t e1, uint8_t e2, uint8_t reset", "d0, d1, d2, d3, d4, d5, d6, d7, dc, e1, e2, reset", "d0, d1, d2, d3, d4, d5, d6, d7, dc, e1, e2, reset", "u8x8_byte_sed1520");

    private String name;

    private String setPinFunction;

    private String arduinoComProcedure;

    private String arduinoGpioProcedure;

    private String pinsWithType;

    private String pinsPlain;

    private String pinsMarkdown;

    private String genericComProcedure;

    GlcdCommInterfaceInfo(String name, String setPinFunction, String arduinoComProcedure,
                          String arduinoGpioProcedure, String pinsWithType, String pinsPlain, String pinsMarkdown,
                          String genericComProcedure) {
        this.name = name;
        this.setPinFunction = setPinFunction;
        this.arduinoComProcedure = arduinoComProcedure;
        this.arduinoGpioProcedure = arduinoGpioProcedure;
        this.pinsWithType = pinsWithType;
        this.pinsPlain = pinsPlain;
        this.pinsMarkdown = pinsMarkdown;
        this.genericComProcedure = genericComProcedure;
    }

    public String getName() {
        return name;
    }

    public String getSetPinFunction() {
        return setPinFunction;
    }

    public String getArduinoComProcedure() {
        return arduinoComProcedure;
    }

    public String getArduinoGpioProcedure() {
        return arduinoGpioProcedure;
    }

    public String getPinsWithType() {
        return pinsWithType;
    }

    public String getPinsPlain() {
        return pinsPlain;
    }

    public String getPinsMarkdown() {
        return pinsMarkdown;
    }

    public String getGenericComProcedure() {
        return genericComProcedure;
    }
}
