package com.ibasco.pidisplay.drivers.glcd.utils;

public class XBMData {
    private int width;

    private int height;

    private byte[] data;

    XBMData(int width, int height, byte[] data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte[] getData() {
        return data;
    }
}
