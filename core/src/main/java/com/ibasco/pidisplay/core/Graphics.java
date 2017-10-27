package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.exceptions.NotImplementedException;

/**
 * Generic Graphics Interface
 *
 * @author Rafael Ibasco
 */
public interface Graphics {

    default void setCursor(int x, int y) {
        throw new NotImplementedException();
    }

    default void drawText(char data) {
        throw new NotImplementedException();
    }

    default void drawText(char[] data) {
        throw new NotImplementedException();
    }

    default void drawText(byte[] data) {
        throw new NotImplementedException();
    }

    void drawText(byte data);

    default void drawText(int data) {
        drawText((byte) data);
    }

    void drawText(String text);

    int getWidth();

    int getHeight();

    void clear();

    void cursorBlink(boolean state);

    void flush();
}
