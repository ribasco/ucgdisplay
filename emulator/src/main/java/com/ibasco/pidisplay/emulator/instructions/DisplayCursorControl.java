package com.ibasco.pidisplay.emulator.instructions;

import com.ibasco.pidisplay.emulator.GlcdInstruction;

public class DisplayCursorControl extends GlcdInstruction {
    public DisplayCursorControl(byte value) {
        super(0x10, "Display/Cursor Control", value);
    }
}
