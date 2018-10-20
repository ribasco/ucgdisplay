/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native Library
 * Filename: U8g2Message.java
 *
 * ---------------------------------------------------------
 * %%
 * Copyright (C) 2018 Universal Character/Graphics display library
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
package com.ibasco.ucgdisplay.core.u8g2;

import java.util.Arrays;

@SuppressWarnings("unused")
public enum U8g2Message {

    U8X8_MSG_GPIO_AND_DELAY_INIT(0x28, 0, "GPIO Initialization"),
    U8X8_MSG_GPIO_D0(0x40, 0, "Data Pin 0"),
    U8X8_MSG_GPIO_D1(0x41, 0, "Data Pin 1"),
    U8X8_MSG_GPIO_D2(0x42, 0, "Data Pin 2"),
    U8X8_MSG_GPIO_D3(0x43, 0, "Data Pin 3"),
    U8X8_MSG_GPIO_D4(0x44, 0, "Data Pin 4"),
    U8X8_MSG_GPIO_D5(0x45, 0, "Data Pin 5"),
    U8X8_MSG_GPIO_D6(0x46, 0, "Data Pin 6"),
    U8X8_MSG_GPIO_D7(0x47, 0, "Data Pin 7"),
    U8X8_MSG_GPIO_CS(0x49, 0, "Chip Select"),
    U8X8_MSG_GPIO_E(0x48, 0, "Clock/Enable"),
    U8X8_MSG_GPIO_CS1(0x4e, 0, "Chip Select 1"),
    U8X8_MSG_GPIO_CS2(0x4f, 0, "Chip Select 2"),
    U8X8_MSG_GPIO_RESET(0x4b, 0, "Reset"),
    U8X8_MSG_GPIO_DC(0x4a, 0, "Data/Command"),
    U8X8_MSG_BYTE_INIT(0x14, 1, "Byte Initialization"),
    U8X8_MSG_DELAY_MILLI(0x29, 1, "Delay (Milliseconds)"),
    U8X8_MSG_DELAY_NANO(0x2c, 1, "Delay (Nanoseconds)"),
    U8X8_MSG_BYTE_SEND(0x17, 1, "Byte Send Sequence"),
    U8X8_MSG_BYTE_START_TRANSFER(0x18, 1, "Start of Byte Transfer"),
    U8X8_MSG_BYTE_END_TRANSFER(0x19, 1, "End of Byte Transfer"),
    U8X8_MSG_BYTE_SET_DC(0x20, 1, "Set DC Pin"),
    U8X8_MSG_GPIO_I2C_DATA(0x4d, 1, "I2C Data"),
    U8X8_MSG_GPIO_I2C_CLOCK(0x4c, 1, "I2C Clock"),
    U8X8_MSG_DELAY_I2C(0x2d, 1, "I2C delay"),
    U8G2_BYTE_SEND_INIT(0x1C, 1, "Start of Byte Send"), //custom event, not official u8g2 message
    U8X8_MSG_START(0xFE, 2, "Start of Message"), //this is a non-u8g2 message and should only be used internally
    U8X8_MSG_END(0xFF, 2, "End of Message"); //this is a non-u8g2 message and should only be used internally

    private int code;
    private int type;
    private String description;

    U8g2Message(int code, int type, String description) {
        this.code = code;
        this.description = description;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public static U8g2Message valueOf(int code) {
        return Arrays.stream(values()).filter(p -> p.code == code).findFirst().orElse(null);
    }
}
