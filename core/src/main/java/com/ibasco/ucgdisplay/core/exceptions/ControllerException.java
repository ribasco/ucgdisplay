package com.ibasco.ucgdisplay.core.exceptions;

import com.ibasco.ucgdisplay.core.Controller;

public class ControllerException extends RuntimeException {

    private final Controller controller;

    public ControllerException(String message, Controller controller) {
        this(message, null, controller);
    }

    public ControllerException(String message, Throwable cause, Controller controller) {
        super(message, cause);
        this.controller = controller;
    }

    public final Controller getController() {
        return controller;
    }
}
