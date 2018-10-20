package com.ibasco.ucgdisplay.core.exceptions;

public class OffsetOutOfBoundsException extends GraphicsException {

    private final int offset;

    private final int max;

    private final int min;

    public OffsetOutOfBoundsException(final int offset, final int min, final int max) {
        this(offset, min, max, String.format("Offset out of bounds (actual: %d, min: %d, max: %d)", offset, min, max));
    }

    public OffsetOutOfBoundsException(final int offset, final int min, final int max, String message) {
        super(message);
        this.offset = offset;
        this.min = min;
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getOffset() {
        return offset;
    }
}
