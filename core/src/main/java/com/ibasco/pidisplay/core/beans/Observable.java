package com.ibasco.pidisplay.core.beans;

//TODO: Make obsolete
@Deprecated
public interface Observable<T> {
    void removeListener(PropertyChangeListener<? super T> listener);

    void addListener(PropertyChangeListener<? super T> listener);
}
