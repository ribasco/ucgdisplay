package com.ibasco.pidisplay.core.exceptions;

public class NotOnUIThreadException extends RuntimeException {
    public NotOnUIThreadException() {
        super("Method should not be called outside the UI Thread");
    }

    public NotOnUIThreadException(String message) {
        super(message);
    }
}
