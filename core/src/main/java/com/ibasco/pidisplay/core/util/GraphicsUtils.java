package com.ibasco.pidisplay.core.util;

import com.ibasco.pidisplay.core.ui.GraphicsBuffer;
import org.apache.commons.lang3.StringUtils;

public class GraphicsUtils {
    public static int calcXOffset(final int width, final int index) {
        return index % width;
    }

    public static int calcYOffset(final int width, final int height, final int index) {
        int y = (int) Math.floor(index / width);
        return y >= height ? height - 1 : y;
    }

    public static int calcOffset(final int width, final int x, final int y) {
        return x + (y * width);
    }

    public static void printBuffer(final GraphicsBuffer buffer, StringBuilder output) {
        printBuffer(buffer.getWidth(), buffer.getHeight(), buffer.array(), output);
    }

    public static void printBuffer(final int width, final int height, final byte[] data, StringBuilder output) {
        if ((width * height) != data.length)
            throw new IllegalArgumentException("Invalid buffer size");

        output.append(StringUtils.SPACE);
        output.append(StringUtils.repeat('-', width));
        output.append(StringUtils.SPACE);
        output.append("\n");
        for (int i = 0; i < data.length; i++) {
            char c = (data[i] == 0) ? ' ' : (char) data[i];
            output.append(c);
            if (GraphicsUtils.calcXOffset(width, i) == width - 1) {
                //log.debug("{}|{}|", GraphicsUtils.calcYOffset(width, height, i), sb.toString());
                output.append(GraphicsUtils.calcYOffset(width, height, i));
                output.append("|");
                if (GraphicsUtils.calcYOffset(width, height, i) < height)
                    output.append("\n");
            }

        }
        output.append("  ");
        output.append(StringUtils.repeat('-', width));
        output.append(" ");
    }
}
