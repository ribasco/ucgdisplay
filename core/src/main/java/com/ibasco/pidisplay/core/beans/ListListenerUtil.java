package com.ibasco.pidisplay.core.beans;

import java.util.HashSet;
import java.util.Set;

abstract public class ListListenerUtil<E> {

    public static <E> ListListenerUtil<E> addListener(ListListenerUtil<E> helper, ListChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null)
            return new SimpleListListener<>(listener);
        return helper.addListener(listener);
    }

    public static <E> ListListenerUtil<E> removeListener(ListListenerUtil<E> helper, ListChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null)
            return null;
        return helper.removeListener(listener);
    }

    public static <E> void fireChangeEvent(ListListenerUtil<E> helper, ListChangeListener.Change<? extends E> change) {
        if (helper != null) {
            helper.fireChangeEvent(change);
        }
    }

    abstract protected ListListenerUtil<E> addListener(ListChangeListener<? super E> listener);

    abstract protected ListListenerUtil<E> removeListener(ListChangeListener<? super E> listener);

    abstract protected void fireChangeEvent(ListChangeListener.Change<? extends E> change);

    private static final class SimpleListListener<E> extends ListListenerUtil<E> {
        private Set<ListChangeListener<? super E>> listeners = new HashSet<>();

        public SimpleListListener(ListChangeListener<? super E> listener) {
            addListener(listener);
        }

        @Override
        protected ListListenerUtil<E> addListener(ListChangeListener<? super E> listener) {
            listeners.add(listener);
            return this;
        }

        @Override
        protected ListListenerUtil<E> removeListener(ListChangeListener<? super E> listener) {
            listeners.remove(listener);
            return this;
        }

        @Override
        protected void fireChangeEvent(ListChangeListener.Change<? extends E> change) {
            listeners.forEach(l -> l.onChange(change));
        }
    }
}
