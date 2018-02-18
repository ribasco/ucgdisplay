package com.ibasco.pidisplay.core.ui;

import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;

public interface CharGraphics extends Graphics {
    @Override
    CharDisplayDriver getDriver();

    void setAutoscroll(boolean state);

    void setBlink(boolean state);

    void clearLine();

    void clearLine(int lineNumber);

    CharManager charManager();

    void drawChar(int index);

    void drawChar(String key);

    void drawChar(CharData charData);
}
