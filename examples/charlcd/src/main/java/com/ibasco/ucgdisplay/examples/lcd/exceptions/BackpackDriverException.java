package com.ibasco.ucgdisplay.examples.lcd.exceptions;

public class BackpackDriverException extends RuntimeException {
    public BackpackDriverException() {
    }

    public BackpackDriverException(String message) {
        super(message);
    }

    public BackpackDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackpackDriverException(Throwable cause) {
        super(cause);
    }

    public BackpackDriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
