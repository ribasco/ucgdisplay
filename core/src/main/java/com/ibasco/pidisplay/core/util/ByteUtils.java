package com.ibasco.pidisplay.core.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * @author Rafael Ibasco
 */
public class ByteUtils {
    public static ByteBuffer wrapDirectBuffer(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(data);
        buffer.rewind();
        return buffer;
    }

    public byte[] toByteArray(char[] data, Charset charset) {
        if (charset == null)
            charset = Charset.defaultCharset();
        return new String(data).getBytes(charset);
    }
}
