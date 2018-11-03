package com.ibasco.ucgdisplay.core.u8g2.exceptions;

public class U8g2EventException extends RuntimeException {
    public U8g2EventException() {
    }

    public U8g2EventException(String message) {
        super(message);
    }

    public U8g2EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public U8g2EventException(Throwable cause) {
        super(cause);
    }

    public U8g2EventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
