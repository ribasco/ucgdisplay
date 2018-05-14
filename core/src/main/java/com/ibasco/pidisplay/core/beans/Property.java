package com.ibasco.pidisplay.core.beans;

public interface Property<T> {
    void set(T value);

    T get();

    default T getDefault(T defaultValue) {
        return get() == null ? defaultValue : get();
    }

    default boolean isSet() {
        return get() != null;
    }
}
