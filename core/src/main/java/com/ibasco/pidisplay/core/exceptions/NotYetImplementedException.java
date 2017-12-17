package com.ibasco.pidisplay.core.exceptions;

public class NotYetImplementedException extends RuntimeException {
    public NotYetImplementedException() {
        super("No implementation for method available");
    }

    public NotYetImplementedException(String message) {
        super(message);
    }
}
