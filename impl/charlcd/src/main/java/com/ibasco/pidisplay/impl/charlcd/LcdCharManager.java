package com.ibasco.pidisplay.impl.charlcd;

import com.ibasco.pidisplay.core.drivers.CharDisplayDriver;
import com.ibasco.pidisplay.core.ui.CharManager;

class LcdCharManager extends CharManager {
    LcdCharManager(CharDisplayDriver driver) {
        super(driver);
    }

    @Override
    public byte[] processText() {
        return new byte[0];
    }
}
