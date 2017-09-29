package com.ibasco.pidisplay.core.beans;

import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ObservableProperty<T> extends PropertyBase<T> implements Observable<T> {

    private static final Logger log = LoggerFactory.getLogger(ObservableProperty.class);

    private boolean valid = true;

    private Set<PropertyChangeListener<? super T>> listeners = new HashSet<>();

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

    public T getInvalid() {
        return super.get();
    }

    private void invalidate(T oldVal) {
        if (valid) {
            valid = false;
            fireChangeEvent(oldVal);
        }
    }

    public boolean isValid() {
        return valid;
    }

    private void fireChangeEvent(T oldVal) {
        for (PropertyChangeListener<? super T> listener : listeners) {
            listener.changed(this, oldVal, super.get());
        }
    }

    @Override
    public void addListener(PropertyChangeListener<? super T> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener<? super T> listener) {
        listeners.remove(listener);
    }

    @Override
    public Set<PropertyChangeListener<? super T>> getListeners() {
        return listeners;
    }

    @Override
    public String toString() {
        TypeToken<T> type = new TypeToken<T>(getClass()) {
        };
        return String.format("ObservableProperty (%s) = (%s)", type.getType().getTypeName(), this.getInvalid());
    }
}
