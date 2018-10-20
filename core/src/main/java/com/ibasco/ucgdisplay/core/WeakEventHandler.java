package com.ibasco.ucgdisplay.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;

/**
 * A weak-referenced {@link EventHandler}.
 *
 * @param <T>
 *         {@link Event} class
 *
 * @author Rafael Ibasco
 */
public final class WeakEventHandler<T extends Event> implements EventHandler<T> {

    public static final Logger log = LoggerFactory.getLogger(WeakEventHandler.class);

    private final WeakReference<EventHandler<T>> ref;

    public WeakEventHandler(final EventHandler<T> eventHandler) {
        ref = new WeakReference<>(eventHandler);
    }

    public boolean isRemoved() {
        return ref.get() == null;
    }

    @Override
    public void handle(final T event) {
        final EventHandler<T> eventHandler = ref.get();
        if (eventHandler == null)
            log.debug("NULL EVENT HANDLER");
        if (eventHandler != null) {
            eventHandler.handle(event);
        }
    }
}
