package com.ibasco.pidisplay.core.beans;

abstract public class PropertyBase<T> implements Property<T> {

    private T value;

    public PropertyBase(T value) {
        this.value = value;
    }

    @Override
    public void set(T newValue) {
        value = newValue;
    }

    @Override
    public T get() {
        return this.value;
    }
}
