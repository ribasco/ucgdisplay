package com.ibasco.pidisplay.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class EventDispatchChainImpl implements EventDispatchChain {
    public static final Logger log = LoggerFactory.getLogger(EventDispatchChainImpl.class);

    private Deque<EventDispatcher> dispatchers = new ArrayDeque<>();

    @Override
    public EventDispatchChain addLast(EventDispatcher eventDispatcher) {
        dispatchers.addLast(eventDispatcher);
        return this;
    }

    @Override
    public EventDispatchChain addFirst(EventDispatcher eventDispatcher) {
        dispatchers.addFirst(eventDispatcher);
        return this;
    }

    @Override
    public Event dispatchEvent(Event event) {
        if (dispatchers.isEmpty())
            return event;
        return dispatchers.removeFirst().dispatchEvent(event, this);
    }

    public void reset() {
        //Re-initialize queue?
        //this.dispatchers.clear();
        this.dispatchers = new ArrayDeque<>();
    }

    @Override
    public String toString() {
        Iterator<EventDispatcher> it = dispatchers.iterator();
        StringBuilder sb = new StringBuilder();
        int ctr = 1;
        while (it.hasNext()) {
            EventDispatcher dispatcher = it.next();
            sb.append("(");
            sb.append(ctr++);
            sb.append(") ");
            sb.append(dispatcher.toString());
            if (ctr <= dispatchers.size())
                sb.append(" -> ");
        }
        return sb.toString();
    }
}
