/*-
 * ========================START=================================
 * Organization: Universal Character/Graphics display library
 * Project: UCGDisplay :: Native :: Graphics
 * Filename: U8g2MessageEvent.java
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

abstract public class U8g2MessageEvent {
    private final U8g2Message message;
    private final int value;

    public U8g2MessageEvent(byte msg, byte value) {
        this(Byte.toUnsignedInt(msg), Byte.toUnsignedInt(value));
    }

    public U8g2MessageEvent(int msg, int value) {
        this(U8g2Message.valueOf(msg), value);
    }

    public U8g2MessageEvent(U8g2Message message, int value) {
        this.message = message;
        this.value = value;
    }

    public U8g2Message getMessage() {
        return message;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return message + " = " + Integer.toHexString(value).toUpperCase();
    }
}
