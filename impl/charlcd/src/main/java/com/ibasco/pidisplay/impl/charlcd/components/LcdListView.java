package com.ibasco.pidisplay.impl.charlcd.components;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.ibasco.pidisplay.core.beans.ObservableList;
import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.ui.CharData;
import com.ibasco.pidisplay.core.ui.CharGraphics;
import com.ibasco.pidisplay.core.ui.Chars;
import com.ibasco.pidisplay.core.ui.components.ListView;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LcdListView<X> extends ListView<CharGraphics, X> {

    private static final Logger log = LoggerFactory.getLogger(LcdListView.class);

    public LcdListView() {
        this(null);
    }

    public LcdListView(List<X> items) {
        super(items);
    }

    private ObservableProperty<CharData> itemIcon = createProperty(Chars.BULLET_POINT_OPEN);

    private ObservableProperty<CharData> itemIconFocused = createProperty(Chars.BULLET_POINT_FOCUSED);

    private ObservableProperty<CharData> itemIconSelected = createProperty(Chars.BULLET_POINT_SELECTED);

    public CharData getItemIcon() {
        return itemIcon.get();
    }

    public void setItemIcon(CharData itemIcon) {
        this.itemIcon.set(itemIcon);
    }

    public CharData getItemIconSelected() {
        return itemIconSelected.get();
    }

    public void setItemIconSelected(CharData itemIconSelected) {
        this.itemIconSelected.set(itemIconSelected);
    }

    public CharData getItemIconFocused() {
        return itemIconFocused.get();
    }

    public void setItemIconFocused(CharData itemIconFocused) {
        this.itemIconFocused.set(itemIconFocused);
    }

    @Override
    protected void initialize(CharGraphics graphics) {
        log.info("Initialize {}", this);
        graphics.charManager().registerChar(Chars.LEFT_ARROW);
        graphics.charManager().registerChar(Chars.RIGHT_ARROW);
        graphics.charManager().registerChar(Chars.UP_ARROW);
        graphics.charManager().registerChar(Chars.DOWN_ARROW);
        graphics.charManager().registerChar(Chars.BULLET_POINT);
        graphics.charManager().registerChar(Chars.BULLET_POINT_OPEN);
        graphics.charManager().registerChar(Chars.BULLET_POINT_FOCUSED);
        graphics.charManager().registerChar(Chars.BULLET_POINT_SELECTED);
    }

    @Override
    protected void drawNode(CharGraphics graphics) {
        if (getItems() == null || getItems().size() == 0)
            return;

        //Initialize default dimensions
        int width = super.width.getDefault(graphics.getWidth());
        int height = super.height.getDefault(graphics.getHeight());
        int top = super.topPos.getDefault(0);
        int left = super.leftPos.getDefault(0);

        int maxHeight = graphics.getHeight();
        int maxWidth = graphics.getWidth();

        RangeMap<Integer, List<X>> rangeMap = createPageRangeMap(height);
        Map.Entry<Range<Integer>, List<X>> entry = rangeMap.getEntry(getFocusedIndex());

        if (entry != null) {
            List<X> pageEntries = entry.getValue();
            int index = getFocusedIndex() - entry.getKey().lowerEndpoint();
            //graphics.clear();
            for (int i = 0; i < pageEntries.size(); i++) {
                int x = left;
                int y = i + top;
                graphics.clearLine(y);
                graphics.setCursor(x, y);
                drawListItem(pageEntries.get(i), graphics, index == i, getSelectedIndices().contains(getFocusedIndex()));
            }
        }
    }

    private void drawListItem(X item, CharGraphics graphics, boolean focused, boolean selected) {
        String text = StringUtils.left(Objects.toString(item, ""), graphics.getWidth());
        if (focused) {
            graphics.drawChar(itemIconFocused.get());
        } else if (selected) {
            graphics.drawChar(itemIconSelected.get());
        } else {
            graphics.drawChar(itemIcon.get());
        }
        graphics.drawText(" ");
        graphics.drawText(text);
    }

    private RangeMap<Integer, List<X>> createPageRangeMap(int pageLimit) {
        RangeMap<Integer, List<X>> rangeMap = TreeRangeMap.create();
        int maxEntries = getItems().size();
        ObservableList<X> items = getItems();
        //noinspection Duplicates
        for (int startIdx = 0; startIdx < maxEntries; startIdx += pageLimit - 1) {
            int endIdx = startIdx + (pageLimit - 1);
            if (endIdx >= maxEntries)
                endIdx = (startIdx + (maxEntries - startIdx)) - 1;
            rangeMap.put(Range.closed(startIdx, endIdx), items.subList(startIdx, endIdx + 1));
            startIdx++;
        }
        return rangeMap;
    }

    public void doPrevious() {
        if ((getFocusedIndex() - 1) < 0) {
            setFocusedIndex(0);
        } else {
            setFocusedIndex(getFocusedIndex() - 1);
        }
    }

    public void doNext() {
        if ((getFocusedIndex() + 1) > (getItems().size() - 1)) {
            setFocusedIndex(getItems().size() - 1);
        } else {
            setFocusedIndex(getFocusedIndex() + 1);
        }
    }
}
