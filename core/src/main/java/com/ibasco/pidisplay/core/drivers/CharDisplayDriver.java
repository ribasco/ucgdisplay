package com.ibasco.pidisplay.core.drivers;


import com.ibasco.pidisplay.core.enums.ScrollDirection;
import com.ibasco.pidisplay.core.enums.TextDirection;

public interface CharDisplayDriver extends DisplayDriver {
    void home();

    void display(boolean state);

    void blink(boolean state);

    void cursor(boolean state);

    void autoscroll(boolean state);

    void scrollDisplay(ScrollDirection scrollDirection);

    void textDirection(TextDirection textDirection);

    void createChar(int num, byte[] charData);
}
