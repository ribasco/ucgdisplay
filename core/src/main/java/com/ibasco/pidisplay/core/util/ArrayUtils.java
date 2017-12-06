package com.ibasco.pidisplay.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

public class ArrayUtils {
    public static final Logger log = LoggerFactory.getLogger(ArrayUtils.class);

    public static final boolean nonNullBytes(byte b) {
        return b != 0;
    }

    public static int indexOf(final byte[] data, int offset, Predicate<Byte> predicate) {
        for (int i = offset; i < data.length; i++) {
            if (predicate.test(data[i]))
                return i;
        }
        return -1;
    }

    public static int indexOfNonNull(final byte[] data, int offset) {
        for (int i = offset; i < data.length; i++) {
            if (data[i] != 0)
                return i;
        }
        return -1;
    }

    public static int indexOf(final byte[] data, byte search) {
        return indexOf(data, 0, search);
    }

    public static int indexOf(final byte[] data, int offset, byte search) {
        for (int i = offset; i < data.length; i++) {
            if (data[i] == search)
                return i;
        }
        return -1;
    }

    public static int merge(byte[] lhs, final byte[] rhs) {
        checkEmptyArgs(lhs, rhs);
        checkLength(lhs, rhs);
        int lastPos = 0;
        for (int i = 0; i < lhs.length; i++) {
            if (rhs[i] != 0 && (rhs[i] != lhs[i])) {
                lhs[i] = rhs[i];
                lastPos = i;
            }
        }
        return lastPos;
    }

    public static byte[] diff(final byte[] lhs, final byte[] rhs) {
        checkEmptyArgs(lhs, rhs);
        checkLength(lhs, rhs);
        byte[] tmp = new byte[lhs.length];
        boolean hasDiff = false;
        for (int i = 0; i < tmp.length; i++) {
            if (lhs[i] != rhs[i]) {
                tmp[i] = rhs[i];
                hasDiff = true;
            } else
                tmp[i] = 0;
        }
        return (!hasDiff) ? null : tmp;
    }

    private static void checkLength(final byte[] lhs, final byte[] rhs) {
        if (lhs.length != rhs.length)
            throw new IllegalStateException("The argument(s) must be of the equal length");
    }

    private static void checkEmptyArgs(byte[] lhs, byte[] rhs) {
        if (lhs == null || rhs == null || lhs.length == 0 || rhs.length == 0)
            throw new IllegalArgumentException("The argument(s) cannot be null/empty");
    }

    public static byte[] replaceNullBytes(byte[] data, byte replacement) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0)
                data[i] = replacement;
        }
        return data;
    }
}
