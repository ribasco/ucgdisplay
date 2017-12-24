package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.DisplayParent;
import com.ibasco.pidisplay.core.beans.ObservableList;
import com.ibasco.pidisplay.core.beans.ObservableListWrapper;
import com.ibasco.pidisplay.core.ui.Graphics;

import java.util.HashSet;
import java.util.Set;

abstract public class List<T extends Graphics, X> extends DisplayParent<T> {

    private ObservableList<X> items = new ObservableListWrapper<>();

    private Set<Integer> selectedIndices = new HashSet<>();

    public List(Integer width, Integer height) {
        this(width, height, null);
    }

    public List(Integer width, Integer height, java.util.List<X> items) {
        super(width, height);
        if (items != null)
            this.items.addAll(items);
    }

    public void select(int index) {
        selectedIndices.add(index);
    }

    public void select(Object item) {

    }

    public void selectAll() {
        for (int i = 0; i < getItems().size(); i++) {
            selectedIndices.add(i);
        }
    }

    public ObservableList<X> getSelectedItems() {
        return getItems();
    }

    public void setItems(java.util.List<X> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public ObservableList<X> getItems() {
        return items;
    }
}
