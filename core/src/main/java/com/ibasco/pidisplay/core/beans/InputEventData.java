package com.ibasco.pidisplay.core.beans;

import com.ibasco.pidisplay.core.enums.InputEventCode;
import com.ibasco.pidisplay.core.enums.InputEventType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class InputEventData {

    private LocalDateTime dateTime;

    private int seconds;

    private int microseconds;

    private InputEventType type;

    private InputEventCode code;

    private int value;

    public InputEventData(int seconds, int microseconds, int type, int code, int value) {
        this.dateTime = Instant.ofEpochSecond(seconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.seconds = seconds;
        this.microseconds = microseconds;
        this.type = InputEventType.toEventType(type);
        this.code = InputEventCode.toKeyCode(code);
        this.value = value;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMicroseconds() {
        return microseconds;
    }

    public InputEventType getType() {
        return type;
    }

    public InputEventCode getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("dateTime", dateTime)
                .append("type", type)
                .append("code", code)
                .append("value", value)
                .build();
    }
}
