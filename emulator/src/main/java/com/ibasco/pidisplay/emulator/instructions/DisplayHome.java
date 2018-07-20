package com.ibasco.pidisplay.emulator.instructions;

import com.ibasco.pidisplay.emulator.GlcdInstruction;

public class DisplayHome extends GlcdInstruction {
    public DisplayHome(byte value) {
        super(0x2, "Home", value);
    }
}
