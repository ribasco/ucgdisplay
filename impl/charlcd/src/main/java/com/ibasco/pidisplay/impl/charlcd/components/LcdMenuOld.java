package com.ibasco.pidisplay.impl.charlcd.components;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.ibasco.pidisplay.core.EventHandler;
import com.ibasco.pidisplay.core.enums.TextAlignment;
import com.ibasco.pidisplay.core.util.Node;
import com.ibasco.pidisplay.impl.charlcd.CharGraphics;
import com.ibasco.pidisplay.impl.charlcd.LcdDisplay;
import com.ibasco.pidisplay.impl.charlcd.enums.LcdMenuCursorOrientation;
import com.ibasco.pidisplay.impl.charlcd.enums.LcdMenuItemStyle;
import com.ibasco.pidisplay.impl.charlcd.events.LcdMenuItemEvent;
import com.ibasco.pidisplay.impl.charlcd.events.LcdMenuNavEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.*;

@SuppressWarnings("WeakerAccess")
@Deprecated
public class LcdMenuOld extends LcdDisplay {

    private static final Logger log = LoggerFactory.getLogger(LcdMenuOld.class);

    private static final String DEFAULT_NUMBER_FORMAT = "0. ";

    private static final String DEFAULT_MENU_ITEM_ABBR = "..";

    protected static final String CMD_CURSOR = "cursor";

    protected static final String CMD_PAGENUM = "pageNum";

    protected static final String CMD_PAGECOUNT = "pageCount";

    private int cursorPos = 0;

    private List<Node<String>> entries = new ArrayList<>();

    private Stack<NodePos> parentNodeStack = new Stack<>();

    //<editor-fold desc="Menu Properties">
    private int pageNumber = 1;

    private boolean headerVisible = true;

    private boolean cursorVisible = true;

    private LcdMenuCursorOrientation cursorOrientation = LcdMenuCursorOrientation.LEFT;

    private LcdMenuItemStyle itemStyle = LcdMenuItemStyle.NONE;

    private DecimalFormat menuItemNumberFormatter = new DecimalFormat(DEFAULT_NUMBER_FORMAT);

    private TextAlignment itemTextAlignment = TextAlignment.LEFT;

    private TextAlignment headerTextAlignment = TextAlignment.LEFT;

    private Byte menuCursorChar = null;
    //</editor-fold>

    //<editor-fold desc="Event Handler Properties">
    private EventHandler<? super LcdMenuNavEvent> onNavNext;

    private EventHandler<? super LcdMenuNavEvent> onNavPrevious;

    private EventHandler<? super LcdMenuNavEvent> onNavEnter;

    private EventHandler<? super LcdMenuNavEvent> onNavExit;

    private EventHandler<? super LcdMenuItemEvent> onItemFocus;

    private EventHandler<? super LcdMenuItemEvent> onItemSelected;
    //</editor-fold>

    public LcdMenuOld(Node<String> root) {
        if (!Objects.requireNonNull(root).hasChildren())
            throw new IllegalArgumentException("Node does not contain child entries");
        //Register custom lcd menu variables
        registerVar(CMD_PAGENUM, args -> String.valueOf(getPageNumber()));
        registerVar(CMD_PAGECOUNT, args -> String.valueOf(getPageCount()));
        registerVar(CMD_CURSOR, args -> String.valueOf(this.cursorPos + 1));
        refresh(root, 0);
    }

    //<editor-fold desc="Menu Getter/Setter Properties">

    public Node<String> getSelectedItem() {
        return current();
    }

    public List<Node<String>> getItems() {
        return this.entries;
    }

    public boolean isCursorVisible() {
        return cursorVisible;
    }

    public void setCursorVisible(boolean cursorVisible) {
        this.cursorVisible = cursorVisible;
    }

    public LcdMenuCursorOrientation getCursorOrientation() {
        return cursorOrientation;
    }

