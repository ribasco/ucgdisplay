package com.ibasco.pidisplay.core.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ObservableProperty<T> extends PropertyBase<T> implements Observable<T> {

    private static final Logger log = LoggerFactory.getLogger(ObservableProperty.class);

    private boolean valid = true;

    private Set<PropertyChangeListener<T>> listeners = new HashSet<>();

    public ObservableProperty() {
        this(null);
    }

    public ObservableProperty(T value) {
        super(value);
    }

    @Override
    public void set(T newValue) {
        set(newValue, super.get() != newValue);
    }

    public void setValid(T newValue) {
        set(newValue, false);
    }

    public void setInvalid(T newValue) {
        set(newValue, true);
    }

    private void set(T newValue, boolean invalidate) {
        T oldVal = super.get();
        super.set(newValue);
        if (invalidate)
            invalidate(oldVal);
    }

    @Override
    public T get() {
        valid = true;
        return super.get();
    }

    private void invalidate(T oldVal) {
        if (valid) {
            valid = false;
            fireChangeEvent(oldVal);
        }
    }

    private void fireChangeEvent(T oldVal) {
        listeners.forEach(l -> l.changed(this, oldVal, super.get()));
    }

    @Override
    public void addListener(PropertyChangeListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener<T> listener) {
        listeners.remove(listener);
    }

    public Set<PropertyChangeListener<T>> getListeners() {
        return listeners;
    }
}
