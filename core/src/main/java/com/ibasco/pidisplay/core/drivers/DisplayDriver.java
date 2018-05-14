package com.ibasco.pidisplay.core.drivers;

public interface DisplayDriver {
    int getHeight();

    int getWidth();

    void clear();

    void setCursor(int x, int y);

    void write(byte... data);
}
