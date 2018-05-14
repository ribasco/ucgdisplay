package com.ibasco.pidisplay.core.beans;

import java.util.ArrayList;
import java.util.List;

public class ObservableListWrapper<T> extends BaseObservableList<T> {
    public ObservableListWrapper() {
        this(new ArrayList<>());
    }

    public ObservableListWrapper(List<T> backingList) {
        super(backingList);
    }

    @Override
    public void fireChangeEvent(ListChangeListener.Change<? extends T> change) {
        invalidatedList(change);
        super.fireChangeEvent(change);
    }

    protected void invalidatedList(ListChangeListener.Change<? extends T> changeDetails) {
        //to be overriden
    }
}
