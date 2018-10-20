package com.ibasco.ucgdisplay.core.ui.components.dialog;

import com.ibasco.ucgdisplay.core.enums.AlertType;
import com.ibasco.ucgdisplay.core.ui.Graphics;
import com.ibasco.ucgdisplay.core.ui.components.Dialog;
import com.ibasco.ucgdisplay.core.ui.components.Text;

abstract public class AlertDialog<T extends Graphics, B> extends Dialog<T, B> {

    private AlertType alertType;

    protected Text<T> title;

    protected Text<T> content;

    public AlertDialog(AlertType alertType) {
        this.alertType = alertType;
    }
}
