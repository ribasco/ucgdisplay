package com.ibasco.ucgdisplay.core.exceptions;

import java.io.IOException;

public class NoHIDAvailableException extends IOException {
    public NoHIDAvailableException() {
        super("There are no input devices connected to the system");
    }
}
