package com.ibasco.ucgdisplay.drivers.clcd;

import java.util.Objects;

/**
 * Represents a custom character to be used by the {@link CharGraphics} interface
 */
public class CharData {
    private byte[] charData;

    private String key;

    private String description;

    public CharData(final String desc, final String key, final byte[] charData) {
        this.charData = charData;
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

    public byte[] getBytes() {
        return charData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharData charData = (CharData) o;
        return Objects.equals(key, charData.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Char: " + this.key;
    }
}
