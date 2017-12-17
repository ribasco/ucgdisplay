package com.ibasco.pidisplay.core.ui;

import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;

public interface CharGraphics extends Graphics {
    @Override
    CharDisplayDriver getDriver();

    void setAutoscroll(boolean state);

    void setBlink(boolean state);
}
