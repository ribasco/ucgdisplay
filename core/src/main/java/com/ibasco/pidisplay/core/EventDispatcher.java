package com.ibasco.pidisplay.core;

/**
 * The {@link EventDispatcher} interface
 *
 * @author Rafael Ibasco
 */
public interface EventDispatcher {
    <T extends Event> void dispatch(T event);

    void invokeLater(Runnable runnable);

    void shutdown();

    boolean isEventDispatchThread();
}
