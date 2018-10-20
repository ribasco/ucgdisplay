package com.ibasco.ucgdisplay.core.ui.components;

import com.ibasco.ucgdisplay.core.DisplayParent;
import com.ibasco.ucgdisplay.core.beans.ObservableList;
import com.ibasco.ucgdisplay.core.beans.ObservableListWrapper;
import com.ibasco.ucgdisplay.core.beans.ObservableProperty;
import com.ibasco.ucgdisplay.core.events.ListViewItemEvent;
import com.ibasco.ucgdisplay.core.ui.Graphics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract public class ListView<T extends Graphics, X> extends DisplayParent<T> {

    private ObservableList<X> items = new ObservableListWrapper<>();

    private Set<Integer> selectedIndices = new HashSet<>();

    private ObservableProperty<Integer> focusedIndex = createProperty(true, 0, (oldValue, newValue) -> {
        if (oldValue != null)
            fireEvent(new ListViewItemEvent(ListViewItemEvent.ITEM_EXIT_FOCUS, oldValue));
        if (newValue != null)
            fireEvent(new ListViewItemEvent(ListViewItemEvent.ITEM_ENTER_FOCUS, newValue));
    });

    public ListView(java.util.List<X> items) {
        super(null, null);
        if (items != null)
            this.items.addAll(items);
    }

    public void select(int index) {
        selectedIndices.add(index);
        fireEvent(new ListViewItemEvent(ListViewItemEvent.ITEM_SELECTED, index));
    }

    public void select(Object item) {
        if (item == null)
            return;
        for (int i = 0; i < items.size(); i++) {
            if (item.equals(items.get(i)))
                selectedIndices.add(i);
        }
    }

    public void selectAll() {
        for (int i = 0; i < getItems().size(); i++) {
            selectedIndices.add(i);
        }
    }

    public void clearSelection(int index) {
        selectedIndices.remove(index);
        fireEvent(new ListViewItemEvent(ListViewItemEvent.ITEM_DESELECTED, index));
    }

    public void clearSelection() {
        selectedIndices.clear();
    }

    public List<Integer> getSelectedIndices() {
        return new ArrayList<>(selectedIndices);
    }

    public List<X> getSelectedItems() {
        final ArrayList<X> selectedItems = new ArrayList<>();
        selectedIndices.forEach(idx -> selectedItems.add(items.get(idx)));
        return selectedItems;
    }

    public void setItems(java.util.List<X> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public ObservableList<X> getItems() {
        return items;
    }

    public int getFocusedIndex() {
        return focusedIndex.get();
    }

    public void setFocusedIndex(int focusedIndex) {
        this.focusedIndex.set(focusedIndex);
    }
}
