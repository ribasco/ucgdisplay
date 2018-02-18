package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ObservableProperty;
import com.ibasco.pidisplay.core.events.FocusEvent;
import com.ibasco.pidisplay.core.ui.Graphics;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @FunctionalInterface
    protected interface InvalidatedCallback<T> {
        void invalidated(T oldValue, T newValue);
    }

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

    private DisplayParent<T> parent;

    private final long id = idCounter.incrementAndGet();

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private int depth = 0;

    private AtomicBoolean dirty = new AtomicBoolean(true);
    //endregion

    ObservableProperty<EventDispatcher> eventDispatcher;

    EventHandlerManager eventHandlerManager;

    final EventHandler<FocusEvent> focusEnterEventHandler = this::onFocusEnter;

    final EventHandler<FocusEvent> focusExitEventHandler = this::onFocusExit;

    //region Constructor
    protected DisplayNode(Integer width, Integer height) {
        this(null, null, width, height);
    }

    protected DisplayNode(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }
    //endregion

    protected void onFocusEnter(FocusEvent focusEvent) {
    }

    protected void onFocusExit(FocusEvent focusEvent) {

    }

    //region Property Value Getter/Setters
    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    protected void setFocusable(boolean focusable) {
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
        T graphics = getController().getGraphics();
        if (active) {
            initialize(graphics);
            onActivate();
        } else {
            cleanUp(graphics);
            onDeactivate();
        }
    }

    protected void initialize(T graphics) {

    }

    protected void cleanUp(T graphics) {

    }

    protected void onDeactivate() {
        //no implementation. meant to be overriden by sub-classes
    }

    protected void onActivate() {
        //no implementation. meant to be overriden by sub-classes
    }

    public boolean isVisible() {
        return this.visible.get();
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }

    public DisplayParent<T> getParent() {
        return parent;
    }

    protected void setParent(DisplayParent<T> parent) {
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

    protected void postFlush(T graphics) {
        //no implementation. meant to be overriden by sub-classes
    }
    //endregion

    //region Protected Methods
    public ObservableProperty<Controller<T>> controllerProperty() {
        if (controller == null)
            controller = new ObservableProperty<>(null);
        return controller;
    }

    public Controller<T> getController() {
        return controllerProperty().get();
    }

    public void setController(Controller<T> controller) {
        controllerProperty().set(controller);
    }

    protected boolean hasParent() {
        return parent != null;
    }

    public void fireEvent(Event event) {
        Event.fireEvent(this, event);
    }
    //endregion

    //region Package-Private Methods

    /**
     * Initialize default dimensions
     *
     * @param graphics
     *         The {@link Graphics} driver of this node
     */
    void initDimensions(T graphics) {
        if (!this.maxWidth.isSet())
            this.maxWidth.setValid(graphics.getWidth());
        if (!this.maxHeight.isSet())
            this.maxHeight.setValid(graphics.getHeight());
        if (!this.width.isSet())
            this.width.setValid(graphics.getWidth());
        if (!this.height.isSet())
            this.height.setValid(graphics.getHeight());
    }

    /**
     * To be called by the {@link Controller} and should not be called elsewhere
     *
     * @param graphics
     *         The underlying {@link Graphics} driver used by this node
     */
    void draw(T graphics) {
        validate(graphics);
        //Draw the current node
        this.drawNode(graphics);
    }

    /**
     * Validate {@link DisplayNode} state
     *
     * @param graphics
     *         The underlying {@link Graphics} driver used by this node
     */
    final void validate(T graphics) {
        if (isAttached())
            this.getController().checkEventDispatchThread();

        if (graphics == null)
            throw new NullPointerException("Graphics object must not be null");
    }
    //endregion

    final boolean isAttached() {
        return getController() != null;
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

    public final <E extends Event> void addEventHandler(final EventType<E> eventType, final EventHandler<? super E> eventHandler) {
        addEventHandler(eventType, eventHandler, CAPTURE);
        addEventHandler(eventType, eventHandler, BUBBLE);
        addEventHandler(eventType, eventHandler, POST_DISPATCH);
    }

    public final <E extends Event> void addEventHandler(final EventType<E> eventType, final EventHandler<? super E> eventHandler, EventDispatchPhase phase) {
        getEventHandlerManager().addEventHandler(eventType, eventHandler, phase);
    }

    public final <E extends Event> void removeEventHandler(final EventType<E> eventType, final EventHandler<? super E> eventHandler, EventDispatchPhase phase) {
        getEventHandlerManager().removeEventHandler(eventType, eventHandler, phase);
    }

    protected <B> ObservableProperty<B> createProperty(B defaultValue) {
        return createProperty(false, defaultValue);
    }

    protected <B> ObservableProperty<B> createProperty(boolean redrawInvalid, B defaultValue) {
        return createProperty(redrawInvalid, defaultValue, null);
    }

    protected <B> ObservableProperty<B> createProperty(boolean redrawInvalid, B defaultValue, InvalidatedCallback<B> callback) {
        if (!redrawInvalid)
            return new ObservableProperty<>(defaultValue);
        return new ObservableProperty<B>(defaultValue) {
            @Override
            protected void invalidated(B oldValue, B newValue) {
                if (callback != null)
                    callback.invalidated(oldValue, newValue);
            }
        };
    }

    boolean isDirty() {
        return dirty.get();
    }

    void setDirty(boolean dirty) {
        this.dirty.set(dirty);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public EventDispatchChain buildEventTargetPath(EventDispatchChain tail) {
        DisplayNode<T> current = this;
        //loop through all parent nodes and prepend it to the tail of the chain
        while (current != null) {
            if (current.eventDispatcher != null) {
                final EventDispatcher eventDispatcherValue = current.eventDispatcher.get();
                if (eventDispatcherValue != null)
                    tail = tail.addFirst(eventDispatcherValue);
            }
            current = current.getParent();
        }
        //Append event dispatcher of this node's controller
        /*if (getController() != null) {
            tail = getController().buildEventTargetPath(tail);
        } else
            log.error("Skipping append controller dispatcher");*/
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
