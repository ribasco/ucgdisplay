package com.ibasco.pidisplay.core.ui;

import com.ibasco.pidisplay.core.utils.NativeLibraryLoader;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Native Implementation of the U8G2 Interface
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings("Duplicates")
public class U8g2Interface {

    private static final Logger log = getLogger(U8g2Interface.class);

    //List of supported communication protocols by U8G2
    public static final int COM_4WSPI = 0x0001;
    public static final int COM_3WSPI = 0x0002;
    public static final int COM_6800 = 0x0004;
    public static final int COM_8080 = 0x0008;
    public static final int COM_I2C = 0x0010;
    public static final int COM_ST7920SPI = 0x0020;     /* mostly identical to COM_4WSPI, but does not use DC */
    public static final int COM_UART = 0x0040;
    public static final int COM_KS0108 = 0x0080;        /* mostly identical to 6800 mode, but has more chip select lines */
    public static final int COM_SED1520 = 0x0100;

    static {
        try {
            NativeLibraryLoader.loadLibrary("pidisp");
        } catch (Exception e) {
            throw new RuntimeException("Unable to load required native library", e);
        }
    }

    /**
     * Setup U8G2. This method MUST be called FIRST before performing any other u8g2 related operations.
     *
     * @param setupProc
     *         The setup procedure that the native library will use to initialze the display
     * @param commInt
     *         The communications interface that will be used (e.g. {@link #COM_4WSPI})
     * @param commType
     *         The communication type (HARDWARE = 0, SOFTWARE = 1)
     * @param rotation
     *         The display rotation
     * @param address
     *         The device address. This is most commonly used by I2C. Set this to -1 if not applicable to the current
     *         setup.
     * @param pinConfig
     *         Array of integers which represents the pin mapping configuration of the display
     *
     * @return The id of the u8g2 instance. -1 if the setup failed.
     */
    public static native long setup(String setupProc, int commInt, int commType, int rotation, int address, byte[] pinConfig);

    public static native void drawBox(long id, int x, int y, int width, int height);

    /**
     * @see #drawXBM(long, int, int, int, int, byte[])
     */
    @Deprecated
    public static native void drawBitmap(long id, int x, int y, int count, int height, byte[] bitmap);

    public static native void drawCircle(long id, int x, int y, int radius, int options);

    public static native void drawDisc(long id, int x, int y, int radius, int options);

    public static native void drawEllipse(long id, int x, int y, int rx, int ry, int options);

    public static native void drawFilledEllipse(long id, int x, int y, int rx, int ry, int options);

    public static native void drawFrame(long id, int x, int y, int width, int height);

    public static native void drawGlyph(long id, int x, int y, short encoding);

    public static native void drawHLine(long id, int x, int y, int width);

    public static native void drawLine(long id, int x, int y, int x1, int y1);

    public static native void drawPixel(long id, int x, int y);

    public static native void drawRoundedBox(long id, int x, int y, int width, int height, int radius);

    public static native void drawRoundedFrame(long id, int x, int y, int width, int height, int radius);

    public static native void drawString(long id, int x, int y, String value);

    public static native void drawTriangle(long id, int x0, int y0, int x1, int y1, int x2, int y2);

    public static native void drawXBM(long id, int x, int y, int width, int height, byte[] data);

    public static native int drawUTF8(long id, int x, int y, String value);

    public static native int getUTF8Width(long id, String text);

    public static native void setFont(long id, byte[] data);

    public static native void setFont(long id, String fontKey);

    public static native void setFontMode(long id, int mode);

    public static native void setFontDirection(long id, int direction);

    public static native void setFontPosBaseline(long id);

    public static native void setFontPosBottom(long id);

    public static native void setFontPosTop(long id);

    public static native void setFontPosCenter(long id);

    public static native void setFontRefHeightAll(long id);

    public static native void setFontRefHeightExtendedText(long id);

    public static native void setFontRefHeightText(long id);

    public static native void setFlipMode(long id, boolean enable);

    public static native void setPowerSave(long id, boolean enable);

    public static native void setDrawColor(long id, int color);

    public static native void initDisplay(long id);

    public static native void firstPage(long id);

    public static native int nextPage(long id);

    public static native int getAscent(long id);

    public static native int getDescent(long id);

    public static native int getMaxCharWidth(long id);

    public static native int getMaxCharHeight(long id);

    public static native void sendBuffer(long id);

    public static native void clearBuffer(long id);

    public static native void clearDisplay(long id);

    public static native void begin(long id);

    public static native int getHeight(long id);

    public static native int getWidth(long id);

    public static native void clear(long id);

    public static native int setAutoPageClear(long id, int mode);

    public static native void setBitmapMode(long id, int mode);

    public static native void setContrast(long id, int value);

    public static native void setDisplayRotation(long id, int rotation);

    public static native int getBuffer(long id);

    public static native int getBufferTileWidth(long id);

    public static native int getBufferTileHeight(long id);

    @Deprecated
    public static native int getPageCurrTileRow(long id);

    @Deprecated
    public static native void setPageCurrTileRow(long id, int row);

    public static native void setBufferCurrTileRow(long id, int row);

    public static native int getBufferCurrTileRow(long id);

    //public static native void home(long id);

    //public static native void setCursor(long id, int x, int y);

    //public static native void enableUTF8Print(long id);
}
