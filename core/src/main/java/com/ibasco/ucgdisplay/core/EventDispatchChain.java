package com.ibasco.ucgdisplay.core;

public interface EventDispatchChain {
    EventDispatchChain addLast(EventDispatcher eventDispatcher);

    EventDispatchChain addFirst(EventDispatcher eventDispatcher);

    Event dispatchEvent(Event event);
}
