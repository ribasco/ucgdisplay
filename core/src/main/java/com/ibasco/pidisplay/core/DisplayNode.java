package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
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
abstract public class DisplayNode<T extends Graphics> extends DisplayRegion implements EventTarget {
    //region Static Properties
    private static final Logger log = LoggerFactory.getLogger(DisplayNode.class);

    private static final AtomicLong idCounter = new AtomicLong(0);
    //endregion

    //region Properties
    protected ObservableProperty<String> name = createProperty(this.getClass().getSimpleName().toLowerCase());

    protected ObservableProperty<Boolean> visible = createProperty(true, true);

    protected ObservableProperty<Controller<T>> controller;

    /**
     * Input is disabled when enabled property is false
     */
    protected ObservableProperty<Boolean> enabled = createProperty(true, false);

    /**
     * A node is considered "active" if it is currently displayed on the screen. If set to false, draw requests will not
     * be processed
     */
    protected ObservableProperty<Boolean> active = createProperty(true, false);

    protected ObservableProperty<Boolean> focused = createProperty(true, false);

    protected ObservableProperty<Integer> scrollTop = createProperty(true, 0);

    protected ObservableProperty<Integer> scrollLeft = createProperty(true, 0);

    protected ObservableProperty<Boolean> focusable = createProperty(false);

    private List<DisplayNode<T>> children = new ArrayList<>();

    private DisplayNode<T> parent;

    private final long id = idCounter.incrementAndGet();

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private int depth = 0;
    //endregion

    ObservableProperty<EventDispatcher> eventDispatcher;

    EventHandlerManager eventHandlerManager;

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

    public void setFocusable(boolean focusable) {
        this.focusable.set(focusable);
    }

    public boolean isFocusable() {
        return this.focusable.get();
    }

    public boolean isFocused() {
        return focused.get();
    }

    protected void setFocused(boolean focused) {
        this.focused.set(focused);
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public boolean isActive() {
        return active.get();
    }

    void setActive(boolean active) {
        this.active.set(active);
    }

    public boolean isVisible() {
        return this.visible.get();
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
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
    public ObservableProperty<Controller<T>> controllerProperty() {
        if (controller == null)
            controller = new ObservableProperty<>(null);
        return controller;
    }

    Controller<T> getController() {
        return controllerProperty().get();
    }

    public void setController(Controller<T> controller) {
        controllerProperty().set(controller);
    }

    @SafeVarargs
    protected final void add(DisplayNode<T>... nodes) {
        this.children.addAll(Arrays.asList(nodes));
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

    public void fireEvent(Event event) {
        Event.fireEvent(this, event);
    }
    //endregion

    //region Package-Private Methods

    /**
     * To be called by the {@link Controller} and should not be called elsewhere
     *
     * @param graphics
     *         The {@link Graphics} driver to use for this node
     */
    final void draw(T graphics) {
        if (isAttached())
            this.getController().checkEventDispatchThread();

        if (graphics == null)
            throw new NullPointerException("Graphics object must not be null");

        //Draw the child nodes (if it exists)
        doAction(DisplayNode::drawNode, graphics);

        //Draw the current node
        this.drawNode(graphics);
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

    <Y> void doAction(Action<T, Y> action, Y arg, int depth) {
        if (!hasChildren())
            return;
        doAction(this, action, arg, depth);
    }

    /**
     * Performs a recursive operation on the node tree.
     *
     * @param node
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
    <Y> void doAction(DisplayNode<T> node, Action<T, Y> action, Y arg, int depth) {
        node.setDepth(depth);
        //Perform action on the top level first
        for (DisplayNode<T> subNode : node.getChildren()) {
            action.doAction(subNode, arg);
            doAction(subNode, action, arg, depth + 1);
        }
    }
    //endregion

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    final boolean isAttached() {
        return this.controllerProperty().get() != null;
    }

    public ObservableProperty<EventDispatcher> eventDispatcherProperty() {
        if (this.eventDispatcher == null) {
            this.eventDispatcher = new ObservableProperty<>(eventHandlerManager);
        }
        return eventDispatcher;
    }

    private EventHandlerManager getEventHandlerManager() {
        if (eventHandlerManager == null)
            eventHandlerManager = new EventHandlerManager(this);
        eventDispatcherProperty();
        return eventHandlerManager;
    }

    public final EventDispatcher getEventDispatcher() {
        return eventDispatcherProperty().get();
    }

    @Override
    public final EventDispatchQueue getEventDispatchQueue() {
        Controller<T> controller = controllerProperty().get();
        if (controller != null && controller.getEventDispatchQueue() != null) {
            return controller.getEventDispatchQueue();
        }
        log.warn("There is no event dispatch queue associated with '{}'", this);
        return null;
    }

    public final void setEventDispatcher(EventDispatcher eventDispatcher) {
        if (this.eventDispatcher != null)
            this.eventDispatcher.set(eventDispatcher);
    }

    public final <T extends Event> void addEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler, EventDispatchType dispatchType) {
        getEventHandlerManager().addEventHandler(eventType, eventHandler, dispatchType);
    }

    public final <T extends Event> void removeEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler, EventDispatchType dispatchType) {
        getEventHandlerManager().removeEventHandler(eventType, eventHandler, dispatchType);
    }

    protected <B> ObservableProperty<B> createProperty(B defaultValue) {
        return createProperty(false, defaultValue);
    }

    protected <B> ObservableProperty<B> createProperty(boolean redrawInvalid, B defaultValue) {
        if (!redrawInvalid)
            return new ObservableProperty<>(defaultValue);
        return new ObservableProperty<B>(defaultValue) {
            @Override
            protected void invalidated(B oldValue, B newValue) {
                if (DisplayNode.this.isActive()) {
                    //redraw();
                }
            }
        };
    }

    @SuppressWarnings("Duplicates")
    @Override
    public EventDispatchChain buildEventTargetPath(EventDispatchChain tail) {
        DisplayNode<T> current = this;
        //loop through all succeeding parent nodes and prepend it to the tail of the chain
        while (current != null) {
            if (current.eventDispatcher != null) {
                final EventDispatcher eventDispatcherValue = current.eventDispatcher.get();
                if (eventDispatcherValue != null)
                    tail = tail.addFirst(eventDispatcherValue);
            }
            current = current.getParent();
        }
        //Append event dispatcher of this node's controller
        if (getController() != null) {
            tail = getController().buildEventTargetPath(tail);
        } else
            log.error("Skipping append controller dispatcher");
        return tail;
    }

    //region Equals/HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayNode<?> that = (DisplayNode<?>) o;
        return equals(that);
    }

    public <X extends Graphics> boolean equals(DisplayNode<X> tDisplayNode) {
        return tDisplayNode.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).toHashCode();
    }
    //endregion

    @Override
    public String toString() {
        String name = StringUtils.defaultIfBlank(this.name.get(), this.getClass().getSimpleName());
        return String.format("[%s#%d]", StringUtils.capitalize(name), this.id);
    }
}
