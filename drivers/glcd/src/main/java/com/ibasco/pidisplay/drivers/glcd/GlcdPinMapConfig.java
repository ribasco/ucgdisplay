package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdPinMappingException;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for mapping a {@link GlcdPin} with device pin number
 *
 * @author Rafael Ibasco
 */
public class GlcdPinMapConfig {
    private Map<GlcdPin, Byte> pinMap;

    public GlcdPinMapConfig() {
        this.pinMap = new HashMap<>();
    }

    /**
     * Maps a {@link GlcdPin} to a device pin number
     *
     * @param glcdPin
     *         A {@link GlcdPin} to be mapped
     * @param value
     *         The pin number of the device to be mapped
     *
     * @return GlcdPinMapConfig
     */
    public GlcdPinMapConfig map(GlcdPin glcdPin, int value) {
        if (value > 255)
            throw new RuntimeException("", new GlcdPinMappingException("Invalid pin number", this));
        pinMap.put(glcdPin, (byte) value);
        return this;
    }

    /**
     * Check if the provided pin is mapped
     *
     * @param pin
     *         The {@link GlcdPin}
     *
     * @return <code>True</code> if the provided {@link GlcdPin} is mapped to a device pin
     */
    boolean isMapped(GlcdPin pin) {
        return pinMap.containsKey(pin);
    }

    /**
     * @return <code>True</code> if the pin map is empty
     */
    boolean isEmpty() {
        return pinMap.isEmpty();
    }

    /**
     * <p>Converts the internal pin map configuration to a primitive byte array which will
     * later be used by the native driver. Each index represents a known device pin.</p>
     *
     * @return A primitive byte array contating
     */
    byte[] toByteArray() {
        byte[] data = new byte[16];
        if (!pinMap.isEmpty()) {
            for (Map.Entry<GlcdPin, Byte> entry : pinMap.entrySet()) {
                GlcdPin pin = entry.getKey();
                Byte pNum = entry.getValue();
                data[pin.getIndex()] = pNum;
            }
        }
        return data;
    }
}
