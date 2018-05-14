package com.ibasco.pidisplay.core.drivers;

public interface GraphicsDisplayDriver extends DisplayDriver {
    void drawBox(int x, int y, int width, int height);

    void drawBitmap();

    void drawCircle(int x, int y, int radius);

    void drawDisc(int x, int y, int radius);

    /**
     * Options:
     *
     * U8G2_DRAW_UPPER_RIGHT
     * U8G2_DRAW_UPPER_LEFT
     * U8G2_DRAW_LOWER_LEFT
     * U8G2_DRAW_LOWER_RIGHT
     * U8G2_DRAW_ALL
     *
     * @param x
     * @param y
     * @param rx
     * @param ry
     * @param options
     */
    void drawEllipse(int x, int y, int rx, int ry, int options);

    void drawFilledEllipse(int x, int y, int rx, int ry, int options);

    void drawFrame(int x, int y, int width, int height);

    void setFont();

    void drawHLine(int x, int y, int width);

    void drawLine(int x, int y, int x1, int y1);

    void drawPixel(int x, int y);

    void drawRoundedBox();

    void drawRoundedFrame();
}
