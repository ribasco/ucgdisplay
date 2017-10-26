package com.ibasco.pidisplay.core;

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
        if (eventHandler != null) {
            eventHandler.handle(event);
        }
    }
}
