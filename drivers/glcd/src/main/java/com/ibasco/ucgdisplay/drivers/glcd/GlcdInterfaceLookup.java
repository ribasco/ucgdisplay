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
// THIS IS AN AUTO-GENERATED CODE!! DO NOT MODIFY (Last updated: Sat, 9 Jan 2021 08:42:12 +0800)
//
package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdCommProtocol;

import java.util.ArrayList;

public class GlcdInterfaceLookup {
  private static final ArrayList<GlcdInterfaceInfo> interfaceList = new ArrayList<>();

  static {
    interfaceList.add(new GlcdInterfaceInfo(0, GlcdCommProtocol.SPI_SW_4WIRE, "4W_SW_SPI", "u8x8_SetPin_4Wire_SW_SPI", "u8x8_byte_arduino_4wire_sw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t clock, uint8_t data, uint8_t cs, uint8_t dc, uint8_t reset = U8X8_PIN_NONE", "clock, data, cs, dc, reset", "clock, data, cs, dc [, reset]", "u8x8_byte_4wire_sw_spi"));
    interfaceList.add(new GlcdInterfaceInfo(1, GlcdCommProtocol.SPI_HW_4WIRE, "4W_HW_SPI", "u8x8_SetPin_4Wire_HW_SPI", "u8x8_byte_arduino_hw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t cs, uint8_t dc, uint8_t reset = U8X8_PIN_NONE", "cs, dc, reset", "cs, dc [, reset]", "uC specific"));
    interfaceList.add(new GlcdInterfaceInfo(2, GlcdCommProtocol.PARALLEL_6800, "6800", "u8x8_SetPin_8Bit_6800", "u8x8_byte_8bit_6800mode", "u8x8_gpio_and_delay_arduino", "uint8_t d0, uint8_t d1, uint8_t d2, uint8_t d3, uint8_t d4, uint8_t d5, uint8_t d6, uint8_t d7, uint8_t enable, uint8_t cs, uint8_t dc, uint8_t reset = U8X8_PIN_NONE", "d0, d1, d2, d3, d4, d5, d6, d7, enable, cs, dc, reset", "d0, d1, d2, d3, d4, d5, d6, d7, enable, cs, dc [, reset]", "u8x8_byte_8bit_6800mode"));
    interfaceList.add(new GlcdInterfaceInfo(3, GlcdCommProtocol.PARALLEL_8080, "8080", "u8x8_SetPin_8Bit_8080", "u8x8_byte_arduino_8bit_8080mode", "u8x8_gpio_and_delay_arduino", "uint8_t d0, uint8_t d1, uint8_t d2, uint8_t d3, uint8_t d4, uint8_t d5, uint8_t d6, uint8_t d7, uint8_t enable, uint8_t cs, uint8_t dc, uint8_t reset = U8X8_PIN_NONE", "d0, d1, d2, d3, d4, d5, d6, d7, enable, cs, dc, reset", "d0, d1, d2, d3, d4, d5, d6, d7, enable, cs, dc [, reset]", "u8x8_byte_8bit_8080mode"));
    interfaceList.add(new GlcdInterfaceInfo(4, GlcdCommProtocol.SPI_SW_3WIRE, "3W_SW_SPI", "u8x8_SetPin_3Wire_SW_SPI", "u8x8_byte_arduino_3wire_sw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t clock, uint8_t data, uint8_t cs, uint8_t reset = U8X8_PIN_NONE", "clock, data, cs, reset", "clock, data, cs [, reset]", "u8x8_byte_3wire_sw_spi"));
    interfaceList.add(new GlcdInterfaceInfo(5, GlcdCommProtocol.SPI_HW_3WIRE, "3W_HW_SPI", "u8x8_SetPin_3Wire_HW_SPI", "u8x8_byte_arduino_3wire_hw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t cs, uint8_t reset = U8X8_PIN_NONE", "cs, reset", "cs [, reset]", "uC specific"));
    interfaceList.add(new GlcdInterfaceInfo(6, GlcdCommProtocol.I2C_SW, "SW_I2C", "u8x8_SetPin_SW_I2C", "u8x8_byte_arduino_sw_i2c", "u8x8_gpio_and_delay_arduino", "uint8_t clock, uint8_t data, uint8_t reset = U8X8_PIN_NONE", "clock,  data,  reset", "clock,  data [,  reset]", "u8x8_byte_sw_i2c"));
    interfaceList.add(new GlcdInterfaceInfo(7, GlcdCommProtocol.I2C_HW, "HW_I2C", "u8x8_SetPin_HW_I2C", "u8x8_byte_arduino_hw_i2c", "u8x8_gpio_and_delay_arduino", "uint8_t reset = U8X8_PIN_NONE, uint8_t clock = U8X8_PIN_NONE, uint8_t data = U8X8_PIN_NONE", "reset, clock, data", "[reset [, clock, data]]", "uC specific"));
    interfaceList.add(new GlcdInterfaceInfo(8, GlcdCommProtocol.SPI_SW_4WIRE_ST7920, "SW_SPI", "u8x8_SetPin_3Wire_SW_SPI", "u8x8_byte_arduino_4wire_sw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t clock, uint8_t data, uint8_t cs, uint8_t reset = U8X8_PIN_NONE", "clock, data, cs, reset", "clock, data, cs [, reset]", "u8x8_byte_4wire_sw_spi"));
    interfaceList.add(new GlcdInterfaceInfo(9, GlcdCommProtocol.SPI_HW_4WIRE_ST7920, "HW_SPI", "u8x8_SetPin_ST7920_HW_SPI", "u8x8_byte_arduino_hw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t cs, uint8_t reset = U8X8_PIN_NONE", "cs, reset", "cs [, reset]", "uC specific"));
    interfaceList.add(new GlcdInterfaceInfo(10, GlcdCommProtocol.I2C_HW_2ND, "2ND_HW_I2C", "u8x8_SetPin_HW_I2C", "u8x8_byte_arduino_2nd_hw_i2c", "u8x8_gpio_and_delay_arduino", "uint8_t reset = U8X8_PIN_NONE", "reset", "[reset]", "uC specific"));
    interfaceList.add(new GlcdInterfaceInfo(11, GlcdCommProtocol.PARALLEL_6800_KS0108, "", "u8x8_SetPin_KS0108", "u8x8_byte_arduino_ks0108", "u8x8_gpio_and_delay_arduino", "uint8_t d0, uint8_t d1, uint8_t d2, uint8_t d3, uint8_t d4, uint8_t d5, uint8_t d6, uint8_t d7, uint8_t enable, uint8_t dc, uint8_t cs0, uint8_t cs1, uint8_t cs2, uint8_t reset = U8X8_PIN_NONE", "d0, d1, d2, d3, d4, d5, d6, d7, enable, dc, cs0, cs1, cs2, reset", "d0, d1, d2, d3, d4, d5, d6, d7, enable, dc, cs0, cs1, cs2 [, reset]", "u8x8_byte_ks0108"));
    interfaceList.add(new GlcdInterfaceInfo(12, GlcdCommProtocol.SPI_HW_4WIRE_2ND, "2ND_4W_HW_SPI", "u8x8_SetPin_4Wire_HW_SPI", "u8x8_byte_arduino_2nd_hw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t cs, uint8_t dc, uint8_t reset = U8X8_PIN_NONE", "cs, dc, reset", "cs, dc [, reset]", "uC specific"));
    interfaceList.add(new GlcdInterfaceInfo(13, GlcdCommProtocol.SED1520, "", "u8x8_SetPin_SED1520", "u8x8_byte_sed1520", "u8x8_gpio_and_delay_arduino", "uint8_t d0, uint8_t d1, uint8_t d2, uint8_t d3, uint8_t d4, uint8_t d5, uint8_t d6, uint8_t d7, uint8_t dc, uint8_t e1, uint8_t e2, uint8_t reset", "d0, d1, d2, d3, d4, d5, d6, d7, dc, e1, e2, reset", "d0, d1, d2, d3, d4, d5, d6, d7, dc, e1, e2, reset", "u8x8_byte_sed1520"));
    interfaceList.add(new GlcdInterfaceInfo(14, GlcdCommProtocol.SPI_HW_ST7920_2ND, "2ND_HW_SPI", "u8x8_SetPin_ST7920_HW_SPI", "u8x8_byte_arduino_2nd_hw_spi", "u8x8_gpio_and_delay_arduino", "uint8_t cs, uint8_t reset = U8X8_PIN_NONE", "cs, reset", "cs [, reset]", "uC specific"));
  }

  public static ArrayList<GlcdInterfaceInfo> getInfoList() {
    return GlcdInterfaceLookup.interfaceList;
  }
}
