package com.ibasco.pidisplay.core;

@FunctionalInterface
public interface EventHandler<E extends Event> {
    void handle(E event);
}
