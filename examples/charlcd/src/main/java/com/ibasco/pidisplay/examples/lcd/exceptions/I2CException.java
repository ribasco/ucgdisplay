package com.ibasco.pidisplay.examples.lcd.exceptions;

public class I2CException extends Exception {
    public I2CException() {
    }

    public I2CException(String message) {
        super(message);
    }

    public I2CException(String message, Throwable cause) {
        super(message, cause);
    }

    public I2CException(Throwable cause) {
        super(cause);
    }

    public I2CException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
