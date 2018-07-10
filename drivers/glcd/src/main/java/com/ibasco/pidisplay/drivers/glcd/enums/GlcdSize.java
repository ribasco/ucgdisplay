/*
 * THIS IS AN AUTO-GENERATED CODE!! PLEASE DO NOT MODIFY (Last updated: Jul 10 2018 23:08:28)
 */

package com.ibasco.pidisplay.drivers.glcd.enums;

import java.util.Arrays;

@SuppressWarnings("unused")
public enum GlcdSize {
    SIZE_8x8(1, 1),
    SIZE_32x8(4, 1),
    SIZE_96x16(12, 2),
    SIZE_64x32(8, 4),
    SIZE_72x40(9, 5),
    SIZE_64x48(8, 6),
    SIZE_128x32(16, 4),
    SIZE_88x48(11, 6),
    SIZE_136x32(17, 4),
    SIZE_192x32(24, 4),
    SIZE_104x64(13, 8),
    SIZE_96x72(12, 9),
    SIZE_128x64(16, 8),
    SIZE_136x64(17, 8),
    SIZE_96x96(12, 12),
    SIZE_128x96(16, 12),
    SIZE_176x72(22, 9),
    SIZE_160x80(20, 10),
    SIZE_240x64(30, 8),
    SIZE_128x128(16, 16),
    SIZE_160x104(20, 13),
    SIZE_176x104(22, 13),
    SIZE_160x128(20, 16),
    SIZE_160x160(20, 20),
    SIZE_240x120(30, 15),
    SIZE_240x128(30, 16),
    SIZE_256x128(32, 16),
    SIZE_296x128(37, 16),
    SIZE_240x160(30, 20),
    SIZE_200x200(25, 25),
    SIZE_256x160(32, 20),
    SIZE_320x240(40, 30),
    SIZE_384x240(48, 30);

    private int tileWidth;
    private int tileHeight;

    GlcdSize(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public int getDisplayWidth() {
        return tileWidth * 8;
    }

    public int getDisplayHeight() {
        return tileHeight * 8;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public static GlcdSize get(int tileWidth, int tileHeight) {
        return Arrays.stream(GlcdSize.values())
                .filter(p -> (p.getTileWidth() == tileWidth) && (p.getTileHeight() == tileHeight))
                .findFirst().orElse(null);
    }
}