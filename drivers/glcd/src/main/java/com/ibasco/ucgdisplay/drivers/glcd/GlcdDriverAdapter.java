package com.ibasco.ucgdisplay.drivers.glcd;

public interface GlcdDriverAdapter extends U8g2DisplayDriver {
    void initialize(GlcdConfig config, boolean virtual);
}
