package com.ibasco.ucgdisplay.examples.lcd.enums;

import java.util.Arrays;

public enum MsgType {
    COMMAND(0x0),
    REQUEST(0x1),
    RESPONSE(0x2);

    private int code;

    MsgType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MsgType valueOf(int code) {
        return Arrays.stream(MsgType.values()).filter(v -> v.code == code).findFirst().orElse(null);
    }
}
