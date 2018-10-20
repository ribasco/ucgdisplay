package com.ibasco.ucgdisplay.core.beans;

import java.util.*;
import java.util.function.Consumer;

abstract public class BaseObservableList<T> implements ObservableList<T>, Iterable<T> {
    private ListListenerUtil<T> listListenerUtil;

    protected List<T> backingList;

    public BaseObservableList(List<T> backingList) {
        this.backingList = Objects.requireNonNull(backingList, "Backing list cannot be null");
    }

    @Override
    public void addListener(ListChangeListener<? super T> changeListener) {
        this.listListenerUtil = ListListenerUtil.addListener(listListenerUtil, changeListener);
    }

    @Override
    public void removeListener(ListChangeListener<? super T> changeListener) {
        this.listListenerUtil = ListListenerUtil.removeListener(listListenerUtil, changeListener);
    }

    @Override
    public int size() {
        return backingList.size();
    }

    @Override
    public boolean isEmpty() {
        return backingList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return backingList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return backingList.iterator();
    }

    @Override
    public Object[] toArray() {
        return backingList.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return backingList.toArray(a);
    }

    @Override
    public boolean add(T t) {
        boolean added = backingList.add(t);
        if (added)
            fireChangeEvent(new ListChangeListener.SingleChange<>(this, backingList.size() - 1));
        return added;
    }

    protected void fireChangeEvent(ListChangeListener.Change<? extends T> change) {
        ListListenerUtil.fireChangeEvent(listListenerUtil, change);
    }

    @Override
    public boolean remove(Object o) {
        boolean removed = backingList.remove(o);
        if (removed)
            fireChangeEvent(new ListChangeListener.SingleChange<>(this, 0));
        return removed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backingList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return backingList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return backingList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return backingList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return backingList.retainAll(c);
    }

    @Override
    public void clear() {
        backingList.clear();
    }

    @Override
    public T get(int index) {
        return backingList.get(index);
    }

    @Override
    public T set(int index, T element) {
        return backingList.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        backingList.add(index, element);
    }

    @Override
    public T remove(int index) {
        return backingList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return backingList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return backingList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return backingList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return backingList.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return backingList.subList(fromIndex, toIndex);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        backingList.forEach(action);
    }
}
