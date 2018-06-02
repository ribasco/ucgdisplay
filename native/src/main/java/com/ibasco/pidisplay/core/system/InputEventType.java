package com.ibasco.pidisplay.core.system;

import java.util.List;

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
