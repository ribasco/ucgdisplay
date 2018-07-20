package com.ibasco.pidisplay.emulator.instructions;

import com.ibasco.pidisplay.emulator.GlcdInstruction;

public class DisplayClear extends GlcdInstruction {
    public DisplayClear(byte value) {
        super(0x1, "Display Clear", value);
    }
}
