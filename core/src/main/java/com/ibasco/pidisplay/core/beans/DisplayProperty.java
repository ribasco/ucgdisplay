package com.ibasco.pidisplay.core.beans;

public class DisplayProperty<T> extends ObservableProperty<T> {

    public DisplayProperty() {
        super(null);
    }

    public DisplayProperty(T initialValue) {
        super(initialValue);
    }
}
