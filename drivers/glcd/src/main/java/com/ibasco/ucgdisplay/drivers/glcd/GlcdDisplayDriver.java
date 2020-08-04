/*-
 * ========================START=================================
 * UCGDisplay :: Graphics LCD driver
 * %%
 * Copyright (C) 2018 - 2020 Universal Character/Graphics display library
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * =========================END==================================
 */
package com.ibasco.ucgdisplay.drivers.glcd;

import com.ibasco.ucgdisplay.common.drivers.DisplayDriver;
import com.ibasco.ucgdisplay.drivers.glcd.enums.*;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * Graphics Display Driver interface based on the U8G2 Interface.
 *
 * @author Rafael Ibasco
 * @see <a href="https://github.com/olikraus/u8g2/wiki/u8g2reference#drawbox">U8G2 Reference</a>
 */
@SuppressWarnings("unused")
public interface GlcdDisplayDriver extends DisplayDriver {

    /**
     * <p>Draw a box (filled frame), starting at x/y position (upper left edge). The box has width w and height h.
     * Parts of the box can be outside of the display boundaries. This procedure will use the current color
     * (setDrawColor) to draw the box. For a monochrome display, the color index 0 will clear a pixel and the color
     * index 1 will set a pixel.
     * </p>
     *
     * @param width
     *         Width of the box.
     * @param height
     *         Height of the box.
     *
     * @see #setDrawColor(GlcdDrawColor)
     */
    void drawBox(int width, int height);

