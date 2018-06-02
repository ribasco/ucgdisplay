package com.ibasco.pidisplay.core.system;

public class RawInputEvent {
    private InputDevice device;
    private long eventTime;
    private int type;
    private int code;
    private int value;
    private String codeName;
    private String typeName;

    public RawInputEvent(InputDevice device, long eventTime, int type, int code, int value, String codeName, String typeName) {
        this.device = device;
        this.eventTime = eventTime;
        this.type = type;
        this.code = code;
        this.value = value;
        this.codeName = codeName;
        this.typeName = typeName;
    }

    public InputDevice getDevice() {
        return device;
    }

    public long getEventTime() {
        return eventTime;
    }

    public int getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("device=").append(device.getName());
        sb.append(", eventTime=").append(eventTime);
        sb.append(", type=").append(type);
        sb.append(", code=").append(code);
        sb.append(", value=").append(value);
        sb.append(", codeName=").append(codeName);
        sb.append(", typeName=").append(typeName);
        return sb.toString();
    }
}
