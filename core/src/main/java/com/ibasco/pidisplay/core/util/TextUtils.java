package com.ibasco.pidisplay.core.util;

import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.enums.TextWrapStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

public class TextUtils {

    public static String alignText(String text, TextAlignment alignment, int maxWidth) {
        if (TextAlignment.LEFT.equals(alignment))
            return StringUtils.rightPad(text, maxWidth);
        else if (TextAlignment.RIGHT.equals(alignment))
            return StringUtils.leftPad(text, maxWidth);
        else if (TextAlignment.CENTER.equals(alignment))
            return StringUtils.center(text, maxWidth);
        return text;
    }

    public static int countWords(String text) {
        if (text == null || text.isEmpty())
            return 0;
        return text.split("\\s+").length;
    }

    public static String wrapText(String text, int maxWidth, TextWrapStyle style) {
        switch (style) {
            case WORD:
                return wrapWord(text, maxWidth);
            case CONTINUOUS:
                return wrapContinuous(text, maxWidth);
            default: {
                if (!StringUtils.isBlank(text) && text.length() >= maxWidth) {
                    return text.substring(0, maxWidth - 1);
                }
            }
        }
        return text;
    }

    private static String wrapWord(String text, int maxWidth) {
        return WordUtils.wrap(text, maxWidth, "\n", true);
    }

    private static String wrapContinuous(String text, int maxWidth) {
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
