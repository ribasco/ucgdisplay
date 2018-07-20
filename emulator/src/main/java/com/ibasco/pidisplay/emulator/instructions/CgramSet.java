package com.ibasco.pidisplay.emulator.instructions;

import com.ibasco.pidisplay.emulator.GlcdInstruction;

public class CgramSet extends GlcdInstruction {
    public CgramSet(byte value) {
        super(0x40, "CGRAM Set", value);
    }
}
