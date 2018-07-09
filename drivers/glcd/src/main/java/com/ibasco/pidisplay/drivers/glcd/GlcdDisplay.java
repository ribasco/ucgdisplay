package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.*;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdDriverException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Holds important meta data information on a Graphics Display
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class GlcdDisplay {

    public static final Logger log = getLogger(GlcdDisplay.class);

    private String name;
    private GlcdController controller;
    private GlcdSetupInfo[] setupDetails;
    private GlcdSize displaySize;

    GlcdDisplay(GlcdController controller, String name, int tileWidth, int tileHeight, GlcdSetupInfo... setupInfo) {
        this.name = name;
        this.controller = controller;
        this.setupDetails = setupInfo;
        this.displaySize = GlcdSize.get(tileWidth, tileHeight);
    }

    public boolean hasProtocol(GlcdProtocol protocol) {
        for (GlcdSetupInfo info : setupDetails) {
            if ((info.getProtocols() & protocol.getValue()) > 0)
                return true;
        }
        return false;
    }

    public List<GlcdProtocol> getSupportedProtocols() {
        if (setupDetails == null || setupDetails.length <= 0)
            return null;
        List<GlcdProtocol> protocols = new ArrayList<>();
        for (GlcdSetupInfo setup : setupDetails) {
            for (GlcdProtocol protocol : GlcdProtocol.values()) {
                if ((setup.getProtocols() & protocol.getValue()) > 0)
                    protocols.add(protocol);
            }
        }
        return protocols;
    }

    public GlcdSize getDisplaySize() {
        return displaySize;
    }

    public GlcdController getController() {
        return controller;
    }

    public String getName() {
        return name;
    }

    public GlcdSetupInfo[] getSetupDetails() {
        return setupDetails;
    }

    public static void main(String[] args) {
        GlcdDisplay display = Glcd.ST7920.D_128x64;
        GlcdConfig config = new GlcdConfig();
        config.setDisplay(Glcd.ST7920.D_128x64);
        config.setProtocol(GlcdProtocol.SPI_ST7920);
        config.setRotation(GlcdRotation.ROTATION_90);
        config.setPinMapConfig(new GlcdPinMapConfig()
                .map(GlcdPin.SPI_CLOCK, 14)
                .map(GlcdPin.SPI_MOSI, 12)
                .map(GlcdPin.CS, 10)
        );

        try {
            GlcdDriver driver = new GlcdDriver(config);
            driver.setFont(GlcdFont.FONT_7X13_MF);
        } catch (GlcdDriverException e) {
            e.printStackTrace();
        }
    }
}
