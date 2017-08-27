package com.ibasco.pidisplay.core.events;

@FunctionalInterface
public interface EventHandler<E extends Event> {
    void handle(E event);
}
