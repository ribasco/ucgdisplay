package com.ibasco.pidisplay.core.input;

@SuppressWarnings("unused")
public class InputEventType {
    /**
     * Used as markers to separate events. Events may be separated in time or in
     * space, such as with the multitouch protocol
     */
    public static final int EV_SYN = 0x00;
    /**
     * Used to describe state changes of keyboards, buttons, or other key-like
     * devices.
     */
    public static final int EV_KEY = 0x01;
    /**
     * Used to describe relative axis value changes, e.g. moving the mouse 5 units
     * to the left.
     */
    public static final int EV_REL = 0x02;
    /**
     * Used to describe absolute axis value changes, e.g. describing the
     * coordinates of a touch on a touchscreen.
     */
    public static final int EV_ABS = 0x03;
    /**
     * Used to describe miscellaneous input data that do not fit into other types.
     */
    public static final int EV_MSC = 0x04;
    /**
     * Used to describe binary state input switches.
     */
    public static final int EV_SW = 0x05;
    /**
     * Used to turn LEDs on devices on and off. Events that are used for input and output to set and query the
     * state of various LEDs on devices.
     */
    public static final int EV_LED = 0x11;
    /**
     * Used to output sound to devices.
     */
    public static final int EV_SND = 0x12;
    /**
     * Used for autorepeating devices. Events that are used for specifying autorepeating events.
     */
    public static final int EV_REP = 0x14;
    /**
     * Used to send force feedback commands to an input device. Events that are used to initialize a force feedback
     * capable device and to cause such device to feedback.
     */
    public static final int EV_FF = 0x15;
    /**
     * A special type for power button and switch input. Events that are a special type of event used specifically for
     * power management. Its usage is not well defined. To be addressed later.
     */
    public static final int EV_PWR = 0x16;
    /**
     * Used to receive force feedback device status.
     */
    public static final int EV_FF_STATUS = 0x17;
    public static final int EV_MAX = 0x1f;
    public static final int EV_CNT = (EV_MAX + 1);
}
