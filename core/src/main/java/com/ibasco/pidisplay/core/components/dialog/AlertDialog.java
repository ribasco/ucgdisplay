package com.ibasco.pidisplay.core.components.dialog;

import com.ibasco.pidisplay.core.Graphics;
import com.ibasco.pidisplay.core.components.DisplayDialog;
import com.ibasco.pidisplay.core.components.DisplayText;
import com.ibasco.pidisplay.core.enums.AlertType;

abstract public class AlertDialog<T extends Graphics, B> extends DisplayDialog<T, B> {

    private AlertType alertType;

    protected DisplayText<T> title;

    protected DisplayText<T> content;

    public AlertDialog(AlertType alertType) {
        this.alertType = alertType;
    }
}
