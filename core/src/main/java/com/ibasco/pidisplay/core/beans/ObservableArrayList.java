package com.ibasco.pidisplay.core.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class ObservableArrayList<T> extends ArrayList<T> implements Observable<T> {

    @Override
    public boolean add(T t) {
        return super.add(t);
    }

    @Override
    public void add(int index, T element) {
        super.add(index, element);
    }

    @Override
    public T remove(int index) {
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return super.addAll(index, c);
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    @Override
    public void removeListener(PropertyChangeListener<? super T> listener) {

    }

    @Override
    public void addListener(PropertyChangeListener<? super T> listener) {

    }

    @Override
    public Set<PropertyChangeListener<? super T>> getListeners() {
        return null;
    }
}
