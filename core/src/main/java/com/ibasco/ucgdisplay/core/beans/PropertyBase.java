package com.ibasco.ucgdisplay.core.beans;

abstract public class PropertyBase<T> implements Property<T> {

    private T value = null;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyBase<?> that = (PropertyBase<?>) o;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
