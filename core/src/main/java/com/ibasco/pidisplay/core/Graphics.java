package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.drivers.DisplayDriver;

import java.io.Flushable;

/**
 * Generic Graphics Interface
 *
 * @author Rafael Ibasco
 */
public interface Graphics extends Flushable {

    void drawText(String text);

    void setDisplayCursor(int x, int y);

    void setCursor(int x, int y);

    void setCursorBlink(boolean state);

    int getCursorX();

    int getCursorY();

    int getWidth();

    int getHeight();

    void clear();

    boolean isDirty();

    DisplayDriver getDriver();
}
