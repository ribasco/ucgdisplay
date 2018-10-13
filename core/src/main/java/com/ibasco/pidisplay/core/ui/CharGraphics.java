package com.ibasco.pidisplay.core.ui;

import com.ibasco.pidisplay.drivers.lcd.hd44780.CharData;
import com.ibasco.pidisplay.drivers.lcd.hd44780.CharDisplayDriver;
import com.ibasco.pidisplay.drivers.lcd.hd44780.CharManager;

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