    /**
     * <p>Draw a box (filled frame), starting at x/y position (upper left edge). The box has width w and height h.
     * Parts of the box can be outside of the display boundaries. This procedure will use the current color
     * (setDrawColor) to draw the box. For a monochrome display, the color index 0 will clear a pixel and the color
     * index 1 will set a pixel.
     * </p>
     *
     * @param x
     *         X-position of upper left edge.
     * @param y
     *         Y-position of upper left edge.
     * @param width
     *         Width of the box.
     * @param height
     *         Height of the box.
     */
    void drawBox(int x, int y, int width, int height);

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
     * @param radius
     *         Defines the size of the circle: Radus = rad
     * @param options
     *         Selects some or all sections of the circle.
     *
     * @see #drawCircle(int, int, int, int)
     */
    void drawCircle(int radius, int options);

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
     * @param x
     *         X-Position of the center of the circle.
     * @param y
     *         Y-Position of the center of the circle.
     * @param radius
     *         Defines the size of the circle: Radus = rad
     * @param options
     *         Selects some or all sections of the circle.
     */
    void drawCircle(int x, int y, int radius, int options);

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
     * These values can be combined with the | operator. This procedure will use the current color ({@link
     * #setDrawColor(GlcdDrawColor)}) for drawing.</p>
     *
     * @param radius
     *         Defines the size of the circle: Radus = rad.
     * @param options
     *         Selects some or all sections of the disc.
     */
    void drawDisc(int radius, int options);

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
     * These values can be combined with the | operator. This procedure will use the current color ({@link
     * #setDrawColor(GlcdDrawColor)}) for drawing.</p>
     *
     * @param x
     *         X-Position of the center of the disc.
     * @param y
     *         Y-Position of the center of the disc.
     * @param radius
     *         Defines the size of the circle: Radus = rad.
     * @param options
     *         Selects some or all sections of the disc.
     */
    void drawDisc(int x, int y, int radius, int options);

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
     * @param rx
     *         Defines the size of the ellipse.
     * @param ry
     *         Defines the size of the ellipse.
     * @param options
     *         Selects some or all sections of the ellipse.
     */
    void drawEllipse(int rx, int ry, int options);

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
    void drawEllipse(int x, int y, int rx, int ry, int options);

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
     * @param rx
     *         X-the size of the ellipse.
     * @param ry
     *         Y- the size of the ellipse.
     * @param options
     *         Selects some or all sections of the ellipse.
     */
    void drawFilledEllipse(int rx, int ry, int options);

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
    void drawFilledEllipse(int x, int y, int rx, int ry, int options);

    /**
     * <p>Draw a frame (empty box), starting at x/y position (upper left edge). The box has width w and height h.
     * Parts of the frame can be outside of the display boundaries. This procedure will use the current color
     * (setDrawColor) to draw the box. For a monochrome display, the color index 0 will clear a pixel and the color
     * index 1 will set a pixel.</p>
     *
     * @param width
     *         Width of the frame.
     * @param height
     *         Height of the frame.
     */
    void drawFrame(int width, int height);

    /**
     * <p>Draw a frame (empty box), starting at x/y position (upper left edge). The box has width w and height h.
     * Parts of the frame can be outside of the display boundaries. This procedure will use the current color
     * (setDrawColor) to draw the box. For a monochrome display, the color index 0 will clear a pixel and the color
     * index 1 will set a pixel.</p>
     *
     * @param x
     *         X-position of upper left edge.
     * @param y
     *         Y-position of upper left edge.
     * @param width
     *         Width of the frame.
     * @param height
     *         Height of the frame.
     */
    void drawFrame(int x, int y, int width, int height);

    /**
     * <p>Draw a single character. The character is placed at the specified pixel posion x and y. U8g2 supports the
     * lower 16 bit of the unicode character range (plane 0/Basic Multilingual Plane): The encoding can be any value
     * from 0 to 65535. The glyph can be drawn only, if the encoding exists in the active font.</p>
     *
     * @param encoding
     *         Unicode value of the character.
     *
     * @see #setFont(byte[])
     */
    void drawGlyph(short encoding);

    /**
     * <p>Draw a single character. The character is placed at the specified pixel posion x and y. U8g2 supports the
     * lower 16 bit of the unicode character range (plane 0/Basic Multilingual Plane): The encoding can be any value
     * from 0 to 65535. The glyph can be drawn only, if the encoding exists in the active font.</p>
     *
     * @param x
     *         X-Position of the character on the display.
     * @param y
     *         Y-Position of the character on the display.
     * @param encoding
     *         Unicode value of the character.
     *
     * @see #setFont(byte[])
     */
    void drawGlyph(int x, int y, short encoding);

    /**
     * <p>Draw a horizontal line, starting at x/y position (left edge). The width (length) of the line is w pixel.
     * Parts of the line can be outside of the display boundaries. This procedure uses the current color index to draw
     * the line. Color index 0 will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param width
     *         Length of the horizontal line
     */
    void drawHLine(int width);

    /**
     * <p>Draw a horizontal line, starting at x/y position (left edge). The width (length) of the line is w pixel.
     * Parts of the line can be outside of the display boundaries. This procedure uses the current color index to draw
     * the line. Color index 0 will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param x
     *         X-Position
     * @param y
     *         Y-Position
     * @param width
     *         Length of the horizontal line
     */
    void drawHLine(int x, int y, int width);

    /**
     * <p>Draw a vertical line, starting at x/y position (upper end). The height (length) of the line is h pixel. Parts
     * of the line can be outside of the display boundaries. This procedure uses the current color index to draw the
     * line. Color index 0 will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param length
     *         Length of the vertical line.
     */
    void drawVLine(int length);

    /**
     * <p>Draw a vertical line, starting at x/y position (upper end). The height (length) of the line is h pixel. Parts
     * of the line can be outside of the display boundaries. This procedure uses the current color index to draw the
     * line. Color index 0 will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param x
     *         X-position.
     * @param y
     *         Y-position.
     * @param length
     *         Length of the vertical line.
     */
    void drawVLine(int x, int y, int length);

    /**
     * Draw a line between two points. This procedure will use the current color (setDrawColor).
     *
     * @param x1
     *         X-position of the second point.
     * @param y1
     *         Y-position of the second point.
     */
    void drawLine(int x1, int y1);

    /**
     * Draw a line between two points. This procedure will use the current color (setDrawColor).
     *
     * @param x
     *         X-position of the first point.
     * @param y
     *         Y-position of the first point.
     * @param x1
     *         X-position of the second point.
     * @param y1
     *         Y-position of the second point.
     */
    void drawLine(int x, int y, int x1, int y1);

    /**
     * <p>Draw a pixel at the specified x/y position. Position (0,0) is at the upper left corner of the display. The
     * position may be outside the display boundaries.This procedure uses the current color index to draw the pixel. The
     * color index 0 will clear a pixel and the color index 1 will set a pixel.</p>
     */
    void drawPixel();

    /**
     * <p>Draw a pixel at the specified x/y position. Position (0,0) is at the upper left corner of the display. The
     * position may be outside the display boundaries.This procedure uses the current color index to draw the pixel. The
     * color index 0 will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param x
     *         X-position.
     * @param y
     *         Y-position.
     */
    void drawPixel(int x, int y);

    /**
     * <p>Draw a box with round edges, starting at x/y position (upper left edge). The box/frame has width w and height
     * h. Parts of the box can be outside of the display boundaries. Edges have radius r. It is required that w &gt;=
     * 2*(r+1) and h &gt;= 2*(r+1). This condition is not checked. Behavior is undefined if w or h is smaller than 2*(r+1).
     * This procedure uses the current color index to draw the box. For a monochrome display, the color index 0 will
     * clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param width
     *         Width of the box.
     * @param height
     *         Height of the box.
     * @param radius
     *         Radius for the four edges.
     */
    void drawRoundedBox(int width, int height, int radius);

    /**
     * <p>Draw a box with round edges, starting at x/y position (upper left edge). The box/frame has width w and height
     * h. Parts of the box can be outside of the display boundaries. Edges have radius r. It is required that w &gt;=
     * 2*(r+1) and h &gt;= 2*(r+1). This condition is not checked. Behavior is undefined if w or h is smaller than 2*(r+1).
     * This procedure uses the current color index to draw the box. For a monochrome display, the color index 0 will
     * clear a pixel and the color index 1 will set a pixel.</p>
     *
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
    void drawRoundedBox(int x, int y, int width, int height, int radius);

    /**
     * <p>Draw a frame with round edges, starting at x/y position (upper left edge). The box/frame has width w and
     * height h. Parts of the box can be outside of the display boundaries. Edges have radius r. It is required that w
     * &gt;= 2*(r+1) and h &gt;= 2*(r+1). This condition is not checked. Behavior is undefined if w or h is smaller than
     * 2*(r+1). This procedure uses the current color index to draw the box. For a monochrome display, the color index 0
     * will clear a pixel and the color index 1 will set a pixel.</p>
     *
     * @param width
     *         Width of the box.
     * @param height
     *         Height of the box.
     * @param radius
     *         Radius for the four edges.
     */
    void drawRoundedFrame(int width, int height, int radius);

    /**
     * <p>Draw a frame with round edges, starting at x/y position (upper left edge). The box/frame has width w and
     * height h. Parts of the box can be outside of the display boundaries. Edges have radius r. It is required that w
     * &gt;= 2*(r+1) and h &gt;= 2*(r+1). This condition is not checked. Behavior is undefined if w or h is smaller than
     * 2*(r+1). This procedure uses the current color index to draw the box. For a monochrome display, the color index 0
     * will clear a pixel and the color index 1 will set a pixel.</p>
     *
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
    void drawRoundedFrame(int x, int y, int width, int height, int radius);

    /**
     * <p>Draw a string. The first character is placed at position x andy. Use setFont to assign a font before drawing
     * a string on the display. To draw a character with encoding 127 to 255, use the C/C++/Arduino escape sequence
     * "\xab" (hex value ab) or "\xyz" (octal value xyz). This function can not draw any glyph with encoding greater or
     * equal to 256. Use drawUTF8 or drawGlyph to access glyphs with encoding greater or equal to 256.</p>
     *
     * @param value
     *         The text to draw on the display
     */
    void drawString(String value);

    /**
     * <p>Draw a string. The first character is placed at position x andy. Use setFont to assign a font before drawing
     * a string on the display. To draw a character with encoding 127 to 255, use the C/C++/Arduino escape sequence
     * "\xab" (hex value ab) or "\xyz" (octal value xyz). This function can not draw any glyph with encoding greater or
     * equal to 256. Use drawUTF8 or drawGlyph to access glyphs with encoding greater or equal to 256.</p>
     *
     * @param x
     *         X-Position of the first character on the display.
     * @param y
     *         Y-Position of the first character on the display.
     * @param value
     *         The text to draw on the display
     */
    void drawString(int x, int y, String value);

    /**
     * <p>Draw a triangle (filled polygon). Arguments are 16 bit and the polygon is clipped to the size of the display.
     * Multiple polygons are drawn so that they exactly match without overlap:The left side of a polygon is drawn, the
     * right side is not draw. The upper side is only draw if it is flat.</p>
     *
     * @param x1
     *         X-position point 1.
     * @param y1
     *         Y-position point 1.
     * @param x2
     *         X-position point 2.
     * @param y2
     *         Y-position point 2.
     */
    void drawTriangle(int x1, int y1, int x2, int y2);

    /**
     * <p>Draw a triangle (filled polygon). Arguments are 16 bit and the polygon is clipped to the size of the display.
     * Multiple polygons are drawn so that they exactly match without overlap:The left side of a polygon is drawn, the
     * right side is not draw. The upper side is only draw if it is flat.</p>
     *
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
    void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2);

    /**
     * <p>Draw a <a href="http://en.wikipedia.org/wiki/X_BitMap">XBM Bitmap</a>. Position (x,y) is the upper left
     * corner of the bitmap. XBM contains monochrome, 1-bit bitmaps.</p>
     *
     * <p>The current color index is used for drawing <strike>(see setColorIndex)</strike> pixel values 1. By default, drawXBM will draw solid
     * bitmaps, use {@link #setBitmapMode(GlcdBitmapMode)} to switch between modes (solid or transparent). The
     * XBMP version of this procedure expects the bitmap to be in PROGMEM area (AVR only). Many tools (including GIMP)
     * can save a bitmap as XBM. A nice step by step instruction is <a href="https://sandhansblog.wordpress.com/2017/04/16/interfacing-displaying-a-custom-graphic-on-an-0-96-i2c-oled/">here</a>
     * (external link). The result will look like this:</p>
     *
     * @param width
     *         Width of the bitmap.
     * @param height
     *         Height of the bitmap.
     * @param file
     *         File containing bitmap data
     *
     * @see #setBitmapMode
     */
    void drawXBM(int width, int height, File file);

    /**
     * <p>Draw a <a href="http://en.wikipedia.org/wiki/X_BitMap">XBM Bitmap</a>. Position (x,y) is the upper left
     * corner of the bitmap. XBM contains monochrome, 1-bit bitmaps.</p>
     *
     * <p>The current color index is used for drawing <strike>(see setColorIndex)</strike> pixel values 1. By default, drawXBM will draw solid
     * bitmaps, use {@link #setBitmapMode(GlcdBitmapMode)} to switch between modes (solid or transparent). The
     * XBMP version of this procedure expects the bitmap to be in PROGMEM area (AVR only). Many tools (including GIMP)
     * can save a bitmap as XBM. A nice step by step instruction is <a href="https://sandhansblog.wordpress.com/2017/04/16/interfacing-displaying-a-custom-graphic-on-an-0-96-i2c-oled/">here</a>
     * (external link). The result will look like this:</p>
     *
     * @param x
     *         X-position.
     * @param y
     *         Y-position.
     * @param width
     *         Width of the bitmap.
     * @param height
     *         Height of the bitmap.
     * @param data
     *         File containing bitmap data
     *
     * @see #setBitmapMode
     */
    void drawXBM(int x, int y, int width, int height, File data);

    /**
     * <p>Draw a <a href="http://en.wikipedia.org/wiki/X_BitMap">XBM Bitmap</a>. Position (x,y) is the upper left
     * corner of the bitmap. XBM contains monochrome, 1-bit bitmaps.</p>
     *
     * <p>The current color index is used for drawing <strike>(see setColorIndex)</strike> pixel values 1. By default, drawXBM will draw solid
     * bitmaps, use {@link #setBitmapMode(GlcdBitmapMode)} to switch between modes (solid or transparent). The
     * XBMP version of this procedure expects the bitmap to be in PROGMEM area (AVR only). Many tools (including GIMP)
     * can save a bitmap as XBM. A nice step by step instruction is <a href="https://sandhansblog.wordpress.com/2017/04/16/interfacing-displaying-a-custom-graphic-on-an-0-96-i2c-oled/">here</a>
     * (external link). The result will look like this:</p>
     *
     * @param width
     *         Width of the bitmap.
     * @param height
     *         Height of the bitmap.
     * @param data
     *         Byte array containing bitmap data
     *
     * @see #setBitmapMode
     */
    void drawXBM(int width, int height, byte[] data);

    /**
     * <p>Draw a <a href="http://en.wikipedia.org/wiki/X_BitMap">XBM Bitmap</a>. Position (x,y) is the upper left
     * corner of the bitmap. XBM contains monochrome, 1-bit bitmaps.</p>
     *
     * <p>The current color index is used for drawing <strike>(see setColorIndex)</strike> pixel values 1. By default, drawXBM will draw solid
     * bitmaps, use {@link #setBitmapMode(GlcdBitmapMode)} to switch between modes (solid or transparent). The
     * XBMP version of this procedure expects the bitmap to be in PROGMEM area (AVR only). Many tools (including GIMP)
     * can save a bitmap as XBM. A nice step by step instruction is <a href="https://sandhansblog.wordpress.com/2017/04/16/interfacing-displaying-a-custom-graphic-on-an-0-96-i2c-oled/">here</a>
     * (external link). The result will look like this:</p>
     *
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
    void drawXBM(int x, int y, int width, int height, byte[] data);

    /**
     * <p>Draw a string which is encoded as UTF-8. There are two preconditions for the use of this function:
     * (A) <strike>the C/C++/Arduino compiler must support UTF-8 encoding (this is default for the gnu compiler, which is also
     * used for most Arduino boards)</strike> and (B) the code editor/IDE must support and store the C/C++/Arduino code as UTF-8
     * (true for the Arduino IDE). If these conditions are met, you can use the character with code value greater than
     * 127 directly in the string (of course the character must exist in the font file, see also setFont). Advantage: No
     * escape codes are required and the source code is more readable. The glyph can be copied and paste into the editor
     * from a "char set" tool. Disadvantage: The code is less portable and the strlen function will not return the
     * number of visible characters.</p>
     *
     * @param value
     *         UTF-8 encoded text
     *
     * @return Width of the string.
     *
     * @apiNote This drawing function depends on the current font mode and drawing color.
     * @see #getUTF8Width(String)
     * @see #setFont
     * @see #drawString(int, int, String)
     */
    int drawUTF8(String value);

    /**
     * <p>Draw a string which is encoded as UTF-8. There are two preconditions for the use of this function:
     * (A) <strike>the C/C++/Arduino compiler must support UTF-8 encoding (this is default for the gnu compiler, which is also
     * used for most Arduino boards)</strike> and (B) the code editor/IDE must support and store the C/C++/Arduino code as UTF-8
     * (true for the Arduino IDE). If these conditions are met, you can use the character with code value greater than
     * 127 directly in the string (of course the character must exist in the font file, see also setFont). Advantage: No
     * escape codes are required and the source code is more readable. The glyph can be copied and paste into the editor
     * from a "char set" tool. Disadvantage: The code is less portable and the strlen function will not return the
     * number of visible characters.</p>
     *
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
     * @see #getUTF8Width(String)
     * @see #setFont
     * @see #drawString(int, int, String)
     */
    int drawUTF8(int x, int y, String value);

    /**
     * <p>Return the pixel width of an UTF-8 encoded string.</p>
     *
     * @param text
     *         UTF-8 encoded text.
     *
     * @return Width of the string if drawn with the current font
     */
    int getUTF8Width(String text);

    /**
     * <p> Define a u8g2 font for the glyph and string drawing functions. Note: u8x8 font can NOT be used. Available
     * fonts are listed here here. The last two characters of the font name define the type and character set for the
     * font:
     * </p>
     *
     * @param data
     *         Font data
     *
     * @see <a href="https://github.com/olikraus/u8g2/wiki/fntlistall">List of available fonts</a>
     */
    void setFont(byte[] data);

    /**
     * <p> Define a u8g2 font for the glyph and string drawing functions. Note: u8x8 font can NOT be used. Available
     * fonts are listed here here. The last two characters of the font name define the type and character set for the
     * font:
     * </p>
     *
     * @param font
     *         A {@link GlcdFont} instance
     *
     * @see <a href="https://github.com/olikraus/u8g2/wiki/fntlistall">List of available fonts</a>
     */
    void setFont(GlcdFont font);

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
     * @param mode
     *         Enable (1) or disable (0) transparent mode.
     *
     * @see #setDrawColor
     * @see #setFont
     */
    void setFontMode(GlcdFontMode mode);

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
     * @param direction
     *         Writing direction/string rotation.
     *
     * @see #drawString(int, int, String)
     */
    void setFontDirection(GlcdFontDirection direction);

    /**
     * <p>Change the reference position for the glyph and string draw functions. By default the reference position is
     * "Baseline".</p>
     */
    void setFontPosBaseline();

    /**
     * <p>Change the reference position for the glyph and string draw functions. By default the reference position is
     * "Baseline".</p>
     */
    void setFontPosBottom();

    /**
     * <p>Change the reference position for the glyph and string draw functions. By default the reference position is
     * "Baseline".</p>
     */
    void setFontPosTop();

    /**
     * <p>Change the reference position for the glyph and string draw functions. By default the reference position is
     * "Baseline".</p>
     */
    void setFontPosCenter();

    /**
     * <p>A call to this procedure will define the calculation method for the ascent and descent of the current
     * font. This method will be used for the current and all other fonts, which will be set with {@link
     * #setFont(byte[])}. Changing this calculation method has an effect on {@link #getAscent()} and {@link
     * #getDescent()}. Default is {@link #setFontRefHeightText()}. </p>
     * <p>
     * Ascent will be the highest ascent of all glyphs of the current font. Descent will be the highest descent of all
     * glyphs of the current font.
     * </p>
     *
     * @see #getAscent
     * @see #getDescent
     */
    void setFontRefHeightAll();

    /**
     * <p>A call to this procedure will define the calculation method for the ascent and descent of the current
     * font. This method will be used for the current and all other fonts, which will be set with {@link
     * #setFont(byte[])}. Changing this calculation method has an effect on {@link #getAscent()} and {@link
     * #getDescent()}.
     *
     * <p>Ascent will be the largest ascent of "A", "1" or "(" of the current font. Descent will be the descent of "g"
     * or "(" of the current font.</p>
     *
     * @see #getAscent
     * @see #getDescent
     */
    void setFontRefHeightExtendedText();

    /**
     * <p>A call to this procedure will define the calculation method for the ascent and descent of the current
     * font. This method will be used for the current and all other fonts, which will be set with {@link
     * #setFont(byte[])}. Changing this calculation method has an effect on {@link #getAscent()} and {@link
     * #getDescent()}.
     *
     * <p>Ascent will be the ascent of "A" or "1" of the current font. Descent will be the descent  "g" of the current
     * font (this is the default after startup).</p>
     *
     * @see #getAscent
     * @see #getDescent
     */
    void setFontRefHeightText();

    /**
     * <p>Some displays support a 180 degree rotation of the internal frame buffer. This hardware feature can be
     * controlled with this procedure. Important: Redraw the complete display after changing the flip mode. Best is to
     * clear the display first, then change the flip mode and finally redraw the content. Results will be undefined for
     * any existing content on the screen.</p>
     *
     * @param enable
     *         Enable (true) or disable (false) 180 degree rotation of the display content
     */
    void setFlipMode(boolean enable);

    /**
     * <p>Activates (enable = true) or disables (enable = false) the power save mode of the display. With activated
     * power save mode, nothing will be visible on the display. The content of the RAM of the display is not changed.
     * This procedure is also called from begin.</p>
     *
     * @param enable
     *         Enable (true) or disable (false) power save mode for the display.
     *
     * @see #begin
     */
    void setPowerSave(boolean enable);

    /**
     * <p>Defines the bit value (color index) for all drawing functions. All drawing function will change the display
     * memory to this bit value. Default value is 1. For example the {@link #drawBox(int, int, int, int)} procedure will
     * set all pixels for the defined area to the bit value, provided here. The color value 2 will activate
     * the XOR mode. Exceptions:</p>
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
     * <li>{@link #clear()}, {@link #clearBuffer()}: Both functions will always set the buffer to the pixel value 0.
     * The color argument of setDrawColor is ignored.</li>
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
     * @param color
     *         The {@link GlcdDrawColor} value
     *
     * @see #drawBox(int, int, int, int)
     * @see #drawGlyph(int, int, short)
     * @see #setFontMode(GlcdFontMode)
     */
    void setDrawColor(GlcdDrawColor color);

    /**
     * <p> Reset and configure the display. This procedure must be called before any other procedures draw something on
     * the display. This procedure leaves the display in a power save mode. In order to see something on the screen,
     * disable power save mode first (setPowerSave). This procedure is called by the begin procedure. Either begin or
     * initDisplay must be called initially.</p>
     */
    void initDisplay();

    /**
     * <p> This command is part of the (picture) loop which renders the content of the display. This command must be
     * used together with nextPage. There are some restrictions: Do not change the content when executing this loop.
     * Always redraw everything. It is not possible to redraw only parts of the content. The advantage is lesser RAM
     * consumption compared to a full frame buffer in RAM, see sendBuffer.</p>
     *
     * @apiNote This procedure sets the current page position to zero.
     * @see #nextPage
     */
    void firstPage();

    /**
     * <p> This command is part of the (picture) loop which renders the content of the display. This command must be
     * used together with firstPage. There are some restrictions: Do not change the content when executing this loop.
     * Always redraw everything. It is not possible to redraw only parts oft the content. The advantage is lesser RAM
     * consumption compared to a full frame buffer in RAM, see sendBuffer. This procedure will send a refresh message
     * (refreshDisplay) to an e-Paper/e-Ink device after completion of the loop (just before returning 0).</p>
     *
     * @return 0, once the loop is completed (all data transfered to the display).
     *
     * @apiNote This procedure adds the height (in tile rows) of the current buffer to the current page position.
     * @see #firstPage
     */
    int nextPage();

    /**
     * <p>Returns the reference height of the glyphs above the baseline (ascent). This value depends on the current
     * reference height (see setFontRefHeightAll).</p>
     *
     * @return The ascent of the current font.
     *
     * @see #setFont
     * @see #getDescent
     * @see #setFontRefHeightAll
     */
    int getAscent();

    /**
     * <p>Returns the reference height of the glyphs below the baseline (descent). For most fonts, this value will be
     * negative. This value depends on the current reference height (see {@link #setFontRefHeightAll()}).</p>
     *
     * @return The descent of the current font.
     *
     * @see #setFont
     * @see #getDescent
     * @see #setFontRefHeightAll
     */
    int getDescent();

    /**
     * <p>Each glyph is stored as a bitmap. This returns the width of the largest bitmap in the font.</p>
     *
     * @return The largest width of any glyph in the font.
     *
     * @see #getMaxCharHeight
     */
    int getMaxCharWidth();

    /**
     * <p>Each glyph is stored as a bitmap. This returns the height of the largest bitmap in the font.</p>
     *
     * @return The largest height of any glyph in the font.
     *
     * @see #getMaxCharWidth
     */
    int getMaxCharHeight();

    /**
     * <p>Send the content of the memory frame buffer to the display. Use {@link #clearBuffer()} to clear the buffer
     * and the draw functions to draw something into the frame buffer. This procedure is useful only with a full frame
     * buffer in the RAM of the microcontroller (Constructor with buffer option "f", see here). This procedure will also
     * send a refresh message (refreshDisplay) to an e-Paper/e-Ink device.</p>
     *
     * @apiNote Actually this procedure will send the current page to the display. This means, the content of the
     * internal pixel buffer will be placed in the tile row given by the current page position. This means, that this
     * procedure could be used for partial updates on paged devices (constructor with buffer option "1" or "2").
     * However, this will only work for LCDs. It will not work with most e-Paper/e-Ink devices because of the buffer
     * switch in the display controller. Conclusion: Use this command only together with full buffer constructors. It
     * will then work with all LCDs and e-Paper/e-Ink devices.
     * @see #clearBuffer
     */
    void sendBuffer();

    /**
     * <p>Clears all pixel in the memory frame buffer. Use sendBuffer to transfer the cleared frame buffer to the
     * display. In most cases, this procedure is useful only with a full frame buffer in the RAM of the microcontroller
     * (Constructor with buffer option "f", see here). This procedure will also send a refresh message (refreshDisplay)
     * to an e-Paper/e-Ink device.</p>
     *
     * @see #sendBuffer
     */
    void clearBuffer();

    /**
     * <p> Clears all pixel on the connected display. This procedure is also called from begin. Usually there is no
     * need to call this function except for the init procedure. Other procedures like {@link #sendBuffer()} and {@link
     * #nextPage()} will also overwrite (and clear) the display.</p>
     */
    void clearDisplay();

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
     * <li>{@link #initDisplay()}</li>
     * <li>{@link #clearDisplay()}</li>
     * <li>{@link #setPowerSave(boolean)} ()}</li>
     * </ol>
     *
     * @see #initDisplay
     * @see #setPowerSave
     * @see #clearDisplay
     */
    void begin();

    /**
     * <p>Clears all pixel on the display and the buffer. Puts the cursor for the print function into the upper left
     * corner. </p>
     * <p>clear will call: </p>
     * <ol>
     * <li><strike>home()</strike></li>
     * <li>{@link #clearDisplay()}</li>
     * <li>{@link #clearBuffer()}</li>
     * </ol>
     *
     * @see #clearBuffer
     */
    void clear();

    /**
     * <p>Enables (mode=1) or disables (mode=0) automatic clearing of the pixel buffer by the {@link #firstPage()} and
     * {@link #nextPage()} procedures. By default this is enabled and in most situation it is not required to disable
     * this. If disabled, the user is responsible to set ALL pixel of the current pixel buffer to some suitable state.
     * The buffer can be erased manually with the clearBuffer procedure. One application for using this function are
     * situation where the background is rendered manually through a direct manipulation of the pixel buffer (see
     * DirectAccess.ino example).</p>
     *
     * @param clear
     *         Set to <code>false</code> to turn off automatic clearing of the internal pixel buffer. Default value is <code>true</code>.
     *
     * @return The width of the buffer in tiles.
     *
     * @see #getBuffer
     */
    int setAutoPageClear(boolean clear);

    /**
     * <p> Defines, whether the bitmap functions will write the background color to solid or transparent. Default mode is {@link GlcdBitmapMode#SOLID}.</p>
     *
     * @param mode
     *         Use {@link GlcdBitmapMode#SOLID} for a solid bitmap background or {@link GlcdBitmapMode#TRANSPARENT} for transparent.
     *
     * @see #drawXBM
     * @see <a href="https://github.com/olikraus/u8g2/wiki/u8g2reference#setbitmapmode">U8g2 - setBitmapMode</a>
     */
    void setBitmapMode(GlcdBitmapMode mode);

    /**
     * <p>Set the contrast or brightness for the display (if supported). Range for 'value': 0 (no contrast) to 255
     * (maximum contrast or brightness).</p>
     *
     * @param value
     *         Contrast or brightness from 0 to 255.
     */
    void setContrast(int value);

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
     * <td><code>ROTATION_R0</code></td>
     * <td>No rotation, landscape</td>
     * </tr>
     * <tr>
     * <td><code>ROTATION_R1</code></td>
     * <td>90 degree clockwise rotation</td>
     * </tr>
     * <tr>
     * <td><code>ROTATION_R2</code></td>
     * <td>180 degree clockwise rotation</td>
     * </tr>
     * <tr>
     * <td><code>ROTATION_R3</code></td>
     * <td>270 degree clockwise rotation</td>
     * </tr>
     * <tr>
     * <td><code>ROTATION_MIRROR</code></td>
     * <td>No rotation, landscape, display content is mirrored</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param rotation
     *         Display rotation argument.
     */
    void setDisplayRotation(int rotation);

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
     * <td><code>{@link GlcdRotation#ROTATION_NONE}</code></td>
     * <td>No rotation, landscape</td>
     * </tr>
     * <tr>
     * <td><code>{@link GlcdRotation#ROTATION_90}</code></td>
     * <td>90 degree clockwise rotation</td>
     * </tr>
     * <tr>
     * <td><code>{@link GlcdRotation#ROTATION_180}</code></td>
     * <td>180 degree clockwise rotation</td>
     * </tr>
     * <tr>
     * <td><code>{@link GlcdRotation#ROTATION_270}</code></td>
     * <td>270 degree clockwise rotation</td>
     * </tr>
     * <tr>
     * <td><code>{@link GlcdRotation#ROTATION_MIRROR}</code></td>
     * <td>No rotation, landscape, display content is mirrored</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param rotation
     *         Display rotation argument.
     */
    void setDisplayRotation(GlcdRotation rotation);

    /**
     * <p>Return the contents of the display buffer. The total memory size of the buffer is 8 *
     * ({@link #getBufferTileHeight()} * {@link #getBufferTileWidth()}). The buffer can be erased with {@link
     * #clearBuffer()}.</p>
     *
     * @return The contents of th
     *
     * @see #getBufferTileHeight
     * @see #getBufferTileWidth
     * @see #clearBuffer
     */
    byte[] getBuffer();

    /**
     * Returns the pixel buffer that is shared with the native library
     *
     * @return A direct {@link ByteBuffer}
     */
    ByteBuffer getNativeBuffer();

    /**
     * Similar to {@link #getNativeBuffer()} but this returns a buffer in bgra pixel format.
     *
     * @return A direct {@link ByteBuffer}. Null if virtual mode is false.
     */
    ByteBuffer getNativeBgraBuffer();

    /**
     * Returns the total size of the internal display buffer
     *
     * @return The buffer size in bytes
     */
    default int getBufferSize() {
        return 8 * (getBufferTileHeight() * getBufferTileWidth());
    }

    /**
     * <p>Return the width of the page buffer in tiles (One tile has a width of 8 pixel).</p>
     *
     * @return The width of the buffer in tiles.
     *
     * @see #getBuffer
     * @see #getBufferTileHeight
     */
    int getBufferTileWidth();

    /**
     * <p>Return the height of the page buffer in tiles. The height of one tile is 8 pixel.</p>
     *
     * @return The height of the buffer in tiles.
     *
     * @see #getBuffer
     * @see #getBufferTileWidth
     */
    int getBufferTileHeight();

    /**
     * <p>Return the intended position for the content of the pixel buffer (page) on the target display. If it is
     * assumed, that the buffer will be placed at the top of the display, then this value is zero. This value is
     * modified by {@link #firstPage()}/{@link #nextPage()} and used by {@link #sendBuffer()} to place the content of
     * the pixel buffer at the target position.</p>
     *
     * @return The current page position in tiles (one tile has a height of 8 pixel)
     *
     * @see #getBuffer()
     */
    int getBufferCurrTileRow();

    /**
     * <p>Set the position of the pixel buffer for the sendBuffer command.</p>
     *
     * @param row
     *         Location for the pixel buffer on the display. row is the "tile" position and must be multiplied with 8 to
     *         get the pixel position.
     *
     * @apiNote Never use this command inside of the {@link #firstPage()}/{@link #nextPage()} loop. It may cause an
     * infinite loop if the current position is modified.
     * @see #getBuffer
     * @see #sendBuffer
     */
    void setBufferCurrTileRow(int row);

    /**
     * <p>Return the pixel width of string.</p>
     *
     * @param text
     *         Text string to be measured
     *
     * @return Width of the string if drawn with the current font (setFont).
     *
     * @see #setFont(GlcdFont)
     * @see #setFont(byte[])
     * @see #drawString(int, int, String)
     */
    int getStrWidth(String text);

    /**
     * <p>Restricts all graphics output to the specified range. The range is defined from x0 (included) to x1 (excluded) and y0 (included) to y1 (excluded).
     * Use setMaxClipWindow to restore writing to the complete window.</p>
     *
     * @param x0
     *         Left edge of the visible area.
     * @param y0
     *         Upper edge of the visible area.
     * @param x1
     *         Right edge +1 of the visible area.
     * @param y1
     *         Lower edge +1 of the visible area.
     *
     * @see #setMaxClipWindow()
     */
    void setClipWindow(int x0, int y0, int x1, int y1);

    /**
     * <p>Removes the effect of {@link #setClipWindow(int, int, int, int)}. Graphics is written to the complete display.</p>
     *
     * @see #setClipWindow(int, int, int, int)
     */
    void setMaxClipWindow();

    /**
     * <p>
     * Updates all area of the display. This is almost identical to {@link #sendBuffer()}. The area has to be specified in tiles.
     * One tile is a 8x8 pixel area. To get the pixel value, multiply the tile value with 8 (for ROTATION_R0).
     * </p>
     *
     * <p>The tile coordinates are independent from the applied rotation in the U8g2 constructor but have the same
     * orientation as ROTATION_R0. For other rotations the calculation between pixel value tile position is more
     * complicated. The three member functions {@link #sendBuffer()}, {@link #updateDisplay()} and {@link #updateDisplay(int, int, int, int)} are designed for the
     * full buffer mode (constructor with _F_ in the name). However {@link #sendBuffer()} and {@link #updateDisplay()} can be used in
     * page mode also. If updateDisplay is used together with ePaper displays, ensure that a proper refresh sequence is
     * send to the display.</p>
     *
     * <p>Differences between {@link #sendBuffer()}, {@link #updateDisplay()} and {@link #updateDisplay(int, int, int, int)}</p>
     *
     * <table>
     * <thead>
     * <tr>
     * <th>Behavior/Feature</th>
     * <th><code>sendBuffer</code></th>
     * <th><code>updateDisplay</code></th>
     * <th><code>updateDisplayArea</code></th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>Sends a refreshDisplay message</td>
     * <td>yes</td>
     * <td>no</td>
     * <td>no</td>
     * </tr>
     * <tr>
     * <td>Works in full buffer mode</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * </tr>
     * <tr>
     * <td>Works in page buffer mode</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>no</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @apiNote <ul>
     * <li>Range for tx: 0..getBufferTileWidth()-1 and for ty: 0..getBufferTileHeight()-1. There is no overflow check.
     * The area must fully fit into the display area.
     * Especially the following conditions must be true: tx+tw <= getBufferTileWidth() and ty+th <= getBufferTileHeight().</li>
     * <li>
     * <p>setClipWindow vs updateDisplayArea: Both may generate similar visual effects, however..</p>
     * <h2>setClipWindow</h2>
     *     <ul>
     *          <li>Pixel coordinates</li>
     *          <li>Used within the firstPage/nextPage loop</li>
     *          <li>Valid for full and page buffer mode</li>
     *          <li>Will limit the number of pixel drawn into the buffer</li>
     *          <li>Performance increase due to lesser pixel painting</li>
     *          <li>Will work with any rotation command in the constructor</li>
     *          <li>Will work with any setting for u8g2.setFlipMode()</li>
     *      </ul>
     *      <h2>updateDisplayArea</h2>
     *      <ul>
     *          <li>Tile coordinates</li>
     *          <li>Must be used outside the firstPage/nextPage loop</li>
     *          <li>Valid only for full buffer mode</li>
     *          <li>Will limit the data transfer to the display</li>
     *          <li>Performance increase due to lesser data transfer to the display</li>
     *          <li>Will work with any rotation command in the constructor, but requires more complicated calculation for the tile coordinates if the rotation is not U8G2_R0.</li>
     *          <li>Will work with any setting for u8g2.setFlipMode()</li>
     *      </ul>
     * </li>
     * <li>More details here <a href="https://github.com/olikraus/u8g2/issues/736">Github Issue (736)</a></li>
     * </ul>
     * @apiNote <ul>
     * <li>Range for tx: 0..getBufferTileWidth()-1 and for ty: 0..getBufferTileHeight()-1. There is no overflow check.
     * The area must fully fit into the display area.
     * Especially the following conditions must be true: tx+tw <= getBufferTileWidth() and ty+th <= getBufferTileHeight().</li>
     * <li>
     * <p>setClipWindow vs updateDisplayArea: Both may generate similar visual effects, however..</p>
     * <h2>setClipWindow</h2>
     *     <ul>
     *          <li>Pixel coordinates</li>
     *          <li>Used within the firstPage/nextPage loop</li>
     *          <li>Valid for full and page buffer mode</li>
     *          <li>Will limit the number of pixel drawn into the buffer</li>
     *          <li>Performance increase due to lesser pixel painting</li>
     *          <li>Will work with any rotation command in the constructor</li>
     *          <li>Will work with any setting for u8g2.setFlipMode()</li>
     *      </ul>
     *      <h2>updateDisplayArea</h2>
     *      <ul>
     *          <li>Tile coordinates</li>
     *          <li>Must be used outside the firstPage/nextPage loop</li>
     *          <li>Valid only for full buffer mode</li>
     *          <li>Will limit the data transfer to the display</li>
     *          <li>Performance increase due to lesser data transfer to the display</li>
     *          <li>Will work with any rotation command in the constructor, but requires more complicated calculation for the tile coordinates if the rotation is not U8G2_R0.</li>
     *          <li>Will work with any setting for u8g2.setFlipMode()</li>
     *      </ul>
     * </li>
     * <li>More details here <a href="https://github.com/olikraus/u8g2/issues/736">Github Issue (736)</a></li>
     * </ul>
     */
    void updateDisplay();

    /**
     * <p>
     * Updates the specified rectangle areaof the display. This is almost identical to {@link #sendBuffer()}.
     * The area has to be specified in tiles. One tile is a 8x8 pixel area.
     * To get the pixel value, multiply the tile value with 8 (for ROTATION_R0).
     * </p>
     *
     * <p>The tile coordinates are independent from the applied rotation in the U8g2 constructor but have the same
     * orientation as ROTATION_R0. For other rotations the calculation between pixel value tile position is more
     * complicated. The three member functions {@link #sendBuffer()}, {@link #updateDisplay()} and {@link #updateDisplay(int, int, int, int)} are designed for the
     * full buffer mode (constructor with _F_ in the name). However {@link #sendBuffer()} and {@link #updateDisplay()} can be used in
     * page mode also. If updateDisplay is used together with ePaper displays, ensure that a proper refresh sequence is
     * send to the display.</p>
     *
     * <p>Differences between sendBuffer, updateDisplay and updateDisplayArea</p>
     *
     * <table>
     * <thead>
     * <tr>
     * <th>Behavior/Feature</th>
     * <th><code>sendBuffer</code></th>
     * <th><code>updateDisplay</code></th>
     * <th><code>updateDisplayArea</code></th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>Sends a refreshDisplay message</td>
     * <td>yes</td>
     * <td>no</td>
     * <td>no</td>
     * </tr>
     * <tr>
     * <td>Works in full buffer mode</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>yes</td>
     * </tr>
     * <tr>
     * <td>Works in page buffer mode</td>
     * <td>yes</td>
     * <td>yes</td>
     * <td>no</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param x
     *         The starting x-coordinate of the rectangular region
     * @param y
     *         The starting y-coordinate of the rectangular region
     * @param width
     *         The width of the rectangular region
     * @param height
     *         The height of the rectangular region
     *
     * @apiNote <ul>
     * <li>Range for tx: 0..getBufferTileWidth()-1 and for ty: 0..getBufferTileHeight()-1. There is no overflow check.
     * The area must fully fit into the display area.
     * Especially the following conditions must be true: tx+tw <= getBufferTileWidth() and ty+th <= getBufferTileHeight().</li>
     * <li>
     * <p>setClipWindow vs updateDisplayArea: Both may generate similar visual effects, however..</p>
     * <h2>setClipWindow</h2>
     *     <ul>
     *          <li>Pixel coordinates</li>
     *          <li>Used within the firstPage/nextPage loop</li>
     *          <li>Valid for full and page buffer mode</li>
     *          <li>Will limit the number of pixel drawn into the buffer</li>
     *          <li>Performance increase due to lesser pixel painting</li>
     *          <li>Will work with any rotation command in the constructor</li>
     *          <li>Will work with any setting for u8g2.setFlipMode()</li>
     *      </ul>
     *      <h2>updateDisplayArea</h2>
     *      <ul>
     *          <li>Tile coordinates</li>
     *          <li>Must be used outside the firstPage/nextPage loop</li>
     *          <li>Valid only for full buffer mode</li>
     *          <li>Will limit the data transfer to the display</li>
     *          <li>Performance increase due to lesser data transfer to the display</li>
     *          <li>Will work with any rotation command in the constructor, but requires more complicated calculation for the tile coordinates if the rotation is not U8G2_R0.</li>
     *          <li>Will work with any setting for u8g2.setFlipMode()</li>
     *      </ul>
     * </li>
     * <li>More details here <a href="https://github.com/olikraus/u8g2/issues/736">Github Issue (736)</a></li>
     * </ul>
     */
    void updateDisplay(int x, int y, int width, int height);

    /**
     * Exports an <a href="https://en.wikipedia.org/wiki/X_BitMap">XBM</a> formatted ASCII string representati0on of the current display buffer
     *
     * @return The XBM formatted ASCII representation of the display buffer
     */
    String exportToXBM();

    /**
     * Exports a <a href="https://en.wikipedia.org/wiki/Netpbm_format">PBM</a> formatted ASCII string representation of the current display buffer
     *
     * @return The PBM formatted ASCII representation of the display buffer
     */
    String exportToPBM();

    /**
     * <p>Exports an <a href="https://en.wikipedia.org/wiki/X_BitMap">XBM</a> formatted ASCII string representati0on of the current display buffer.</p>
     *
     * <p>Display controllers: SH1122, LD7032, ST7920, ST7986, LC7981, T6963, SED1330, RA8835, MAX7219, LS0xx</p>
     *
     * @return The XBM formatted ASCII representation of the display buffer
     */
    String exportToXBM2();

    /**
     * <p>Exports a <a href="https://en.wikipedia.org/wiki/Netpbm_format">PBM</a> formatted ASCII string representation of the current display buffer</p>
     *
     * <p>Display controllers: SH1122, LD7032, ST7920, ST7986, LC7981, T6963, SED1330, RA8835, MAX7219, LS0xx</p>
     *
     * @return The PBM formatted ASCII representation of the display buffer
     */
    String exportToPBM2();

    /**
     * <p>Send special commands to the display controller. These commands are specified in the datasheet of the display
     * controller. U8g2 just provides an interface (There is no support on the functionality for these commands).
     * The information is transfered as a sequence of bytes. Each byte has a special meaning:</p>
     * <ul>
     *  <li>Command byte (c): Commands for the controller. Usually this byte will activate or deactivate a feature in the display controller.</li>
     *  <li>Argument (a): Some commands require extra information. A command byte then requires a certain number or arguments.</li>
     *  <li>Pixel data (d): Instructs the display controller to interpret the byte as pixel data, which has to be written to the display memory. In some cases, pixel data require a special command also.</li>
     * </ul>
     *
     * <h2>Example</h2>
     * <p>Send multiple commands with arguments: Activate hardware scroll to the left on a SSD1306 display</p>
     * <pre>
     *     sendCommand("caaaaaac", 0x027, 0, 3, 0, 7, 0, 255, 0x2f);
     * </pre>
     *
     * @param format
     *         A sequence (string) of c, a or d
     * @param args
     *         A sequence of bytes, separated by comma, one byte per char in the fmt string.
     *         The byte will be interpreted accordingly to the char at the same position of the fmt string.
     **/
    void sendCommand(String format, byte... args);
}
