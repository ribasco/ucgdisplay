package com.ibasco.pidisplay.core.ui;

import com.ibasco.pidisplay.drivers.clcd.CharData;
import com.ibasco.pidisplay.drivers.clcd.CharDisplayDriver;
import com.ibasco.pidisplay.drivers.clcd.CharManager;

public interface CharGraphics extends Graphics {
    @Override
    CharDisplayDriver getDriver();

    void setAutoscroll(boolean state);

    void setBlink(boolean state);

    void clearLine();

    void clearLine(int lineNumber);

    CharManager charManager();

    void drawChar(String key);

    void drawChar(CharData charData);
}
