package com.ibasco.ucgdisplay.core.exceptions;

public class GraphicsException extends RuntimeException {
    public GraphicsException() {
    }

    public GraphicsException(String message) {
        super(message);
    }

    public GraphicsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GraphicsException(Throwable cause) {
        super(cause);
    }

    public GraphicsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
