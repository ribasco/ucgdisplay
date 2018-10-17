package com.ibasco.ucgdisplay.core.u8g2;

import com.ibasco.ucgdisplay.core.utils.NativeLibraryLoader;

/**
 * <p>This is a wrapper class for the U8G2 native graphics interface. It is not advisable to use this class
 * directly unless you know what you are doing. Use the facilities provided in the glcd drivers module.</p>
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings({"Duplicates", "WeakerAccess"})
public class U8g2Graphics {

    //<editor-fold desc="Display rotation options">
    /**
     * No rotation
     *
     * @see #setDisplayRotation
     */
    public static final int ROTATION_R0 = 0;
    /**
     * 90 degree clockwise rotation
     *
     * @see #setDisplayRotation
     */
    public static final int ROTATION_R1 = 1;
    /**
     * 180 degree clockwise rotation
     *
     * @see #setDisplayRotation
     */
    public static final int ROTATION_R2 = 2;
    /**
     * 270 degree clockwise rotation
     *
     * @see #setDisplayRotation
     */
    public static final int ROTATION_R3 = 3;
    /**
     * No rotation, landscape, display content is mirrored
     *
     * @see #setDisplayRotation
     */
    public static final int ROTATION_MIRROR = 4;
    //</editor-fold>

    //<editor-fold desc="Bus Interface types">
    /**
     * Bus interface using the device hardware specific features for data transport
     */
    public static final int BUS_HARDWARE = 0;

    /**
     * Bus interface using software bit-banging procedures for data transport
     */
    public static final int BUS_SOFTWARE = 1;
    //</editor-fold>

    //<editor-fold desc="Bus Interfaces">
    /**
     * 4-Wire SPI protocol
     */
    public static final int COM_4WSPI = 0x0001;

    /**
     * 3-Wire SPI protocol
     */
    public static final int COM_3WSPI = 0x0002;

    /**
     * Parallel 8-bit 6800 protocol
     */
    public static final int COM_6800 = 0x0004;

    /**
     * Parallel 8-bit 8080 protocol
     */
    public static final int COM_8080 = 0x0008;

    /**
     * I2C Protocol
     */
    public static final int COM_I2C = 0x0010;

    /**
     * 4-Wire SPI protocol but does not use DC pin (Used by ST7920 controller)
     */
    public static final int COM_ST7920SPI = 0x0020;     /* mostly identical to COM_4WSPI, but does not use DC */

    /**
     * Serial/UART protocol
     */
    public static final int COM_UART = 0x0040;

    /**
     * Parallel 6800 protocol for KS0108 (Contains more chip select lines)
     */
    public static final int COM_KS0108 = 0x0080;        /* mostly identical to 6800 mode, but has more chip select lines */

    /**
     * Special protocol for SED1520
     */
    public static final int COM_SED1520 = 0x0100;
    //</editor-fold>

    //<editor-fold desc="Draw Options">
    /**
     * Draw the upper right portion of the shape
     */
    public static final int U8G2_DRAW_UPPER_RIGHT = 0x01;

    /**
     * Draw the upper left portion of the shape
     */
    public static final int U8G2_DRAW_UPPER_LEFT = 0x02;

    /**
     * Draw the lower left portion of the shape
     */
    public static final int U8G2_DRAW_LOWER_LEFT = 0x04;

    /**
     * Draw the lower right portion of the shape
     */
    public static final int U8G2_DRAW_LOWER_RIGHT = 0x08;

    /**
     * Draw the whole shape
     */
    public static final int U8G2_DRAW_ALL = (U8G2_DRAW_UPPER_RIGHT | U8G2_DRAW_UPPER_LEFT | U8G2_DRAW_LOWER_RIGHT | U8G2_DRAW_LOWER_LEFT);
    //</editor-fold>

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
     *         The setup procedure name. This is equivalent to the u8g2 setup function in c/c++
     * @param busInterface
     *         The bus communications interface (e.g. {@link #COM_4WSPI})
     * @param busInterfaceType
     *         The bus interface type (HARDWARE = 0, SOFTWARE = 1)
     * @param rotation
     *         The display rotation.
     * @param address
     *         The device address. This is most commonly used by I2C. Set this to -1 if not applicable to the current
     *         setup.
     * @param pinConfig
     *         Array of integers which represents the pin mapping configuration of the display
     * @param virtual
     *         Set to <code>true</code> to enable virtual-mode. All display instructions will be re-routed to the {@link
     *         U8g2EventDispatcher}
     *
     * @return The id of the u8g2 instance. -1 if the setup failed.
     */
    public static native long setup(String setupProc, int busInterface, int busInterfaceType, int rotation, int address, byte[] pinConfig, boolean virtual);

    /**
     * <p>Draw a box (filled frame), starting at x/y position (upper left edge). The box has width w and height h.
     * Parts of the box can be outside of the display boundaries. This procedure will use the current color
     * (setDrawColor) to draw the box. For a monochrome display, the color index 0 will clear a pixel and the color
     * index 1 will set a pixel.
     * </p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-position of upper left edge.
     * @param y
     *         Y-position of upper left edge.
     * @param width
     *         Width of the box.
     * @param height
     *         Height of the box.
     */
    public static native void drawBox(long id, int x, int y, int width, int height);

    /**
     * <p>Draw a bitmap at the specified x/y position (upper left corner of the bitmap). Parts of the bitmap may be
     * outside the display boundaries.The bitmap is specified by the array bitmap. A cleared bit means: Do not draw a
     * pixel. A set bit inside the array means: Write pixel with the current color index.For a monochrome display, the
     * color index 0 will clear a pixel (in solid mode) and the color index 1 will set a pixel.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-position (left position of the bitmap).
     * @param y
     *         Y-position (upper position of the bitmap).
     * @param count
     *         Number of bytes of the bitmap in horizontal direction. The width of the bitmap is count * 8
     * @param height
     *         Height of the bitmap.
     * @param bitmap
     *         Bitmap data
     *
     * @deprecated Please use {@link #drawXBM(long, int, int, int, int, byte[])} instead
     */
    @Deprecated
    public static native void drawBitmap(long id, int x, int y, int count, int height, byte[] bitmap);

    /**
     * <p>Draw a circle with radus rad at position (x0, y0). The diameter of the circle is 2*rad+1. Depending on opt,
     * it is possible to draw only some sections of the circle.
     * </p>
     * <p>
     * Possible values for opt are:
     * <ul>
     * <li>U8G2_DRAW_UPPER_RIGHT</li>
     * <li>U8G2_DRAW_UPPER_LEFT</li>
     * <li>U8G2_DRAW_LOWER_LEFT</li>
     * <li>U8G2_DRAW_LOWER_RIGHT</li>
     * <li>U8G2_DRAW_ALL</li>
     * </ul>
     * <p>
     * These values can be combined with the | operator. This procedure will use the current color (setDrawColor) for
     * drawing.</p>
     * </p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-Position of the center of the circle.
     * @param y
     *         Y-Position of the center of the circle.
     * @param radius
     *         Defines the size of the circle: Radus = rad
     * @param options
     *         Selects some or all sections of the circle.
     */
    public static native void drawCircle(long id, int x, int y, int radius, int options);

    /**
     * <p>Draw a filled circle with radus rad at position (x0, y0). The diameter of the circle is 2*rad+1.
     * Depending on opt, it is possible to draw only some sections of the disc.
     * <p>
     * <p>
     * Possible values for options are:
     * <ul>
     * <li>U8G2_DRAW_UPPER_RIGHT</li>
     * <li>U8G2_DRAW_UPPER_LEFT</li>
     * <li>U8G2_DRAW_LOWER_LEFT</li>
     * <li>U8G2_DRAW_LOWER_RIGHT</li>
     * <li>U8G2_DRAW_ALL</li>
     * </ul>
     * <p>
     * <p>
     * These values can be combined with the | operator. This procedure will use the current color ({@link
     * #setDrawColor(long, int)}) for drawing.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-Position of the center of the disc.
     * @param y
     *         Y-Position of the center of the disc.
     * @param radius
     *         Defines the size of the circle: Radus = rad.
     * @param options
     *         Selects some or all sections of the disc.
     */
    public static native void drawDisc(long id, int x, int y, int radius, int options);

    /**
     * <p>Draw ellipse with radus rx and 'ry' at position (x0, y0). rx*ry must be lower than 512 in 8 Bit mode of
     * u8g2.</p>
     * <p>
     * Possible values for options are:
     * <ul>
     * <li>U8G2_DRAW_UPPER_RIGHT</li>
     * <li>U8G2_DRAW_UPPER_LEFT</li>
     * <li>U8G2_DRAW_LOWER_LEFT</li>
     * <li>U8G2_DRAW_LOWER_RIGHT</li>
     * <li>U8G2_DRAW_ALL</li>
     * </ul>
     * <p>
     * The diameter is twice the radius plus one.
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-Position of the center of the filled circle.
     * @param y
     *         Y-Position of the center of the filled circle.
     * @param rx
     *         Defines the size of the ellipse.
     * @param ry
     *         Defines the size of the ellipse.
     * @param options
     *         Selects some or all sections of the ellipse.
     */
    public static native void drawEllipse(long id, int x, int y, int rx, int ry, int options);

    /**
     * <p>Draw a filled ellipse with radus rx and 'ry' at position (x0, y0). rx*ry must be lower than 512 in 8 Bit mode
     * of u8g2.Depending on opt, it is possible to draw only some sections of the disc.</p>
     * <p>
     * Possible values for options are:
     * <ul>
     * <li>U8G2_DRAW_UPPER_RIGHT</li>
     * <li>U8G2_DRAW_UPPER_LEFT</li>
     * <li>U8G2_DRAW_LOWER_LEFT</li>
     * <li>U8G2_DRAW_LOWER_RIGHT</li>
     * <li>U8G2_DRAW_ALL</li>
     * </ul>
     * <p>
     * These values can be combined with the | operator.
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-Position of the center of the filled circle.
     * @param y
     *         Y-Position of the center of the filled circle.
     * @param rx
     *         X-the size of the ellipse.
     * @param ry
     *         Y- the size of the ellipse.
     * @param options
     *         Selects some or all sections of the ellipse.
     */
    public static native void drawFilledEllipse(long id, int x, int y, int rx, int ry, int options);

    /**
     * <p>Draw a frame (empty box), starting at x/y position (upper left edge). The box has width w and height h.
     * Parts of the frame can be outside of the display boundaries. This procedure will use the current color
     * (setDrawColor) to draw the box. For a monochrome display, the color index 0 will clear a pixel and the color
     * index 1 will set a pixel.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-position of upper left edge.
     * @param y
     *         Y-position of upper left edge.
     * @param width
     *         Width of the frame.
     * @param height
     *         Height of the frame.
     */
    public static native void drawFrame(long id, int x, int y, int width, int height);

    /**
     * <p>Draw a single character. The character is placed at the specified pixel posion x and y. U8g2 supports the
     * lower 16 bit of the unicode character range (plane 0/Basic Multilingual Plane): The encoding can be any value
     * from 0 to 65535. The glyph can be drawn only, if the encoding exists in the active font.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-Position of the character on the display.
     * @param y
     *         Y-Position of the character on the display.
     * @param encoding
     *         Unicode value of the character.
     *
     * @see #setFont(long, byte[])
     */
    public static native void drawGlyph(long id, int x, int y, short encoding);

    /**
     * <p>Draw a horizontal line, starting at x/y position (left edge). The width (length) of the line is w pixel.
     * Parts of the line can be outside of the display boundaries. This procedure uses the current color index to draw
     * the line. Color index 0 will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-Position
     * @param y
     *         Y-Position
     * @param width
     *         Length of the horizontal line
     */
    public static native void drawHLine(long id, int x, int y, int width);

    /**
     * <p>Draw a vertical line, starting at x/y position (upper end). The height (length) of the line is h pixel. Parts
     * of the line can be outside of the display boundaries. This procedure uses the current color index to draw the
     * line. Color index 0 will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-position.
     * @param y
     *         Y-position.
     * @param length
     *         Length of the vertical line.
     */
    public static native void drawVLine(long id, int x, int y, int length);

    /**
     * Draw a line between two points. This procedure will use the current color (setDrawColor).
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-position of the first point.
     * @param y
     *         Y-position of the first point.
     * @param x1
     *         X-position of the second point.
     * @param y1
     *         Y-position of the second point.
     */
    public static native void drawLine(long id, int x, int y, int x1, int y1);

    /**
     * <p>Draw a pixel at the specified x/y position. Position (0,0) is at the upper left corner of the display. The
     * position may be outside the display boundaries.This procedure uses the current color index to draw the pixel. The
     * color index 0 will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-position.
     * @param y
     *         Y-position.
     */
    public static native void drawPixel(long id, int x, int y);

    /**
     * <p>Draw a box with round edges, starting at x/y position (upper left edge). The box/frame has width w and height
     * h. Parts of the box can be outside of the display boundaries. Edges have radius r. It is required that w >=
     * 2*(r+1) and h >= 2*(r+1). This condition is not checked. Behavior is undefined if w or h is smaller than 2*(r+1).
     * This procedure uses the current color index to draw the box. For a monochrome display, the color index 0 will
     * clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-position of upper left edge.
     * @param y
     *         Y-position of upper left edge.
     * @param width
     *         Width of the box.
     * @param height
     *         Height of the box.
     * @param radius
     *         Radius for the four edges.
     */
    public static native void drawRoundedBox(long id, int x, int y, int width, int height, int radius);

    /**
     * <p>Draw a frame with round edges, starting at x/y position (upper left edge). The box/frame has width w and
     * height h. Parts of the box can be outside of the display boundaries. Edges have radius r. It is required that w
     * >= 2*(r+1) and h >= 2*(r+1). This condition is not checked. Behavior is undefined if w or h is smaller than
     * 2*(r+1). This procedure uses the current color index to draw the box. For a monochrome display, the color index 0
     * will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-position of upper left edge.
     * @param y
     *         Y-position of upper left edge.
     * @param width
     *         Width of the box.
     * @param height
     *         Height of the box.
     * @param radius
     *         Radius for the four edges.
     */
    public static native void drawRoundedFrame(long id, int x, int y, int width, int height, int radius);

    /**
     * <p>Draw a string. The first character is placed at position x andy. Use setFont to assign a font before drawing
     * a string on the display. To draw a character with encoding 127 to 255, use the C/C++/Arduino escape sequence
     * "\xab" (hex value ab) or "\xyz" (octal value xyz). This function can not draw any glyph with encoding greater or
     * equal to 256. Use drawUTF8 or drawGlyph to access glyphs with encoding greater or equal to 256.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-Position of the first character on the display.
     * @param y
     *         Y-Position of the first character on the display.
     * @param value
     *         The text to draw on the display
     */
    public static native void drawString(long id, int x, int y, String value);

    /**
     * <p>Draw a triangle (filled polygon). Arguments are 16 bit and the polygon is clipped to the size of the display.
     * Multiple polygons are drawn so that they exactly match without overlap:The left side of a polygon is drawn, the
     * right side is not draw. The upper side is only draw if it is flat.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x0
     *         X-position point 0.
     * @param y0
     *         Y-position point 0.
     * @param x1
     *         X-position point 1.
     * @param y1
     *         Y-position point 1.
     * @param x2
     *         X-position point 2.
     * @param y2
     *         Y-position point 2.
     */
    public static native void drawTriangle(long id, int x0, int y0, int x1, int y1, int x2, int y2);

    /**
     * <p>Draw a <a href="http://en.wikipedia.org/wiki/X_BitMap">XBM Bitmap</a>. Position (x,y) is the upper left
     * corner of the bitmap. XBM contains monochrome, 1-bit bitmaps.</p>
     *
     * <p>The current color index is used for drawing <strike>(see setColorIndex)</strike> pixel values 1. Version
     * 2.15.x of U8g2 introduces a solid and a transparent mode for bitmaps. By default, drawXBM will draw solid
     * bitmaps. This differs from the previous versions: Use setBitmapMode(1) to switch to the previous behavior. The
     * XBMP version of this procedure expects the bitmap to be in PROGMEM area (AVR only). Many tools (including GIMP)
     * can save a bitmap as XBM. A nice step by step instruction is <a href="https://sandhansblog.wordpress.com/2017/04/16/interfacing-displaying-a-custom-graphic-on-an-0-96-i2c-oled/">here</a>
     * (external link). The result will look like this:</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-position.
     * @param y
     *         Y-position.
     * @param width
     *         Width of the bitmap.
     * @param height
     *         Height of the bitmap.
     * @param data
     *         Bitmap data
     *
     * @see #setBitmapMode
     */
    public static native void drawXBM(long id, int x, int y, int width, int height, byte[] data);

    /**
     * <p>Draw a string which is encoded as UTF-8. There are two preconditions for the use of this function:
     * (A) the C/C++/Arduino compiler must support UTF-8 encoding (this is default for the gnu compiler, which is also
     * used for most Arduino boards) and (B) the code editor/IDE must support and store the C/C++/Arduino code as UTF-8
     * (true for the Arduino IDE). If these conditions are met, you can use the character with code value greater than
     * 127 directly in the string (of course the character must exist in the font file, see also setFont). Advantage: No
     * escape codes are required and the source code is more readable. The glyph can be copied and paste into the editor
     * from a "char set" tool. Disadvantage: The code is less portable and the strlen function will not return the
     * number of visible characters.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param x
     *         X-Position of the first character on the display.
     * @param y
     *         Y-Position of the first character on the display.
     * @param value
     *         UTF-8 encoded text
     *
     * @return Width of the string.
     *
     * @apiNote This drawing function depends on the current font mode and drawing color.
     * @see #getUTF8Width(long, String)
     * @see #setFont(long, byte[])
     * @see #drawString(long, int, int, String)
     */
    public static native int drawUTF8(long id, int x, int y, String value);

    /**
     * <p>Return the pixel width of an UTF-8 encoded string.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param text
     *         UTF-8 encoded text.
     *
     * @return Width of the string if drawn with the current font
     */
    public static native int getUTF8Width(long id, String text);

    /**
     * <p> Define a u8g2 font for the glyph and string drawing functions. Note: u8x8 font can NOT be used. Available
     * fonts are listed here here. The last two characters of the font name define the type and character set for the
     * font:
     * </p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param data
     *         Font data
     *
     * @see <a href="https://github.com/olikraus/u8g2/wiki/fntlistall">List of available fonts</a>
     */
    public static native void setFont(long id, byte[] data);

    /**
     * <p> Define a u8g2 font for the glyph and string drawing functions. Note: u8x8 font can NOT be used. Available
     * fonts are listed here here. The last two characters of the font name define the type and character set for the
     * font:
     * </p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param fontKey
     *         A key string representing the Font
     *
     * @see <a href="https://github.com/olikraus/u8g2/wiki/fntlistall">List of available fonts</a>
     */
    public static native void setFont(long id, String fontKey);

    /**
     * <p>Defines, whether the glyph and string drawing functions will write the background color (mode 0/solid,
     * mode = 0) or not (mode 1/transparent, mode = 1). Default mode is 1 (background color of the characters is not
     * changed).</p>
     * <p>
     * <strong>Note:</strong> Always choose a suitable font, depending on the font mode:
     * <br />
     * <table>
     * <thead>
     * <tr>
     * <th>Font Name</th>
     * <th>Font Type</th>
     * <th>Suitable for...</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>FONT_xxx_TX</td>
     * <td>Transparent gylphs with variable width</td>
     * <td>
     * <code>mode = 1</code>, XOR Mode</td>
     * </tr>
     * <tr>
     * <td>FONT_xxx_MX</td>
     * <td>Monospace/fixed width glyphs</td>
     * <td><code>mode = 0</code></td>
     * </tr>
     * <tr>
     * <td>FONT_xxx_HX</td>
     * <td>Glyphs with variable width and common height</td>
     * <td><code>mode = 0</code></td>
     * </tr>
     * <tr>
     * <td>FONT_xxx_8X</td>
     * <td>Monospace/fixed width glyphs in a 8x8 box</td>
     * <td><code>mode = 0</code></td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param mode
     *         Enable (1) or disable (0) transparent mode.
     *
     * @see #setDrawColor
     * @see #setFont
     */
    public static native void setFontMode(long id, int mode);

    /**
     * <p>The arguments defines the drawing direction of all strings or glyphs.</p>
     * <p>
     * <br />
     * <table>
     * <thead>
     * <tr>
     * <th>Argument</th>
     * <th>String Rotation</th>
     * <th>Description</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>0</td>
     * <td>0 degree</td>
     * <td>Left to right</td>
     * </tr>
     * <tr>
     * <td>1</td>
     * <td>90 degree</td>
     * <td>Top to down</td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>180 degree</td>
     * <td>Right to left</td>
     * </tr>
     * <tr>
     * <td>3</td>
     * <td>270 degree</td>
     * <td>Down to top</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param direction
     *         Writing direction/string rotation.
     *
     * @see #drawString(long, int, int, String)
     */
    public static native void setFontDirection(long id, int direction);

    /**
     * <p>Change the reference position for the glyph and string draw functions. By default the reference position is
     * "Baseline".</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     */
    public static native void setFontPosBaseline(long id);

    /**
     * <p>Change the reference position for the glyph and string draw functions. By default the reference position is
     * "Baseline".</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     */
    public static native void setFontPosBottom(long id);

    /**
     * <p>Change the reference position for the glyph and string draw functions. By default the reference position is
     * "Baseline".</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     */
    public static native void setFontPosTop(long id);

    /**
     * <p>Change the reference position for the glyph and string draw functions. By default the reference position is
     * "Baseline".</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     */
    public static native void setFontPosCenter(long id);

    /**
     * <p>A call to this procedure will define the calculation method for the ascent and descent of the current
     * font. This method will be used for the current and all other fonts, which will be set with {@link #setFont(long,
     * byte[])}. Changing this calculation method has an effect on {@link #getAscent(long)} and {@link
     * #getDescent(long)}. Default is {@link #setFontRefHeightText(long)}. </p>
     * <p>
     * Ascent will be the highest ascent of all glyphs of the current font. Descent will be the highest descent of all
     * glyphs of the current font.
     * </p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @see #getAscent
     * @see #getDescent
     */
    public static native void setFontRefHeightAll(long id);

    /**
     * <p>A call to this procedure will define the calculation method for the ascent and descent of the current
     * font. This method will be used for the current and all other fonts, which will be set with {@link #setFont(long,
     * byte[])}. Changing this calculation method has an effect on {@link #getAscent(long)} and {@link
     * #getDescent(long)}.
     *
     * <p>Ascent will be the largest ascent of "A", "1" or "(" of the current font. Descent will be the descent of "g"
     * or "(" of the current font.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @see #getAscent
     * @see #getDescent
     */
    public static native void setFontRefHeightExtendedText(long id);

    /**
     * <p>A call to this procedure will define the calculation method for the ascent and descent of the current
     * font. This method will be used for the current and all other fonts, which will be set with {@link #setFont(long,
     * byte[])}. Changing this calculation method has an effect on {@link #getAscent(long)} and {@link
     * #getDescent(long)}.
     *
     * <p>Ascent will be the ascent of "A" or "1" of the current font. Descent will be the descent  "g" of the current
     * font (this is the default after startup).</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @see #getAscent
     * @see #getDescent
     */
    public static native void setFontRefHeightText(long id);

    /**
     * <p>Some displays support a 180 degree rotation of the internal frame buffer. This hardware feature can be
     * controlled with this procedure. Important: Redraw the complete display after changing the flip mode. Best is to
     * clear the display first, then change the flip mode and finally redraw the content. Results will be undefined for
     * any existing content on the screen.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param enable
     *         Enable (true) or disable (false) 180 degree rotation of the display content
     */
    public static native void setFlipMode(long id, boolean enable);

    /**
     * <p>Activates (enable = true) or disables (enable = false) the power save mode of the display. With activated
     * power save mode, nothing will be visible on the display. The content of the RAM of the display is not changed.
     * This procedure is also called from begin.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param enable
     *         Enable (true) or disable (false) power save mode for the display.
     *
     * @see #begin
     */
    public static native void setPowerSave(long id, boolean enable);

    /**
     * <p>Defines the bit value (color index) for all drawing functions. All drawing function will change the display
     * memory to this bit value. Default value is 1. For example the {@link #drawBox(long, int, int, int, int)}
     * procedure will set all pixels for the defined area to the bit value, provided here. In v2.11 the new color value
     * 2 will activate the XOR mode. Exceptions:</p>
     *
     * <p>Both functions will always set the buffer to the pixel value 0. The color argument of setDrawColor is
     * ignored.</p>
     * <p>
     * <br /> Note: Not all graphics procedures will support XOR mode. Especially XOR mode is not supported by
     * drawCircle, drawDisc, drawEllipse and drawFilledEllipse.</p>
     * <p>
     * <br />
     * <strong>Exceptions:</strong>
     *
     * <ul>
     * <li>{@link #clear(long)}, {@link #clearBuffer(long)}: Both functions will always set the buffer to the pixel
     * value 0. The color argument of setDrawColor is ignored.</li>
     * <li>drawGlyph: All font drawing procedures will use this color argument as foreground color. In none-transparent
     * (solid) mode (setFontMode) the complement of the color value will be the background color and is set to 0 for
     * color value 2 (However, suggestion is not to use solid and XOR mode together):</li>
     * </ul>
     *
     * <table border="1">
     * <thead>
     * <tr>
     * <th>Font Mode</th>
     * <th>Draw Color</th>
     * <th>Glyph Foreground Color</th>
     * <th>Glyph Background Color</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>0: solid</td>
     * <td>0</td>
     * <td>0</td>
     * <td>1</td>
     * </tr>
     * <tr>
     * <td>0: solid</td>
     * <td>1</td>
     * <td>1</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>0: solid</td>
     * <td>2</td>
     * <td>XOR</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>1: transparent</td>
     * <td>0</td>
     * <td>0</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>1: transparent</td>
     * <td>1</td>
     * <td>1</td>
     * <td>-</td>
     * </tr>
     * <tr>
     * <td>1: transparent</td>
     * <td>2</td>
     * <td>XOR</td>
     * <td>-</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param color
     *         0 (clear pixel value in the display RAM), 1 (set pixel value) or 2 (XOR mode)
     *
     * @see #drawBox(long, int, int, int, int)
     * @see #drawGlyph(long, int, int, short)
     * @see #setFontMode(long, int)
     */
    public static native void setDrawColor(long id, int color);

    /**
     * <p> Reset and configure the display. This procedure must be called before any other procedures draw something on
     * the display. This procedure leaves the display in a power save mode. In order to see something on the screen,
     * disable power save mode first (setPowerSave). This procedure is called by the begin procedure. Either begin or
     * initDisplay must be called initially.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     */
    public static native void initDisplay(long id);

    /**
     * <p> This command is part of the (picture) loop which renders the content of the display. This command must be
     * used together with nextPage. There are some restrictions: Do not change the content when executing this loop.
     * Always redraw everything. It is not possible to redraw only parts of the content. The advantage is lesser RAM
     * consumption compared to a full frame buffer in RAM, see sendBuffer.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @apiNote This procedure sets the current page position to zero.
     * @see #nextPage
     */
    public static native void firstPage(long id);

    /**
     * <p> This command is part of the (picture) loop which renders the content of the display. This command must be
     * used together with firstPage. There are some restrictions: Do not change the content when executing this loop.
     * Always redraw everything. It is not possible to redraw only parts oft the content. The advantage is lesser RAM
     * consumption compared to a full frame buffer in RAM, see sendBuffer. This procedure will send a refresh message
     * (refreshDisplay) to an e-Paper/e-Ink device after completion of the loop (just before returning 0).</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return 0, once the loop is completed (all data transfered to the display).
     *
     * @apiNote This procedure adds the height (in tile rows) of the current buffer to the current page position.
     * @see #firstPage
     */
    public static native int nextPage(long id);

    /**
     * <p>Returns the reference height of the glyphs above the baseline (ascent). This value depends on the current
     * reference height (see {@link #setFontRefHeightAll(long)}).</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return The ascent of the current font.
     *
     * @see #setFont
     * @see #getDescent
     * @see #setFontRefHeightAll
     */
    public static native int getAscent(long id);

    /**
     * <p>Returns the reference height of the glyphs below the baseline (descent). For most fonts, this value will be
     * negative. This value depends on the current reference height (see {@link #setFontRefHeightAll(long)}).</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return The descent of the current font.
     *
     * @see #setFont
     * @see #getDescent
     * @see #setFontRefHeightAll
     */
    public static native int getDescent(long id);

    /**
     * <p>Each glyph is stored as a bitmap. This returns the width of the largest bitmap in the font.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return The largest width of any glyph in the font.
     *
     * @see #getMaxCharHeight
     */
    public static native int getMaxCharWidth(long id);

    /**
     * <p>Each glyph is stored as a bitmap. This returns the height of the largest bitmap in the font.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return The largest height of any glyph in the font.
     *
     * @see #getMaxCharWidth
     */
    public static native int getMaxCharHeight(long id);

    /**
     * <p>Send the content of the memory frame buffer to the display. Use {@link #clearBuffer(long)} to clear the
     * buffer and the draw functions to draw something into the frame buffer. This procedure is useful only with a full
     * frame buffer in the RAM of the microcontroller (Constructor with buffer option "f", see here). This procedure
     * will also send a refresh message (refreshDisplay) to an e-Paper/e-Ink device.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @apiNote Actually this procedure will send the current page to the display. This means, the content of the
     * internal pixel buffer will be placed in the tile row given by the current page position. This means, that this
     * procedure could be used for partial updates on paged devices (constructor with buffer option "1" or "2").
     * However, this will only work for LCDs. It will not work with most e-Paper/e-Ink devices because of the buffer
     * switch in the display controller. Conclusion: Use this command only together with full buffer constructors. It
     * will then work with all LCDs and e-Paper/e-Ink devices.
     * @see #clearBuffer
     */
    public static native void sendBuffer(long id);

    /**
     * <p>Clears all pixel in the memory frame buffer. Use sendBuffer to transfer the cleared frame buffer to the
     * display. In most cases, this procedure is useful only with a full frame buffer in the RAM of the microcontroller
     * (Constructor with buffer option "f", see here). This procedure will also send a refresh message (refreshDisplay)
     * to an e-Paper/e-Ink device.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @see #sendBuffer
     */
    public static native void clearBuffer(long id);

    /**
     * <p> Clears all pixel in the internal buffer AND on the connected display. This procedure is also called from
     * begin. Usually there is no need to call this function except for the init procedure. Other procedures like {@link
     * #sendBuffer(long)} and {@link #nextPage(long)} will also overwrite (and clear) the display..</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @see #begin
     */
    public static native void clearDisplay(long id);

    /**
     * <p>Simplified setup procedure of the display for the Arduino enviornment. See the <a
     * href="https://github.com/olikraus/u8g2/wiki/u8g2setupcpp">setup guide</a> for the selection of a suitable U8g2
     * constructor. This function will reset, configure, clear and disable power save mode of the display. U8g2 can also
     * detect key press events. Up to six buttons can be observed. The Arduino pin number can be assigned here. Use
     * U8X8_PIN_NONE if there is no switch connected to the pin. The switch has to connect the GPIO pin with GND (low
     * active button). Use getMenuEvent to check for any key press event. Select, next and prev pins are also required
     * for the user interface procedures (for example userInterfaceMessage ).</p>
     *
     * <p> begin will call: </p>
     *
     * <ol>
     * <li>{@link #initDisplay(long)}</li>
     * <li>{@link #clearDisplay(long)}</li>
     * <li>{@link #setPowerSave(long, boolean)} ()}</li>
     * </ol>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @see #initDisplay
     * @see #setPowerSave
     * @see #clearDisplay
     */
    public static native void begin(long id);

    /**
     * <p>Get the current height of the display instance</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return The display height or -1 if the instance id is invalid
     */
    public static native int getHeight(long id);

    /**
     * <p>Get the current width of the display instance</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return The display width or -1 if the instance id is invalid
     */
    public static native int getWidth(long id);

    /**
     * <p>Clears all pixel on the display and the buffer. Puts the cursor for the print function into the upper left
     * corner. </p>
     * <p>clear will call: </p>
     * <ol>
     * <li><strike>home</strike></li>
     * <li>{@link #clearDisplay(long)}</li>
     * <li>{@link #clearBuffer(long)}</li>
     * </ol>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @see <strike>print</strike>
     * @see <strike>home</strike>
     * @see #clearBuffer
     */
    public static native void clear(long id);

    /**
     * <p>Enables (mode=1) or disables (mode=0) automatic clearing of the pixel buffer by the {@link #firstPage(long)}
     * and {@link #nextPage(long)} procedures. By default this is enabled and in most situation it is not required to
     * disable this. If disabled, the user is responsible to set ALL pixel of the current pixel buffer to some suitable
     * state. The buffer can be erased manually with the clearBuffer procedure. One application for using this function
     * are situation where the background is rendered manually through a direct manipulation of the pixel buffer (see
     * DirectAccess.ino example).</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param mode
     *         0, to turn off automatic clearing of the internal pixel buffer. Default value is 1.
     *
     * @return The width of the buffer in tiles.
     *
     * @see #getBuffer
     */
    public static native int setAutoPageClear(long id, int mode);

    /**
     * <p> Defines, whether the bitmap functions will write the background color (mode 0/solid, mode = 0) or
     * not (mode 1/transparent, mode = 1). Default mode is 0 (solid mode).</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param mode
     *         Enable (1) or disable (0) transparent mode.
     *
     * @see #drawBitmap
     * @see #drawXBM
     */
    public static native void setBitmapMode(long id, int mode);

    /**
     * <p>Set the contrast or brightness for the display (if supported). Range for 'value': 0 (no contrast) to 255
     * (maximum contrast or brightness).</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param value
     *         Contrast or brightness from 0 to 255.
     */
    public static native void setContrast(long id, int value);

    /**
     * <p>Changes the display rotation. Usually the rotation is defined as part of the constructor. The argment
     * can be one of the following values:</p>
     *
     * <table>
     * <thead>
     * <tr>
     * <th><code>Constant</code></th>
     * <th>Description</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td><code>{@link #ROTATION_R0}</code></td>
     * <td>No rotation, landscape</td>
     * </tr>
     * <tr>
     * <td><code>{@link #ROTATION_R1}</code></td>
     * <td>90 degree clockwise rotation</td>
     * </tr>
     * <tr>
     * <td><code>{@link #ROTATION_R2}</code></td>
     * <td>180 degree clockwise rotation</td>
     * </tr>
     * <tr>
     * <td><code>{@link #ROTATION_R3}</code></td>
     * <td>270 degree clockwise rotation</td>
     * </tr>
     * <tr>
     * <td><code>{@link #ROTATION_MIRROR}</code></td>
     * <td>No rotation, landscape, display content is mirrored (v2.6.x)</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param rotation
     *         Display rotation argument.
     */
    public static native void setDisplayRotation(long id, int rotation);

    /**
     * <p>Return the address of the start of the buffer. This is a also the address of the leftmost tile of the current
     * page (Byte 0 in the above memory structure). The total memory size of the buffer is 8 *
     * u8g2.getBufferTileHeight() * u8g2.getBufferTileWidth(). The buffer can be erased with {@link
     * #clearBuffer(long)}.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return Address of the internal page buffer.
     *
     * @see #getBufferTileHeight
     * @see #getBufferTileWidth
     * @see #clearBuffer
     */
    public static native int getBuffer(long id);

    /**
     * <p>Return the width of the page buffer in tiles (One tile has a width of 8 pixel).</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return The width of the buffer in tiles.
     *
     * @see #getBuffer
     * @see #getBufferTileHeight
     */
    public static native int getBufferTileWidth(long id);

    /**
     * <p>Return the height of the page buffer in tiles. The height of one tile is 8 pixel.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return The height of the buffer in tiles.
     *
     * @see #getBuffer
     * @see #getBufferTileWidth
     */
    public static native int getBufferTileHeight(long id);

    /**
     * <p>Return the intended position for the content of the pixel buffer (page) on the target display. If it is
     * assumed, that the buffer will be placed at the top of the display, then this value is zero. This value is
     * modified by {@link #firstPage(long)}/{@link #nextPage(long)} and used by {@link #sendBuffer(long)} to place the
     * content of the pixel buffer at the target position.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     *
     * @return The current page position in tiles (one tile has a height of 8 pixel)
     *
     * @see #getBuffer(long)
     */
    public static native int getBufferCurrTileRow(long id);

    /**
     * <p>Set the position of the pixel buffer for the sendBuffer command.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param row
     *         Location for the pixel buffer on the display. row is the "tile" position and must be multiplied with 8 to
     *         get the pixel position.
     *
     * @apiNote Never use this command inside of the {@link #firstPage(long)}/{@link #nextPage(long)} loop. It may cause
     * an infinite loop if the current position is modified.
     * @see #getBuffer
     * @see #sendBuffer
     */
    public static native void setBufferCurrTileRow(long id, int row);


    /**
     * <p>Return the pixel width of string.</p>
     *
     * @param id
     *         The display instance id retrieved via {@link #setup(String, int, int, int, int, byte[], boolean)}
     * @param text
     *         Text string to be measured
     *
     * @return Width of the string if drawn with the current font (setFont).
     *
     * @see #setFont(long, String)
     * @see #setFont(long, byte[])
     * @see #drawString(long, int, int, String)
     */
    public static native int getStrWidth(long id, String text);
}
