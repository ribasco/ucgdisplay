package com.ibasco.ucgdisplay.examples.lcd.exceptions;

public class CreateCharException extends RuntimeException {
    public CreateCharException() {
    }

    public CreateCharException(String message) {
        super(message);
    }

    public CreateCharException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateCharException(Throwable cause) {
        super(cause);
    }

    public CreateCharException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
