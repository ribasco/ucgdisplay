package com.ibasco.pidisplay.core.ui;

import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;

abstract public class CharGraphicsBase implements CharGraphics {
    @Override
    public CharDisplayDriver getDriver() {
        return null;
    }
}
