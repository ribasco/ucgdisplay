package com.ibasco.pidisplay.impl.lcd.hitachi.components.dialog;

import com.ibasco.pidisplay.core.components.dialog.AlertDialog;
import com.ibasco.pidisplay.core.enums.AlertType;
import com.ibasco.pidisplay.impl.lcd.hitachi.CharGraphics;
import com.ibasco.pidisplay.impl.lcd.hitachi.components.LcdText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcdAlertDialog<B> extends AlertDialog<CharGraphics, B> {

    private static final Logger log = LoggerFactory.getLogger(LcdAlertDialog.class);

    private LcdText title = new LcdText(0, 0, "Title");

    private LcdText content = new LcdText(0, 1, "Content");

    public LcdAlertDialog(AlertType alertType) {
        super(alertType);
        add(title, content);
    }

    @Override
    protected void drawNode(CharGraphics graphics) {
        log.debug("Drawing Dialog");
    }
}
