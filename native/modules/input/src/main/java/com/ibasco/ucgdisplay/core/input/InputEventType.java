/*-
 * ========================START=================================
 * UCGDisplay :: Native :: Input
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
package com.ibasco.ucgdisplay.core.input;

import java.util.List;

/**
 * Holds information about an input event
 *
 * @author Rafael Ibasco
 */
public class InputEventType {
    private String key;
    private int value;
    private List<InputEventCode> codes;
    private boolean state;

    public InputEventType(String key, int value, List<InputEventCode> codes, boolean state) {
        this.key = key;
        this.value = value;
        this.codes = codes;
        this.state = state;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public boolean hasState() {
        return state;
    }

    public List<InputEventCode> getCodes() {
        return codes;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("InputEventType{");
        sb.append("key='").append(key).append('\'');
        sb.append(", value=").append(value);
        sb.append(", codes=").append(codes == null ? "null" : codes.size());
        sb.append(", state=").append(state);
        sb.append('}');
        return sb.toString();
    }
}
