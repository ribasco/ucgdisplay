package com.ibasco.pidisplay.drivers.glcd.exceptions;

/**
 * An exception thrown when the display driver has not yet been initialized
 *
 * @author Rafael Ibasco
 */
public class GlcdNotInitializedException extends GlcdDriverException {
    public GlcdNotInitializedException() {
    }

    public GlcdNotInitializedException(String message) {
        super(message);
    }

    public GlcdNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlcdNotInitializedException(Throwable cause) {
        super(cause);
    }

    public GlcdNotInitializedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
