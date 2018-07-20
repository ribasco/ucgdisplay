package com.ibasco.pidisplay.emulator.instructions;

import com.ibasco.pidisplay.emulator.GlcdInstruction;

public class EntryModeSet extends GlcdInstruction {
    public EntryModeSet(byte value) {
        super(0x4, "Entry Mode Set", value);
    }
}
