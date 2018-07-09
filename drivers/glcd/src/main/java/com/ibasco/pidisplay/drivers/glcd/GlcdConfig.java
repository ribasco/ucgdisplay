package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdProtocol;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdRotation;
import com.ibasco.pidisplay.drivers.glcd.enums.GlcdSize;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdConfigException;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdException;
import org.slf4j.Logger;

import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Configuration class to be used by the glcd native library
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class GlcdConfig {

    public static final Logger log = getLogger(GlcdConfig.class);

    private GlcdDisplay display;
    private GlcdProtocol protocol;
    private GlcdRotation rotation;
    private GlcdPinMapConfig pinMap;

    public GlcdConfig() {
    }

    public GlcdConfig(GlcdDisplay display, GlcdProtocol protocol, GlcdRotation rotation, GlcdPinMapConfig pinMapConfig) {
        this.display = display;
        this.rotation = rotation;
        this.protocol = protocol;
        this.pinMap = pinMapConfig;
    }

    public void setDisplay(GlcdDisplay display) {
        this.display = display;
    }

    public void setProtocol(GlcdProtocol protocol) {
        this.protocol = protocol;
    }

    public void setRotation(GlcdRotation rotation) {
        this.rotation = rotation;
    }

    public void setPinMapConfig(GlcdPinMapConfig pinMap) {
        this.pinMap = pinMap;
    }

    public GlcdSize getDisplaySize() {
        return display.getDisplaySize();
    }

    public GlcdProtocol getProtocol() {
        return protocol;
    }

    public GlcdPinMapConfig getPinMap() {
        return pinMap;
    }

    public GlcdDisplay getDisplay() {
        return display;
    }

    public GlcdRotation getRotation() {
        return rotation;
    }

    String getSetupProcedure() {
        if (display == null) {
            throw new RuntimeException("Unable to obtain setup procedure",
                    new GlcdConfigException("Display has not been set", this)
            );
        }
        if (protocol == null) {
            throw new RuntimeException("Unable to obtain setup procedure",
                    new GlcdConfigException("Protocol not set", this));
        }

        GlcdSetupInfo setupInfo = Arrays.stream(display.getSetupDetails())
                .filter(setup -> setup.isSupported(protocol))
                .findFirst()
                .orElse(null);

        if (setupInfo == null)
            throw new RuntimeException("Unable to locate setup procedure",
                    new GlcdException(String.format("Could not find a suitable setup procedure for protocol '%s'", protocol.name())));

        log.debug("Found setup procedure for display (Display: {}, Protocol: {}, Setup Proc: {}))", display.getName(), protocol.name(), setupInfo.getFunction());

        return setupInfo.getFunction();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GlcdConfig{");
        sb.append("display=").append(display);
        sb.append(", protocol=").append(protocol);
        sb.append(", rotation=").append(rotation);
        sb.append(", pinMap=").append(pinMap);
        sb.append('}');
        return sb.toString();
    }
}
