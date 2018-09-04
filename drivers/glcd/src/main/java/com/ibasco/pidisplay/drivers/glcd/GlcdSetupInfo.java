package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdCommInterface;

public class GlcdSetupInfo {
    private int protocols;
    private String function;

    GlcdSetupInfo(String setupFunction, int supportedProtocols) {
        this.protocols = supportedProtocols;
        this.function = setupFunction;
    }

    int getProtocols() {
        return protocols;
    }

    String getFunction() {
        return function;
    }

    boolean isSupported(GlcdCommInterface protocol) {
        return (protocols & protocol.getValue()) > 0;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GlcdSetupInfo{");
        sb.append("protocols=").append(protocols);
        sb.append(", function='").append(function).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
