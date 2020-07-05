/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
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
package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.drivers.glcd.enums.GlcdCommProtocol;

public class GlcdInterfaceInfo {

    private final int index;
    private final String name;
    private final GlcdCommProtocol protocol;
    private final String setPinFunction;
    private final String arduinoComProcedure;
    private final String arduinoGpioProcedure;
    private final String pinsWithType;
    private final String pinsPlain;
    private final String pinsMarkdown;
    private final String genericComProcedure;

    public GlcdInterfaceInfo(int index, GlcdCommProtocol protocol, String name, String setPinFunction, String arduinoComProcedure, String arduinoGpioProcedure, String pinsWithType, String pinsPlain, String pinsMarkdown, String genericComProcedure) {
        this.index = index;
        this.name = name;
        this.protocol = protocol;
        this.setPinFunction = setPinFunction;
        this.arduinoComProcedure = arduinoComProcedure;
        this.arduinoGpioProcedure = arduinoGpioProcedure;
        this.pinsWithType = pinsWithType;
        this.pinsPlain = pinsPlain;
        this.pinsMarkdown = pinsMarkdown;
        this.genericComProcedure = genericComProcedure;
    }

    public int getIndex() {
        return index;
    }

    public GlcdCommProtocol getProtocol() {
        return protocol;
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
