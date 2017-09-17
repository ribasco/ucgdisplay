package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.beans.PropertyChangeListener;
import com.ibasco.pidisplay.core.events.EventDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

abstract public class DisplayNode<T extends Graphics>
        extends DisplayRegion implements Display<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayNode.class);

    //region Properties
    protected ObservableProperty<String> name = new ObservableProperty<>(this.getClass().getSimpleName().toLowerCase());

    protected ObservableProperty<Boolean> visible = new ObservableProperty<>(false);

    protected ObservableProperty<Boolean> enabled = new ObservableProperty<>(false);

    protected ObservableProperty<Boolean> active = new ObservableProperty<>(false);
    //endregion

    private List<DisplayNode<T>> children = new ArrayList<>();

    private static final AtomicInteger idCtr = new AtomicInteger(0);

    protected DisplayNode<T> parent;

    private int id = idCtr.incrementAndGet();

    /**
     * Represents an action that will be performed on a {@link DisplayNode}
     *
     * @param <X>
     *         The underlying {@link Graphics} type of the {@link DisplayNode}
     * @param <Y>
     *         The type of the argument for the operation being called
     */
    @FunctionalInterface
    protected interface DisplayAction<X extends Graphics, Y> {
        void doAction(DisplayNode<X> node, Y arg);
    }

    //region Constructor
    protected DisplayNode(Integer width, Integer height) {
        this(null, null, width, height);
    }

    protected DisplayNode(Integer x, Integer y, Integer width, Integer height) {
        super(x, y, width, height);
        redrawOnChange(this.x, this.y, this.visible, this.width, this.height,
                this.minWidth, this.minHeight, this.maxWidth, this.maxHeight);
    }
    //endregion

    //region Property Getter/Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
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

    //endregion

    public void setOnVisibilityChange(PropertyChangeListener<Boolean> listener) {
        visible.addListener(listener);
    }

    public void setOnActiveChange(PropertyChangeListener<Boolean> listener) {
        active.addListener(listener);
    }

    public void setOnEnableChange(PropertyChangeListener<Boolean> listener) {
        enabled.addListener(listener);
    }

    /**
     * The primary method used by the system to draw the component
     *
     * @param graphics
     *         The {@link Graphics} implementation used by this node
     */
    abstract protected void drawNode(T graphics);

    @Override
    public final void draw(T graphics) {
        if (graphics == null)
            throw new IllegalArgumentException("Graphics object must not be null");
        EventDispatcher.checkEventDispatchThread();
        if (!isActive()) {
            log.debug("Display '{}' Not active. Skipped", this);
            return;
        }
        doAction(DisplayNode::drawNode, graphics);
    }

    //region Protected Methods
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

    /**
     * Defaults to {@link #doAction(DisplayNode, DisplayAction, Object, int)} with a default depth level of 0
     *
     * @see #doAction(DisplayNode, DisplayAction, Object, int)
     */
    protected <Y> void doAction(DisplayAction<T, Y> action, Y arg) {
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
     *         The {@link DisplayAction} which contains the operations to be performed on the child nodes
     * @param arg
     *         The argument of the operation
     * @param depth
     *         The current depth level on the node tree
     * @param <Y>
     *         The type of the argument of the operation
     */
    protected <Y> void doAction(DisplayNode<T> component, DisplayAction<T, Y> action, Y arg, int depth) {
        for (DisplayNode<T> node : component.getChildren()) {
            action.doAction(node, arg);
            doAction(node, action, arg, depth + 1);
        }
    }

    @SuppressWarnings("unchecked")
    protected void redrawOnChange(ObservableProperty... properties) {
        if (properties != null) {
            PropertyChangeListener changeListener = (observable, oldValue, newValue) -> this.redraw();
            for (ObservableProperty property : properties)
                property.addListener(changeListener);
        }
    }
    //endregion

    //region Equals/HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisplayNode<?> that = (DisplayNode<?>) o;

        if (id != that.id) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id;
        return result;
    }
    //endregion

    @Override
    public String toString() {
        return String.format("%s (id: %d)", getName(), getId());
    }
}
