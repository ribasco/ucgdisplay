package com.ibasco.ucgdisplay.drivers.clcd.exceptions;

public class TokenCountMismatchException extends RuntimeException {
    public TokenCountMismatchException() {
    }

    public TokenCountMismatchException(String message) {
        super(message);
    }

    public TokenCountMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenCountMismatchException(Throwable cause) {
        super(cause);
    }

    public TokenCountMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
