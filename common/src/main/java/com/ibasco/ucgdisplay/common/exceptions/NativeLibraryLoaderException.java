package com.ibasco.ucgdisplay.common.exceptions;

public class NativeLibraryLoaderException extends RuntimeException {
    public NativeLibraryLoaderException() {
    }

    public NativeLibraryLoaderException(String message) {
        super(message);
    }

    public NativeLibraryLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NativeLibraryLoaderException(Throwable cause) {
        super(cause);
    }

    public NativeLibraryLoaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
