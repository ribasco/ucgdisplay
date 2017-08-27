package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.enums.TextAlignment;

/**
 * Generic Graphics Interface
 *
 * @author Rafael Ibasco
 */
public interface Graphics {

    void setCursor(int x, int y);

    void drawText(byte[] data);

    void drawText(byte data);

    default void drawText(int data) {
        drawText((byte) data);
    }

    void drawText(String text);

    void drawText(int y, String text);

    void drawText(String text, int x, int y);

    void drawText(String text, Object... args);

    void drawText(int y, String text, TextAlignment alignment);

    void drawText(int y, String text, TextAlignment alignment, Object... arguments);

    int getWidth();

    int getHeight();

    void clear();

    default void clear(int y) {
        clear(-1, y, -1);
    }

    void clear(int x, int y, int length);
}
