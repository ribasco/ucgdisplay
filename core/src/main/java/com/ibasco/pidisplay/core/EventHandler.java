package com.ibasco.pidisplay.core;

import java.util.EventListener;

@FunctionalInterface
public interface EventHandler<T extends Event> extends EventListener {
    void handle(T event);
}