package com.ibasco.pidisplay.components;

@FunctionalInterface
public interface RotaryEncoderListener {
    void change(long encoderValue, RotaryState state);
}
