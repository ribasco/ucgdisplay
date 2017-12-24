package com.ibasco.pidisplay.core.ui.components.dialog;

import com.ibasco.pidisplay.core.enums.AlertType;
import com.ibasco.pidisplay.core.ui.Graphics;
import com.ibasco.pidisplay.core.ui.components.Dialog;
import com.ibasco.pidisplay.core.ui.components.Text;

abstract public class AlertDialog<T extends Graphics, B> extends Dialog<T, B> {

    private AlertType alertType;

    protected Text<T> title;

    protected Text<T> content;

    public AlertDialog(AlertType alertType) {
        this.alertType = alertType;
    }
}
