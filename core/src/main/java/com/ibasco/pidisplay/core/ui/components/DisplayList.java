package com.ibasco.pidisplay.core.ui.components;

import com.ibasco.pidisplay.core.DisplayParent;
import com.ibasco.pidisplay.core.beans.ObservableList;
import com.ibasco.pidisplay.core.beans.ObservableListWrapper;
import com.ibasco.pidisplay.core.ui.Graphics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract public class DisplayList<T extends Graphics, X> extends DisplayParent<T> {

    private ObservableList<X> items = new ObservableListWrapper<>();

    private Set<Integer> selectedIndices = new HashSet<>();

    public DisplayList(Integer width, Integer height) {
        this(width, height, null);
    }

    public DisplayList(Integer width, Integer height, List<X> items) {
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

    public void selectFirst() {
    }

    public void selectLast() {

    }

    public void selectNext() {

    }

    public void selectPrevious() {

    }

    public void selectRange(int start, int end) {

    }

    public ObservableList<X> getSelectedItems() {
        return getItems();
    }

    public void setItems(List<X> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public ObservableList<X> getItems() {
        return items;
    }
}
