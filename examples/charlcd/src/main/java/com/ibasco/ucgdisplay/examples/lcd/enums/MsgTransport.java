package com.ibasco.ucgdisplay.examples.lcd.enums;

import java.util.Arrays;

public enum MsgTransport {
    I2C(0x0),
    SERIAL(0x1);

    private int code;

    MsgTransport(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MsgTransport valueOf(int code) {
        return Arrays.stream(MsgTransport.values()).filter(v -> v.code == code).findFirst().orElse(null);
    }
}
