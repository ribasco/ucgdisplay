package com.ibasco.pidisplay.core.enums;

import java.util.Arrays;

/**
 * Event types are groupings of codes under a logical input construct. Each
 * type has a set of applicable codes to be used in generating events.
 *
 * @author Rafael Ibasco
 * @see <a href="https://www.kernel.org/doc/Documentation/input/event-codes.txt">Event Codes</a>
 */
@SuppressWarnings("unused")
public enum InputEventType {
    /**
     * Used as markers to separate events. Events may be separated in time or in
     * space, such as with the multitouch protocol
     */
    EV_SYN(0x00),
    /**
     * Used to describe state changes of keyboards, buttons, or other key-like
     * devices.
     */
    EV_KEY(0x01),
    /**
     * Used to describe relative axis value changes, e.g. moving the mouse 5 units
     * to the left.
     */
    EV_REL(0x02),
    /**
     * Used to describe absolute axis value changes, e.g. describing the
     * coordinates of a touch on a touchscreen.
     */
    EV_ABS(0x03),
    /**
     * Used to describe miscellaneous input data that do not fit into other types.
     */
    EV_MSC(0x04),
    /**
     * Used to describe binary state input switches.
     */
    EV_SW(0x05),
    /**
     * Used to turn LEDs on devices on and off. Events that are used for input and output to set and query the
     * state of various LEDs on devices.
     */
    EV_LED(0x11),
    /**
     * Used to output sound to devices.
     */
    EV_SND(0x12),
    /**
     * Used for autorepeating devices. Events that are used for specifying autorepeating events.
     */
    EV_REP(0x14),
    /**
     * Used to send force feedback commands to an input device. Events that are used to initialize a force feedback
     * capable device and to cause such device to feedback.
     */
    EV_FF(0x15),
    /**
     * A special type for power button and switch input. Events that are a special type of event used specifically for
     * power management. Its usage is not well defined. To be addressed later.
     */
    EV_PWR(0x16),
    /**
     * Used to receive force feedback device status.
     */
    EV_FF_STATUS(0x17),
    EV_MAX(0x1f),
    EV_CNT(EV_MAX.value + 1),
    UNKNOWN(-1);

    private int value;

    InputEventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    InputEventType override(int value) {
        this.value = value;
        return this;
    }

    public static InputEventType toEventType(int value) {
        return Arrays.stream(InputEventType.values()).filter(p -> p.getValue() == value)
                .findFirst()
                .orElse(InputEventType.UNKNOWN.override(value));
    }

    @Override
    public String toString() {
        return String.format("%s(%d)", this.name(), this.value);
    }
}
