package com.ibasco.pidisplay.impl.lcd.hitachi.components;

import com.ibasco.pidisplay.core.components.DisplayScreen;
import com.ibasco.pidisplay.impl.lcd.hitachi.LcdGraphics;

public class LcdScreen extends DisplayScreen<LcdGraphics> {
    public LcdScreen() {
        super();
    }

    public LcdScreen(LcdGroup primary) {
        super(primary);
    }
}
