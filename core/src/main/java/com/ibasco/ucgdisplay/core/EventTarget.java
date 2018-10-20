package com.ibasco.ucgdisplay.core;

public interface EventTarget {
    EventDispatchPhase CAPTURE = EventDispatchPhase.CAPTURE;
    EventDispatchPhase BUBBLE = EventDispatchPhase.BUBBLE;
    EventDispatchPhase POST_DISPATCH = EventDispatchPhase.POST_DISPATCH;

    EventDispatchChain buildEventTargetPath(EventDispatchChain tail);

    EventDispatchQueue getEventDispatchQueue();
}
