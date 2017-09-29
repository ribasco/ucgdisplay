package com.ibasco.pidisplay.core.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Rafael Ibasco
 */
public class ByteUtils {
    public static ByteBuffer wrapDirectBuffer(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(data);
        buffer.flip();
        return buffer;
    }
}
