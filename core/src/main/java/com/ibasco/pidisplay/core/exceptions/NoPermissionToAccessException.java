package com.ibasco.pidisplay.core.exceptions;

import java.io.File;
import java.io.IOException;

public class NoPermissionToAccessException extends IOException {
    public NoPermissionToAccessException(File resource) {
        super(String.format("You do not have permission to access '%s'", resource.getPath()));
    }
}
