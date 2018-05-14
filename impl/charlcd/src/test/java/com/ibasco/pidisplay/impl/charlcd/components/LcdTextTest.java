package com.ibasco.pidisplay.impl.charlcd.components;

import org.junit.jupiter.api.Test;

class LcdTextTest {

    private LcdText textDisplay = new LcdText();

    @Test
    void testDraw() {
        textDisplay.setText("");
    }
}