package com.ibasco.pidisplay.emulator.instructions;

import com.ibasco.pidisplay.emulator.GlcdInstruction;

public class DisplayControl extends GlcdInstruction {
    public DisplayControl(byte value) {
        super(0x8, "Display Control", value);
    }
}
