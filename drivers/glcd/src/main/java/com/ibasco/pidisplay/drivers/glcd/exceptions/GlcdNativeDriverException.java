package com.ibasco.ucgdisplay.drivers.glcd.exceptions;

public class GlcdNativeDriverException extends GlcdDriverException {
    public GlcdNativeDriverException() {
    }

    public GlcdNativeDriverException(String message) {
        super(message);
    }

    public GlcdNativeDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlcdNativeDriverException(Throwable cause) {
        super(cause);
    }

    public GlcdNativeDriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
