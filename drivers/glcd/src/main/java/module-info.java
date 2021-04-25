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
module ucgd.drivers.glcd {
    exports com.ibasco.ucgdisplay.drivers.glcd;
    exports com.ibasco.ucgdisplay.drivers.glcd.enums;
    exports com.ibasco.ucgdisplay.drivers.glcd.exceptions;
    exports com.ibasco.ucgdisplay.drivers.glcd.utils;

    requires ucgd.nativ.graphics;
    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires ucgd.common;
    requires org.apache.commons.codec;

    provides com.ibasco.ucgdisplay.common.drivers.DisplayDriver with com.ibasco.ucgdisplay.drivers.glcd.GlcdDriver;
}
