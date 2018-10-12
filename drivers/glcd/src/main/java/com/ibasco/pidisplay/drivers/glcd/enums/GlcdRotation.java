package com.ibasco.pidisplay.drivers.glcd.enums;

import com.ibasco.pidisplay.drivers.glcd.U8g2DisplayDriver;

import static com.ibasco.pidisplay.drivers.glcd.U8g2DisplayDriver.*;

/**
 * Indicates the rotation type of the display
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings("unused")
public enum GlcdRotation {
    /**
     * No rotation
     */
    ROTATION_NONE(ROTATION_R0),
    /**
     * 90 Degrees clockwise rotation
     */
    ROTATION_90(ROTATION_R1),
    /**
     * 180 Degrees clockwise rotation
     */
    ROTATION_180(ROTATION_R2),
    /**
     * 270 Degrees clockwise rotation
     */
    ROTATION_270(ROTATION_R3),
    /**
     * No rotation. Landscape, display content is mirrored
     */
    ROTATION_MIRROR(U8g2DisplayDriver.ROTATION_MIRROR);

    private int value;

    GlcdRotation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
