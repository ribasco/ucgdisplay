package com.ibasco.pidisplay.emulator.instructions;

import com.ibasco.pidisplay.emulator.GlcdInstruction;

public class FunctionSet extends GlcdInstruction {
    public FunctionSet(byte value) {
        super(0x20, "Function Set", value);
    }
}
