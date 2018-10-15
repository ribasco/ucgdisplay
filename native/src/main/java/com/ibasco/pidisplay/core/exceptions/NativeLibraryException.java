package com.ibasco.pidisplay.core.exceptions;

/**
 * Thrown when an exception occurs on the native layer
 *
 * @author Rafael Ibasco
 */
public class NativeLibraryException extends RuntimeException {
    public NativeLibraryException() {
    }

    public NativeLibraryException(String message) {
        super(message);
    }

    public NativeLibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NativeLibraryException(Throwable cause) {
        super(cause);
    }

    public NativeLibraryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
