package com.ibasco.pidisplay.core.beans;

public interface Observable<T> {
    void removeListener(T listener);

    void addListener(T listener);
}