    public void setCursorOrientation(LcdMenuCursorOrientation cursorOrientation) {
        this.cursorOrientation = cursorOrientation;
    }

    public TextAlignment getHeaderTextAlignment() {
        return headerTextAlignment;
    }

    public void setHeaderTextAlignment(TextAlignment headerTextAlignment) {
        this.headerTextAlignment = headerTextAlignment;
    }

    public byte getMenuCursorChar() {
        return menuCursorChar;
    }

    public void setMenuCursorChar(byte menuCursorChar) {
        this.menuCursorChar = menuCursorChar;
    }

    public TextAlignment getItemTextAlignment() {
        return itemTextAlignment;
    }

    public void setItemTextAlignment(TextAlignment itemTextAlignment) {
        this.itemTextAlignment = itemTextAlignment;
    }

    public void setMenuItemNumberFormat(String pattern) {
        this.menuItemNumberFormatter.applyPattern(pattern);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageCount() {
        return getTotalPages(getHeight());
    }

    public LcdMenuItemStyle getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(LcdMenuItemStyle itemStyle) {
        this.itemStyle = itemStyle;
    }

    public boolean isHeaderVisible() {
        return headerVisible;
    }

    public void setHeaderVisible(boolean headerVisible) {
        this.headerVisible = headerVisible;
    }
    //</editor-fold>

    //<editor-fold desc="Menu Actions">
    public boolean doNext() {
        if (isValidIndex(this.cursorPos + 1)) {
            this.cursorPos++;
            redraw();
            fireEvent(new LcdMenuNavEvent(LcdMenuNavEvent.LCD_NAV_NEXT, current(), this));
            return true;
        }
        return false;
    }

    public boolean doPrevious() {
        if (isValidIndex(this.cursorPos - 1)) {
            this.cursorPos--;
            redraw();
            fireEvent(new LcdMenuNavEvent(LcdMenuNavEvent.LCD_NAV_PREVIOUS, current(), this));
            return true;
        }
        return false;
    }

    public boolean doEnter() {
        Node<String> current = current();
        if (current != null && current.hasChildren()) {
            parentNodeStack.add(new NodePos(this.cursorPos, current.getParent()));
            refresh(current, 0);
            fireEvent(new LcdMenuNavEvent(LcdMenuNavEvent.LCD_NAV_ENTER, current.getParent(), this));
            return true;
        }
        return false;
    }

    public boolean doExit() {
        if (!parentNodeStack.isEmpty()) {
            NodePos parent = parentNodeStack.pop();
            refresh(parent.node, parent.position);
            fireEvent(new LcdMenuNavEvent(LcdMenuNavEvent.LCD_NAV_EXIT, current(), this));
            return true;
        }
        return false;
    }

    public boolean doSelect() {
        Node<String> current = current();
        if (isValidIndex(this.cursorPos) && ((current != null) && !current.hasChildren())) {
            fireEvent(new LcdMenuItemEvent(LcdMenuItemEvent.LCD_ITEM_SELECTED, current, cursorPos, this));
            return true;
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold desc="Event Handler Getters/Setters">
    public EventHandler<? super LcdMenuNavEvent> getOnNavNext() {
        return onNavNext;
    }

    public EventHandler<? super LcdMenuNavEvent> getOnNavPrevious() {
        return onNavPrevious;
    }

    public EventHandler<? super LcdMenuNavEvent> getOnNavEnter() {
        return onNavEnter;
    }

    public EventHandler<? super LcdMenuNavEvent> getOnNavExit() {
        return onNavExit;
    }

    public EventHandler<? super LcdMenuItemEvent> getOnItemFocus() {
        return onItemFocus;
    }

    public EventHandler<? super LcdMenuItemEvent> getOnItemSelected() {
        return onItemSelected;
    }

    public void setOnNavNext(EventHandler<? super LcdMenuNavEvent> handler) {
        this.onNavNext = handler;
        //addHandler(LcdMenuNavEvent.LCD_NAV_NEXT, handler);
    }

    public void setOnNavPrevious(EventHandler<? super LcdMenuNavEvent> handler) {
        this.onNavPrevious = handler;
        //addHandler(LcdMenuNavEvent.LCD_NAV_PREVIOUS, handler);
    }

    public void setOnNavEnter(EventHandler<? super LcdMenuNavEvent> handler) {
        this.onNavEnter = handler;
        //addHandler(LcdMenuNavEvent.LCD_NAV_ENTER, handler);
    }

    public void setOnNavExit(EventHandler<? super LcdMenuNavEvent> handler) {
        this.onNavExit = handler;
        //addHandler(LcdMenuNavEvent.LCD_NAV_EXIT, handler);
    }

    public void setOnItemSelected(EventHandler<? super LcdMenuItemEvent> handler) {
        this.onItemSelected = handler;
        //addHandler(LcdMenuItemEvent.LCD_ITEM_SELECTED, handler);
    }

    public void setOnItemFocus(EventHandler<? super LcdMenuItemEvent> handler) {
        this.onItemFocus = handler;
        //addHandler(LcdMenuItemEvent.LCD_ITEM_FOCUS, handler);
    }
    //</editor-fold>

    @Override
    protected void drawNode(CharGraphics graphics) {
        graphics.clear();

        int maxRows = getHeight();
        int totalPages = getTotalPages(maxRows);
        int startRowOffset = isHeaderVisible() ? 1 : 0;

        RangeMap<Integer, List<Node<String>>> pageMap = createPageRangeMap(maxRows - startRowOffset);
        Map.Entry<Range<Integer>, List<Node<String>>> entry = pageMap.getEntry(this.cursorPos);

        if (entry == null) {
            log.warn("No menu entry found for index '{}'", this.cursorPos);
            return;
        }

        List<Node<String>> pageEntries = entry.getValue();
        int localIndex = cursorPos - entry.getKey().lowerEndpoint();
        this.pageNumber = calcPageNumber(maxRows, entry.getKey().upperEndpoint());

        //Is header enabled?
        if (isHeaderVisible() && !pageEntries.isEmpty() && startRowOffset > 0) {
            graphics.setCursor(0, 0);
            drawMenuHeader(pageEntries.get(0).getParent(), graphics);
        }

        log.debug("Cursor: {}, Local Cursor: {}, Total Pages: {}, Total Entries: {}, Lower: {}, Upper: {}, Page Number: {}",
                cursorPos, localIndex, totalPages, pageEntries.size(), entry.getKey().lowerEndpoint(), entry.getKey().upperEndpoint(), pageNumber);

        for (int i = 0; i < pageEntries.size(); i++) {
            graphics.setCursor(0, i + startRowOffset);
            drawMenuItem((i + entry.getKey().lowerEndpoint()) + 1, pageEntries.get(i), graphics, (localIndex == i));
        }
    }

    private void drawMenuHeader(Node<String> parentNode, CharGraphics graphics) {
        graphics.setCursor(0, 0);
        String headerText = parentNode.getName();
        int remainingChars = graphics.getWidth();
        headerText = textProcessor.process(headerText);
        headerText = StringUtils.abbreviate(headerText, DEFAULT_MENU_ITEM_ABBR, remainingChars);
        headerText = alignText(headerText, headerTextAlignment, remainingChars - 1);
        graphics.drawText(headerText);
        if (parentNode.hasParent())
            graphics.drawText(0);
    }

    private void drawMenuItem(final int index, final Node<String> node, final CharGraphics graphics, final boolean isSelected) {
        final int maxChars = graphics.getWidth();
        final ByteBuffer buffer = ByteBuffer.allocate(maxChars);

        log.debug("Item: {}, Is Root: {}", node.getName(), node.isRoot());

        //Is the cursor set to the left position?
        if (isCursorVisible() && LcdMenuCursorOrientation.LEFT.equals(cursorOrientation)) {
            if (isSelected) {
                buffer.put((menuCursorChar != null) ? menuCursorChar : CharGraphics.CHAR_RIGHTARROW);
            } else
                buffer.put(CharGraphics.CHAR_SPACE);
            buffer.put(CharGraphics.CHAR_SPACE);
        }

        //Add the item prefix
        if (itemStyle == LcdMenuItemStyle.NUMBERED) {
            String formattedNumber = menuItemNumberFormatter.format(index);
            buffer.put(formattedNumber.getBytes());
        } else if (itemStyle == LcdMenuItemStyle.BULLETED) {
            buffer.put(CharGraphics.CHAR_BULLETPOINT);
        }

        String text = node.getName();
        int rightCursorOffset = (LcdMenuCursorOrientation.RIGHT.equals(cursorOrientation)) ? 2 : 0;

        //Abbreviate the text if it is greater than the number of characters available
        if (text.length() > buffer.remaining())
            text = StringUtils.abbreviate(text, DEFAULT_MENU_ITEM_ABBR, buffer.remaining() - rightCursorOffset);
        text = alignText(text, itemTextAlignment, buffer.remaining() - rightCursorOffset);

        //Text should be evaluated for custom character codes before adding to the buffer
        buffer.put(textProcessor.process(text).getBytes());

        //Add the right side cursor
        if (isCursorVisible() && isSelected && rightCursorOffset > 0) {
            buffer.put(CharGraphics.CHAR_SPACE);
            buffer.put((menuCursorChar != null) ? menuCursorChar : CharGraphics.CHAR_LEFTARROW);
        }

        //Fill the remaining with space
        while (buffer.hasRemaining())
            buffer.put(CharGraphics.CHAR_SPACE);

        graphics.drawText(buffer.array());
    }

    private String alignText(String text, TextAlignment alignment, int maxWidth) {
        if (TextAlignment.LEFT.equals(alignment))
            text = StringUtils.rightPad(text, maxWidth);
        else if (TextAlignment.RIGHT.equals(alignment))
            text = StringUtils.leftPad(text, maxWidth);
        else if (TextAlignment.CENTER.equals(alignment))
            text = StringUtils.center(text, maxWidth);
        return text;
    }

    private void refresh(Node<String> node, int index) {
        if (!node.hasChildren()) {
            log.debug("Node does not have any children");
            return;
        }
        this.entries.clear();
        this.entries.addAll(node.getChildren());
        this.cursorPos = index;
        redraw();
    }

    private Node<String> current() {
        if (!isValidIndex(this.cursorPos))
            return null;
        return this.entries.get(cursorPos);
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index <= (this.entries.size() - 1);
    }

    private int calcPageNumber(int maxEntries, int upperBound) {
        return ((upperBound + 1) / maxEntries) + (((upperBound + 1) % maxEntries) > 0 ? 1 : 0);
    }

    private RangeMap<Integer, List<Node<String>>> createPageRangeMap(int pageLimit) {
        RangeMap<Integer, List<Node<String>>> rangeMap = TreeRangeMap.create();
        int maxEntries = entries.size();
        for (int startIdx = 0; startIdx < maxEntries; startIdx += pageLimit - 1) {
            int endIdx = startIdx + (pageLimit - 1);
            if (endIdx >= maxEntries)
                endIdx = (startIdx + (maxEntries - startIdx)) - 1;
            rangeMap.put(Range.closed(startIdx, endIdx), entries.subList(startIdx, endIdx + 1));
            startIdx++;
        }
        return rangeMap;
    }

    private int getTotalPages(int maxRows) {
        return (entries.size() / maxRows) + ((entries.size() % maxRows) != 0 ? 1 : 0);
    }

    private class NodePos {
        int position;
        Node<String> node;

        NodePos(int position, Node<String> node) {
            this.position = position;
            this.node = node;
        }
    }
}
