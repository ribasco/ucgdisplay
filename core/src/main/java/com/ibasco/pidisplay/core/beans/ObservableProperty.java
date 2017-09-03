package com.ibasco.pidisplay.core.beans;

import com.ibasco.pidisplay.core.events.EventHandler;
import com.ibasco.pidisplay.core.events.ValueChangeEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ibasco.pidisplay.core.events.EventDispatcher.*;

public class ObservableProperty<T> extends PropertyBase<T> implements Observable<EventHandler<? super ValueChangeEvent>> {

    private static final Logger log = LoggerFactory.getLogger(ObservableProperty.class);

    private boolean valid = true;

    public ObservableProperty() {
        this(null);
    }

    public ObservableProperty(T value) {
        super(value);
    }

    @Override
    public void set(T newValue) {
        if (super.get() != newValue) {
            T oldVal = super.get();
            super.set(newValue);
            invalidate(oldVal);
        }
    }

    @Override
    public T get() {
        valid = true;
        return super.get();
    }

    public T getDefault(T defaultValue) {
        return ObjectUtils.defaultIfNull(super.get(), defaultValue);
    }

    public boolean isSet() {
        return super.get() != null;
    }

    private void invalidate(T oldVal) {
        if (valid) {
            valid = false;
            fireChangeEvent(oldVal);
        }
    }

    private void fireChangeEvent(T oldVal) {
        dispatch(new ValueChangeEvent<>(ValueChangeEvent.VALUE_CHANGED_EVENT, oldVal, super.get()));
    }

    @Override
    public void addListener(EventHandler<? super ValueChangeEvent> listener) {
        log.debug("Adding change listener");
        addHandler(ValueChangeEvent.VALUE_CHANGED_EVENT, listener);
    }

    @Override
    public void removeListener(EventHandler<? super ValueChangeEvent> listener) {
        removeHandler(ValueChangeEvent.VALUE_CHANGED_EVENT, listener);
    }
}
