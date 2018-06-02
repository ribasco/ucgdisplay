package com.ibasco.pidisplay.core.system;

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
