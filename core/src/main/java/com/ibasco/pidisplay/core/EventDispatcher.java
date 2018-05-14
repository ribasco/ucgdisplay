package com.ibasco.pidisplay.core;

public interface EventDispatcher {
    Event dispatchEvent(Event event, EventDispatchChain tail);
}
