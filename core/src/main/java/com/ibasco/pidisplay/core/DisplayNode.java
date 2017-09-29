package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.events.DisplayEvent;
import com.ibasco.pidisplay.core.events.Event;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The base class for all display components.
 *
 * @param <T>
 *         The underlying type of the {@link Graphics} implementation used by this display node
 *
 * @author Rafael Ibasco
 */
@SuppressWarnings("WeakerAccess")
abstract public class DisplayNode<T extends Graphics>
        extends DisplayRegion {

    //region Static Properties
    private static final Logger log = LoggerFactory.getLogger(DisplayNode.class);

    private static final AtomicLong idCounter = new AtomicLong(0);
    //endregion

    //region Properties
    protected ObservableProperty<String> name = new ObservableProperty<>(this.getClass().getSimpleName().toLowerCase());

    protected ObservableProperty<Boolean> visible = new ObservableProperty<>(false);

    protected ObservableProperty<Boolean> enabled = new ObservableProperty<>(false);

    protected ObservableProperty<Boolean> active = new ObservableProperty<>(false);

    protected ObservableProperty<Boolean> focused = new ObservableProperty<>(false);

    protected ObservableProperty<Integer> scrollTop = new ObservableProperty<>(0);

    protected ObservableProperty<Integer> scrollLeft = new ObservableProperty<>(0);

    private List<DisplayNode<T>> children = new ArrayList<>();

    private DisplayNode<T> parent;

    private final long id = idCounter.incrementAndGet();

    private AtomicBoolean initialized = new AtomicBoolean(false);

    EventDispatcher eventDispatcher;
    //endregion

    //region Inner Classes

    /**
     * Represents an action that will be performed on a {@link DisplayNode}
     *
     * @param <X>
     *         The underlying {@link Graphics} type of the {@link DisplayNode}
     * @param <Y>
     *         The type of the argument for the operation being called
     */
    @FunctionalInterface
    protected interface Action<X extends Graphics, Y> {
        void doAction(DisplayNode<X> node, Y arg);
    }
    //endregion

    //region Constructor
    protected DisplayNode(Integer width, Integer height) {
        this(null, null, width, height);
    }

    protected DisplayNode(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }
    //endregion

    //region Property Value Getter/Setters
    public long getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isFocused() {
        return focused.get();
    }

    public void setFocused(boolean focused) {
        this.focused.set(focused);
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
        doAction(DisplayNode::setEnabled, enabled);
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
        doAction(DisplayNode::setActive, active);
    }

    public boolean isVisible() {
        return this.visible.get();
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
        doAction(DisplayNode::setVisible, visible);
    }

    public DisplayNode<T> getParent() {
        return parent;
    }

    protected void setParent(DisplayNode<T> parent) {
        this.parent = parent;
    }

    public Integer getScrollTop() {
        return scrollTop.get();
    }

    public void setScrollTop(Integer scrollTop) {
        this.scrollTop.set(scrollTop);
    }

    public Integer getScrollLeft() {
        return scrollLeft.get();
    }

    public void setScrollLeft(Integer scrollLeft) {
        this.scrollLeft.set(scrollLeft);
    }

    boolean isInitialized() {
        return initialized.get();
    }

    void setInitialized(boolean initialized) {
        this.initialized.set(initialized);
    }
    //endregion

    //region Property Object Getters
    public ObservableProperty<Boolean> activeProperty() {
        return active;
    }

    public ObservableProperty<Boolean> visibleProperty() {
        return visible;
    }

    public ObservableProperty<Boolean> enabledProperty() {
        return enabled;
    }

    public ObservableProperty<Boolean> focusedProperty() {
        return focused;
    }

    public ObservableProperty<Integer> scrollTopProperty() {
        return scrollTop;
    }

    public ObservableProperty<Integer> scrollLeftProperty() {
        return scrollLeft;
    }
    //endregion

    //region Abstract Methods

    /**
     * The primary method used by the system to draw the component
     *
     * @param graphics
     *         The {@link Graphics} implementation used by this node
     */
    abstract protected void drawNode(T graphics);
    //endregion

    //region Protected Methods

    /**
     * @return A {@link List} of {@link ObservableProperty} instances that needs to be redrawn for every change event
     * that occurs.
     */
    protected List<ObservableProperty> getChangeListeners() {
        List<ObservableProperty> changeListeners = new ArrayList<>();
        changeListeners.add(this.leftPos);
        changeListeners.add(this.topPos);
        changeListeners.add(this.visible);
        changeListeners.add(this.width);
        changeListeners.add(this.height);
        changeListeners.add(this.minWidth);
        changeListeners.add(this.minHeight);
        changeListeners.add(this.maxWidth);
        changeListeners.add(this.maxHeight);
        changeListeners.add(this.scrollTop);
        changeListeners.add(this.scrollLeft);
        changeListeners.add(this.focused);
        return changeListeners;
    }

    protected void redraw() {
        fireEvent(new DisplayEvent<>(DisplayEvent.DISPLAY_DRAW, this));
    }

    protected void add(DisplayNode<T> component) {
        component.visible.setValid(true);
        component.parent = this;
        this.children.add(component);
    }

    protected void remove(DisplayNode<T> component) {
        component.visible.setValid(false);
        component.parent = null;
        this.children.remove(component);
    }

    protected List<DisplayNode<T>> getChildren() {
        return this.children;
    }

    protected boolean hasParent() {
        return parent != null;
    }

    protected boolean hasChildren() {
        return this.children != null && this.children.size() > 0;
    }

    protected void fireEvent(Event event) {
        if (this.eventDispatcher == null) {
            log.warn("Event fired but event dispatcher is not set");
            return;
        }
        this.eventDispatcher.dispatch(event);
    }
    //endregion

    //region Package-Private Methods

    /**
     * To be called by the {@link DisplayController} and should not be called elsewhere
     *
     * @param graphics
     *         The {@link Graphics} driver to use for this node
     */
    final void draw(T graphics) {
        if (eventDispatcher != null && !eventDispatcher.isEventDispatchThread())
            throw new IllegalStateException("This should not be called outside the event dispatcher thread");
        if (graphics == null)
            throw new IllegalArgumentException("Graphics object must not be null");
        if (!isActive()) {
            log.debug("Display '{}' Not active. Skipped", this);
            return;
        }
        //Draw the current node
        this.drawNode(graphics);
        //Draw the child nodes (if it exists)
        doAction(DisplayNode::drawNode, graphics);
    }

    /**
     * Defaults to {@link #doAction(Action, Object)} with a default argument of null
     *
     * @see #doAction(Action, Object)
     */
    <Y> void doAction(Action<T, Y> action) {
        doAction(action, null);
    }

    /**
     * Defaults to {@link #doAction(DisplayNode, Action, Object, int)} with a default depth level of 0
     *
     * @see #doAction(DisplayNode, Action, Object, int)
     */
    <Y> void doAction(Action<T, Y> action, Y arg) {
        if (!hasChildren())
            return;
        doAction(this, action, arg, 0);
    }

    /**
     * Performs a recursive operation on the node tree.
     *
     * @param component
     *         The {@link DisplayNode} to process
     * @param action
     *         The {@link Action} which contains the operations to be performed on the child nodes
     * @param arg
     *         The argument of the operation
     * @param depth
     *         The current depth level on the node tree
     * @param <Y>
     *         The type of the argument of the operation
     */
    private <Y> void doAction(DisplayNode<T> component, Action<T, Y> action, Y arg, int depth) {
        //Perform action on the top level first
        for (DisplayNode<T> node : component.getChildren()) {
            action.doAction(node, arg);
            doAction(node, action, arg, depth + 1);
        }
    }
    //endregion

    //region Equals/HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayNode<?> that = (DisplayNode<?>) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).toHashCode();
    }

    //endregion

    @Override
    public String toString() {
        String name = StringUtils.defaultIfBlank(this.name.getInvalid(), this.getClass().getSimpleName());
        return String.format("[%s#%d]", StringUtils.capitalize(name), this.id);
    }
}
