package com.ibasco.pidisplay.core;

@FunctionalInterface
public interface Display<T extends Graphics> {
    void draw(T graphics);

    default void redraw() {
    }
}
