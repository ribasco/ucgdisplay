package com.ibasco.pidisplay.core.util;

import org.apache.commons.text.WordUtils;

public class TextUtils {

    public static int countWords(String text) {
        if (text == null || text.isEmpty())
            return 0;
        return text.split("\\s+").length;
    }

    public static String wrapWord(String text, int maxWidth) {
        return WordUtils.wrap(text, maxWidth, "\n", true);
    }

    public static String wrapContinuous(String text, int maxWidth) {
        StringBuilder tmp = new StringBuilder();
        int start, end;
        for (start = 0, end = start + maxWidth; (start < text.length()) && (end < text.length() - 1); start += maxWidth, end = start + maxWidth) {
            tmp.append(text.substring(start, end));
            tmp.append("\n");
        }
        tmp.append(text.substring(start));
        return tmp.toString();
    }
}
