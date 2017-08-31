package com.ibasco.pidisplay.core.beans;

import com.ibasco.pidisplay.core.events.EventHandler;
import com.ibasco.pidisplay.core.events.ValueChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ibasco.pidisplay.core.events.EventDispatcher.*;

abstract public class ObservableProperty<T> extends PropertyBase<T> implements Observable<EventHandler<? super ValueChangeEvent>> {

    private static final Logger log = LoggerFactory.getLogger(ObservableProperty.class);

    private boolean valid = true;

    public ObservableProperty(T value) {
        super(value);
    }

    @Override
    public void set(T newValue) {
        if (super.get() != newValue) {
            super.set(newValue);
            invalidate();
        }
    }

    @Override
    public T get() {
        valid = true;
        return super.get();
    }

    private void invalidate() {
        if (valid) {
            valid = false;
            fireChangeEvent();
        }
    }

    private void fireChangeEvent() {
        dispatch(new ValueChangeEvent<>(ValueChangeEvent.VALUE_CHANGED_EVENT, get()));
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
