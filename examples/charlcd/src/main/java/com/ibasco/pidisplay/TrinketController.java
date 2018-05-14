package com.ibasco.pidisplay;

import com.pi4j.io.i2c.I2CDevice;

public class TrinketController {
    private I2CDevice device;

    public TrinketController(I2CDevice device) {
        this.device = device;
    }


}
