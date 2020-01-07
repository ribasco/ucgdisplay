/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Input
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
package com.ibasco.ucgdisplay.core.input;

import java.util.Map;

public class InputEventCode {
    private String key;
    private int code;
    private int state;
    private Map<String, Integer> absData;

    public InputEventCode(String key, int code) {
        this(key, code, -1);
    }

    public InputEventCode(String key, int code, int state) {
        this.key = key;
        this.code = code;
        this.state = state;
    }

    public Map<String, Integer> getAbsData() {
        return absData;
    }

    public String getKey() {
        return key;
    }

    public int getCode() {
        return code;
    }

    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("InputEventCode{");
        sb.append("key='").append(key).append('\'');
        sb.append(", code=").append(code);
        sb.append(", state=").append(state);
        sb.append('}');
        return sb.toString();
    }
}
