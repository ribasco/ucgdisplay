package com.ibasco.pidisplay.core.beans;

public interface Observable<T> {
    void removeListener(ChangeListener<T> listener);

    void addListener(ChangeListener<T> listener);
}
