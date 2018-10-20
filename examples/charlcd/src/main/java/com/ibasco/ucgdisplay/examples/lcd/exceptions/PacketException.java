package com.ibasco.ucgdisplay.examples.lcd.exceptions;

public class PacketException extends Exception {
    public PacketException() {
    }

    public PacketException(String message) {
        super(message);
    }

    public PacketException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketException(Throwable cause) {
        super(cause);
    }

    public PacketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
