package com.ibasco.ucgdisplay.examples.lcd.enums;

import java.util.Arrays;

public enum MsgStatus {
    NACK(0x0),
    ACK(0x1);

    private int code;

    MsgStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MsgStatus valueOf(boolean code) {
        return valueOf(code ? 0x1 : 0x0);
    }

    public static MsgStatus valueOf(int code) {
        return Arrays.stream(MsgStatus.values()).filter(v -> v.code == code).findFirst().orElse(null);
    }
}
