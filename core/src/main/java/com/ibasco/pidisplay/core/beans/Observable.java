package com.ibasco.pidisplay.core.beans;

import java.util.Set;

//TODO: Make obsolete
public interface Observable<T> {
    void removeListener(PropertyChangeListener<? super T> listener);

    void addListener(PropertyChangeListener<? super T> listener);

    Set<PropertyChangeListener<? super T>> getListeners();
}
