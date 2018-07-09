package com.ibasco.pidisplay.drivers.glcd.exceptions;

public class GlcdException extends Exception {
    public GlcdException() {
    }

    public GlcdException(String message) {
        super(message);
    }

    public GlcdException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlcdException(Throwable cause) {
        super(cause);
    }

    public GlcdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
