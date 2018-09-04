package com.ibasco.pidisplay.core.exceptions;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super("No implementation for method available");
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
