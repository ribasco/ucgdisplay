package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdBusInterface;

/**
 * Holds information of u8g2 setup procedure and supported bus protocols of a {@link GlcdDisplay}
 *
 * @author Rafael Ibasco
 */
public final class GlcdSetupInfo {
    private int protocols;
    private String function;

    GlcdSetupInfo(String setupFunction, int supportedProtocols) {
        this.protocols = supportedProtocols;
        this.function = setupFunction;
    }

    int getProtocols() {
        return protocols;
    }

    public String getFunction() {
        return function;
    }

    public boolean isSupported(GlcdBusInterface protocol) {
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
