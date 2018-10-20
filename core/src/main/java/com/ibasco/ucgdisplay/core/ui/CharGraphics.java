package com.ibasco.ucgdisplay.core.ui;

import com.ibasco.ucgdisplay.drivers.clcd.CharData;
import com.ibasco.ucgdisplay.drivers.clcd.CharDisplayDriver;
import com.ibasco.ucgdisplay.drivers.clcd.CharManager;

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
