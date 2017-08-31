package com.ibasco.pidisplay.core.beans;

@FunctionalInterface
public interface PropertyChangeListener<T> {
    void propertyChanged(ObservableProperty<T> observable, T oldValue, T newValue);
}
