package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdProtocol;

public class GlcdSetupInfo {
    private int protocols;
    private String function;

    public GlcdSetupInfo(String setupFunction, int supportedProtocols) {
        this.protocols = supportedProtocols;
        this.function = setupFunction;
    }

    public int getProtocols() {
        return protocols;
    }

    public String getFunction() {
        return function;
    }

    public boolean isSupported(GlcdProtocol protocol) {
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
