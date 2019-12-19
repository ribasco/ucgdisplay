/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2019 Universal Character/Graphics display library
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
package com.ibasco.ucgdisplay.drivers.glcd.exceptions;

import com.ibasco.ucgdisplay.drivers.glcd.GlcdPinMapConfig;

public class GlcdPinMappingException extends RuntimeException {
    private GlcdPinMapConfig pinMapConfig;

    public GlcdPinMappingException(String message, GlcdPinMapConfig pinMapConfig) {
        super(message);
        this.pinMapConfig = pinMapConfig;
    }

    public GlcdPinMapConfig getPinMapConfig() {
        return pinMapConfig;
    }
}
