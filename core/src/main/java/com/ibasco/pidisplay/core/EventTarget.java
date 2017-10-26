package com.ibasco.pidisplay.core;

public interface EventTarget {
    EventDispatchType CAPTURE = EventDispatchType.CAPTURE;
    EventDispatchType BUBBLE = EventDispatchType.BUBBLE;

    EventDispatchChain buildEventTargetPath(EventDispatchChain tail);

    EventDispatchQueue getEventDispatchQueue();
}
