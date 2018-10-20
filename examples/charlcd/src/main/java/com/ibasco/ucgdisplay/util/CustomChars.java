package com.ibasco.ucgdisplay.util;

import com.ibasco.ucgdisplay.drivers.clcd.CharData;

public class CustomChars {
    public static final CharData SIGNAL_20_PCT = new CharData("SIGNAL_20_PCT", "SP_20", new byte[]{
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b10000});
    public static final CharData SIGNAL_40_PCT = new CharData("SIGNAL_40_PCT", "SP_40", new byte[]{
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b01000,
            0b11000
    });
    public static final CharData SIGNAL_60_PCT = new CharData("SIGNAL_60_PCT", "SP_60", new byte[]{
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b00100,
            0b01100,
            0b11100});
    public static final CharData SIGNAL_80_PCT = new CharData("SIGNAL_80_PCT", "SP_80", new byte[]{
            0b00000,
            0b00000,
            0b00000,
            0b00000,
            0b00010,
            0b00110,
            0b01110,
            0b11110});
    public static final CharData SIGNAL_100_PCT = new CharData("SIGNAL_100_PCT", "SP_100", new byte[]{
            0b00000,
            0b00000,
            0b00000,
            0b00001,
            0b00011,
            0b00111,
            0b01111,
            0b11111});
}
