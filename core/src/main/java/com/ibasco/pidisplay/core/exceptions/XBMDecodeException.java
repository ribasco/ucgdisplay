package com.ibasco.pidisplay.core.exceptions;

public class XBMDecodeException extends Exception {
    public XBMDecodeException() {
    }

    public XBMDecodeException(String message) {
        super(message);
    }

    public XBMDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public XBMDecodeException(Throwable cause) {
        super(cause);
    }

    public XBMDecodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
