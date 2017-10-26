package com.ibasco.pidisplay.core.beans;

import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ObservableProperty<T> extends PropertyBase<T> implements Observable<T> {

    private static final Logger log = LoggerFactory.getLogger(ObservableProperty.class);

    private boolean valid = true;

    private PropertyListenerUtil<T> listenerUtil;

    public ObservableProperty() {
        this(null);
    }

    public ObservableProperty(T value) {
        super(value);
    }

    @Override
    public void set(T newValue) {
        set(newValue, !Objects.equals(super.get(), newValue));
    }

    public void setValid(T newValue) {
        set(newValue, false);
    }

    private void set(T newValue, boolean invalidate) {
        T oldVal = super.get();
        super.set(newValue);
        if (invalidate)
            invalidate(oldVal);
    }

    @Override
    public T get() {
        return super.get();
    }

    private void invalidate(T oldVal) {
        log.debug("INVALIDATED_PROPERTY => old: {}, new: {}", oldVal, super.get());
        invalidated(oldVal, super.get());
        fireChangeEvent(oldVal);
    }

    protected void invalidated(T oldValue, T newValue) {
        //to be overridden
    }

    public boolean isValid() {
        return valid;
    }

    private void fireChangeEvent(T oldVal) {
        PropertyListenerUtil.fireChangeEvent(listenerUtil, this, oldVal, super.get());
    }

    @Override
    public void addListener(PropertyChangeListener<? super T> listener) {
        this.listenerUtil = PropertyListenerUtil.addListener(this.listenerUtil, listener);
    }

    @Override
    public void removeListener(PropertyChangeListener<? super T> listener) {
        this.listenerUtil = PropertyListenerUtil.removeListener(this.listenerUtil, listener);
    }

    @Override
    public String toString() {
        TypeToken<T> type = new TypeToken<T>(getClass()) {
        };
        return String.format("ObservableProperty (%s) = (%s)", type.getType().getTypeName(), get());
    }
}
