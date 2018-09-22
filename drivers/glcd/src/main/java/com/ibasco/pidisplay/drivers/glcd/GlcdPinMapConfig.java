package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdPinMappingException;

import java.util.HashMap;
import java.util.Map;

public class GlcdPinMapConfig {
    private Map<GlcdPin, Byte> pinMap;

    public GlcdPinMapConfig() {
        this.pinMap = new HashMap<>();
    }

    public GlcdPinMapConfig map(GlcdPin glcdPin, int value) {
        if (value > 255)
            throw new RuntimeException("", new GlcdPinMappingException("Invalid pin number", this));
        pinMap.put(glcdPin, (byte) value);
        return this;
    }

    boolean isMapped(GlcdPin pin) {
        return pinMap.containsKey(pin);
    }

    boolean isEmpty() {
        return pinMap.isEmpty();
    }

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
