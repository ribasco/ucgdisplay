package com.ibasco.pidisplay.core.beans;

import org.apache.commons.lang3.ObjectUtils;

public interface Property<T> {
    void set(T value);

    T get();

    default T get(T defaultValue) {
        return ObjectUtils.defaultIfNull(get(), defaultValue);
    }

    default boolean isSet() {
        return get() != null;
    }
}
