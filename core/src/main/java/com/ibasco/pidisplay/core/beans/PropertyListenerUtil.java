package com.ibasco.pidisplay.core.beans;

import java.util.HashSet;
import java.util.Set;

abstract public class PropertyListenerUtil<E> {
    public static <E> PropertyListenerUtil<E> addListener(PropertyListenerUtil<E> helper, PropertyChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null)
            return new SimplePropertyListener<>(listener);
        return helper.addListener(listener);
    }

    public static <E> PropertyListenerUtil<E> removeListener(PropertyListenerUtil<E> helper, PropertyChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null)
            return null;
        return helper.removeListener(listener);
    }

    public static <E> void fireChangeEvent(PropertyListenerUtil<E> helper, ObservableProperty<? extends E> observable, E oldValue, E newValue) {
        if (helper != null) {
            helper.fireChangeEvent(observable, oldValue, newValue);
        }
    }

    abstract protected PropertyListenerUtil<E> addListener(PropertyChangeListener<? super E> listener);

    abstract protected PropertyListenerUtil<E> removeListener(PropertyChangeListener<? super E> listener);

    abstract protected void fireChangeEvent(ObservableProperty<? extends E> observable, E oldValue, E newValue);

    private static final class SimplePropertyListener<E> extends PropertyListenerUtil<E> {

        private Set<PropertyChangeListener<? super E>> listeners = new HashSet<>();

        public SimplePropertyListener(PropertyChangeListener<? super E> listener) {
            addListener(listener);
        }

        @Override
        protected PropertyListenerUtil<E> addListener(PropertyChangeListener<? super E> listener) {
            listeners.add(listener);
            return this;
        }

        @Override
        protected PropertyListenerUtil<E> removeListener(PropertyChangeListener<? super E> listener) {
            listeners.remove(listener);
            return this;
        }

        @Override
        protected void fireChangeEvent(ObservableProperty<? extends E> observable, E oldValue, E newValue) {
            listeners.forEach(l -> l.changed(observable, oldValue, newValue));
        }

    }
}
