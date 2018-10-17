package com.ibasco.ucgdisplay.drivers.glcd.exceptions;

public class GlcdDriverException extends RuntimeException {
    public GlcdDriverException() {
    }

    public GlcdDriverException(String message) {
        super(message);
    }

    public GlcdDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlcdDriverException(Throwable cause) {
        super(cause);
    }

    public GlcdDriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
