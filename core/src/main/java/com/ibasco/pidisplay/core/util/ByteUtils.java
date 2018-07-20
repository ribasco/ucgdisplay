package com.ibasco.pidisplay.core.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * Byte related utility methods
 *
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

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Converts short to primitive byte array
     *
     * @param num
     *         The number to convert
     *
     * @return A byte representation of the number (Two bytes for short)
     */
    public static byte[] toByteArray(short num) {
        byte[] ret = new byte[2];
        ret[0] = (byte) (num & 0xff);
        ret[1] = (byte) ((num >> 8) & 0xff);
        return ret;
    }

    public static byte[] toByteArray(char[] data, Charset charset) {
        if (charset == null)
            charset = Charset.defaultCharset();
        return new String(data).getBytes(charset);
    }

    public static void printHexBytes(StringBuilder sb, byte[] data) {
        sb.append("[Size: ");
        sb.append(data.length);
        sb.append("] = ");
        for (byte b : data) {
            sb.append(String.format("%02x", b));
            sb.append(" ");
        }
    }

    public static void printHexBytes(StringBuilder sb, byte[] data, boolean includePrefix) {
        if (includePrefix) {
            sb.append("[Size: ");
            sb.append(data.length);
            sb.append("] = ");
        }
        for (byte b : data) {
            sb.append(String.format("%02x", b));
            sb.append(" ");
        }
    }

    public static String toHexString(byte... data) {
        return toHexString(true, data);
    }

    public static String toHexString(boolean includePrefix, byte... data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        return toHexString(sb, data, includePrefix).trim().toUpperCase();
    }

    public static String toHexString(StringBuilder sb, byte[] data) {
        return toHexString(sb, data, true);
    }

    public static String toHexString(StringBuilder sb, byte[] data, boolean includePrefix) {
        printHexBytes(sb, data, includePrefix);
        return sb.toString();
    }
}
