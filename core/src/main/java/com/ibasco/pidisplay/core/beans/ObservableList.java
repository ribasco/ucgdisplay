package com.ibasco.pidisplay.core.beans;

import java.util.List;

public interface ObservableList<E> extends List<E> {

    void addListener(ListChangeListener<? super E> changeListener);

    void removeListener(ListChangeListener<? super E> changeListener);
}
