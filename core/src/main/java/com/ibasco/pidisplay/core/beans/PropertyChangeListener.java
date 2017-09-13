package com.ibasco.pidisplay.core.beans;

@FunctionalInterface
public interface PropertyChangeListener<T> {
    void changed(ObservableProperty<T> observable, T oldValue, T newValue);
}
