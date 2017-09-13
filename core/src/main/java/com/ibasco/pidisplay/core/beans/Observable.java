package com.ibasco.pidisplay.core.beans;

public interface Observable<T> {
    void removeListener(PropertyChangeListener<T> listener);

    void addListener(PropertyChangeListener<T> listener);
}
