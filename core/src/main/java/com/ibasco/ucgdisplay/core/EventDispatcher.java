package com.ibasco.ucgdisplay.core;

public interface EventDispatcher {
    Event dispatchEvent(Event event, EventDispatchChain tail);
}
