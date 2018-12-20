package com.ibasco.ucgdisplay.drivers.glcd.enums;

public enum GlcdBufferType {
    HORIZONTAL("Horizontal", "u8g2_ll_hvline_horizontal_right_lsb"),
    VERTICAL("Vertical", "u8g2_ll_hvline_vertical_top_lsb");

    private String name;

    private String key;

    GlcdBufferType(String name, String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
