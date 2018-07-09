package com.ibasco.pidisplay.drivers.glcd;

import com.ibasco.pidisplay.drivers.glcd.enums.GlcdPin;
import com.ibasco.pidisplay.drivers.glcd.exceptions.GlcdPinMappingException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GlcdPinMapConfig {
    private HashMap<GlcdPin, Byte> pinMap;

    public GlcdPinMapConfig() {
        this.pinMap = new HashMap<>();
    }

    public GlcdPinMapConfig map(GlcdPin glcdPin, int pinNumber) {
        if (pinNumber > 255)
            throw new RuntimeException("", new GlcdPinMappingException("Invalid pin number", this));
        pinMap.put(glcdPin, (byte) pinNumber);
        return this;
    }

    boolean isMapped(GlcdPin pin) {
        return pinMap.containsKey(pin);
    }

    boolean isEmpty() {
        return pinMap.isEmpty();
    }

    byte[] build() {
        if (pinMap.isEmpty())
            return null;
        int size = GlcdPin.values().length - 2; //exclude last two aliases
        byte[] data = new byte[size];
        Arrays.fill(data, (byte) 0);
        for (Map.Entry<GlcdPin, Byte> entry : pinMap.entrySet()) {
            GlcdPin pin = entry.getKey();
            Byte pNum = entry.getValue();
            data[pin.getIndex()] = pNum;
        }
        return data;
    }
}
