package com.ibasco.pidisplay.core.beans;

public interface Property<T> {
    void set(T value);

    T get();

    default boolean isSet() {
        return get() != null;
    }
}
