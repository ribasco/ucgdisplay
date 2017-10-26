package com.ibasco.pidisplay.drivers.lcd.hitachi;

import com.ibasco.pidisplay.drivers.lcd.hitachi.enums.LcdPin;
import com.pi4j.io.gpio.Pin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A utility class to create mapping between {@link LcdPin} and {@link Pin}
 *
 * @author Rafael Ibasco
 */
public class LcdPinMapConfig {
    private HashMap<LcdPin, Pin> pinMap;

    public LcdPinMapConfig() {
        this.pinMap = new HashMap<>();
    }

    public LcdPinMapConfig map(LcdPin lcdPin, Pin pin) {
        pinMap.put(lcdPin, pin);
        return this;
    }

    public Set<Map.Entry<LcdPin, Pin>> getAllPins() {
        return this.pinMap.entrySet();
    }

    boolean isMapped(LcdPin pin) {
        return pinMap.containsKey(pin);
    }

    Pin getMappedPin(LcdPin pin) {
        return pinMap.get(pin);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LcdPin, Pin> pinEntry : pinMap.entrySet()) {
            sb.append(pinEntry.getKey());
            sb.append("=");
            sb.append(pinEntry.getValue());
            sb.append(", ");
        }
        return sb.toString();
    }
}
