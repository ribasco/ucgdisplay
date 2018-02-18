package com.ibasco.pidisplay.core.ui;

/**
 * A collection of custom lcd characters
 *
 * @author Rafael Ibasco
 */
public class CharDefinitions {
    public static final CharData LEFT_ARROW = new CharData("CD_LEFT_ARROW", "CLA", new byte[]{0x00, 0x04, 0x08, 0x1F, 0x08, 0x04, 0x00, 0x00});
    public static final CharData RIGHT_ARROW = new CharData("CD_RIGHT_ARROW", "CRA", new byte[]{0x00, 0x04, 0x02, 0x1F, 0x02, 0x04, 0x00, 0x00});
    public static final CharData UP_ARROW = new CharData("CD_UP_ARROW", "CUA", new byte[]{0x04, 0x0E, 0x15, 0x04, 0x04, 0x04, 0x04, 0x04});
    public static final CharData DOWN_ARROW = new CharData("CD_DOWN_ARROW", "CDA", new byte[]{0x04, 0x04, 0x04, 0x04, 0x04, 0x15, 0x0E, 0x04});
    public static final CharData BULLET_POINT = new CharData("CD_BULLET_POINT", "CBP", new byte[]{0x00, 0x00, 0x0E, 0x0E, 0x0E, 0x0E, 0x00, 0x00});
}
