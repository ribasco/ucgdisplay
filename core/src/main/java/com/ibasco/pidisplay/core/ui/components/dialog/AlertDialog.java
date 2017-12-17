package com.ibasco.pidisplay.core.ui.components.dialog;

import com.ibasco.pidisplay.core.enums.AlertType;
import com.ibasco.pidisplay.core.ui.Graphics;
import com.ibasco.pidisplay.core.ui.components.DisplayDialog;
import com.ibasco.pidisplay.core.ui.components.DisplayText;

abstract public class AlertDialog<T extends Graphics, B> extends DisplayDialog<T, B> {

    private AlertType alertType;

    protected DisplayText<T> title;

    protected DisplayText<T> content;

    public AlertDialog(AlertType alertType) {
        this.alertType = alertType;
    }
}
