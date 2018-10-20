package com.ibasco.ucgdisplay.core.beans;

@FunctionalInterface
public interface PropertyChangeListener<T> {
    void changed(ObservableProperty<? extends T> observable, T oldValue, T newValue);
}
