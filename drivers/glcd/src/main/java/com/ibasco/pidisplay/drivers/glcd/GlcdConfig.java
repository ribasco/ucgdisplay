package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdCommInterface;
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
    private GlcdCommInterface commInterface;
    private GlcdRotation rotation;
    private GlcdPinMapConfig pinMap;
    private int deviceAddress = -1;

    public GlcdConfig() {
    }

    public GlcdConfig(GlcdDisplay display, GlcdCommInterface commInterface, GlcdRotation rotation, GlcdPinMapConfig pinMapConfig, int deviceAddress) {
        this.display = display;
        this.rotation = rotation;
        this.commInterface = commInterface;
        this.pinMap = pinMapConfig;
        this.deviceAddress = deviceAddress;
    }

    public int getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(int deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public void setDisplay(GlcdDisplay display) {
        this.display = display;
    }

    public void setCommInterface(GlcdCommInterface commInterface) {
        this.commInterface = commInterface;
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

    public GlcdCommInterface getCommInterface() {
        return commInterface;
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
        if (commInterface == null) {
            throw new RuntimeException("Unable to obtain setup procedure",
                    new GlcdConfigException("Protocol not set", this));
        }

        GlcdSetupInfo setupInfo = Arrays.stream(display.getSetupDetails())
                .filter(setup -> setup.isSupported(commInterface))
                .findFirst()
                .orElse(null);

        if (setupInfo == null)
            throw new RuntimeException("Unable to locate setup procedure",
                    new GlcdException(String.format("Could not find a suitable setup procedure for commInterface '%s'", commInterface.name())));

        log.debug("Found setup procedure for display (Display: {}, Protocol: {}, Setup Proc: {}))", display.getName(), commInterface.name(), setupInfo.getFunction());

        return setupInfo.getFunction();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GlcdConfig{");
        sb.append("display=").append(display);
        sb.append(", commInterface=").append(commInterface);
        sb.append(", rotation=").append(rotation);
        sb.append(", pinMap=").append(pinMap);
        sb.append('}');
        return sb.toString();
    }
}
