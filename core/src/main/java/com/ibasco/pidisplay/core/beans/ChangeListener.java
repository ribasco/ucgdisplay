package com.ibasco.pidisplay.core.beans;

@FunctionalInterface
public interface ChangeListener<T> {
    void changed(T oldValue, T newValue);
}
