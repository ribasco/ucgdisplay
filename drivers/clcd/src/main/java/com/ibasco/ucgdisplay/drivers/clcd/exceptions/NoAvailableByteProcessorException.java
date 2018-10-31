package com.ibasco.ucgdisplay.drivers.clcd.exceptions;

public class NoAvailableByteProcessorException extends RuntimeException {

    public NoAvailableByteProcessorException() {
    }

    public NoAvailableByteProcessorException(String message) {
        super(message);
    }

    public NoAvailableByteProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAvailableByteProcessorException(Throwable cause) {
        super(cause);
    }

    public NoAvailableByteProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
