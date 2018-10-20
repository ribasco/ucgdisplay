package com.ibasco.ucgdisplay.components;

@FunctionalInterface
public interface RotaryEncoderListener {
    void change(long encoderValue, RotaryState state);
}
