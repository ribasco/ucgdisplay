package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ListChangeListener;
import com.ibasco.pidisplay.core.beans.ObservableList;
import com.ibasco.pidisplay.core.beans.ObservableListWrapper;
import com.ibasco.pidisplay.core.events.FocusEvent;
import com.ibasco.pidisplay.core.ui.Graphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * A {@link DisplayNode} that contains child-nodes
 *
 * @param <T>
 *         The underlying {@link Graphics} driver implementation
 *
 * @author Rafael Ibasco
 */
abstract public class DisplayParent<T extends Graphics> extends DisplayNode<T> {

    public static final Logger log = LoggerFactory.getLogger(DisplayParent.class);

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

    protected ObservableList<DisplayNode<T>> children = new ObservableListWrapper<DisplayNode<T>>() {
        @Override
        protected void invalidatedList(ListChangeListener.Change<? extends DisplayNode<T>> changeDetails) {
            if (changeDetails.removed())
                changeDetails.getRemoved().forEach(n -> n.setParent(null));
            else if (changeDetails.added())
                changeDetails.getList().forEach(a -> a.setParent(DisplayParent.this));
        }
    };

    protected DisplayParent(Integer width, Integer height) {
        super(width, height);
    }

    protected DisplayParent(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }

    @SafeVarargs
    protected final void add(DisplayNode<T>... nodes) {
        Collections.addAll(this.children, nodes);
    }

    protected void remove(DisplayNode<T> component) {
        this.children.remove(component);
    }

    protected boolean hasChildren() {
        return this.children != null && this.children.size() > 0;
    }

    protected ObservableList<DisplayNode<T>> getChildren() {
        return children;
    }

    //region Propagated Properties
    @Override
    protected void postFlush(T graphics) {
        doAction(DisplayNode::postFlush, graphics);
    }

    @Override
    void setInitialized(boolean initialized) {
        super.setInitialized(initialized);
        doAction(DisplayNode::setInitialized, initialized);
    }

    @Override
    public void setController(Controller<T> controller) {
        super.setController(controller);
        doAction(DisplayNode::setController, controller);
    }

    @Override
    void setActive(boolean active) {
        super.setActive(active);
        doAction((node, activated) -> {
            node.setActive(activated);
            if (node.isFocusable()) {
                if (activated) {
                    log.debug("Adding focus event handler for : {}", node);
                    node.addEventHandler(FocusEvent.ENTER_FOCUS, node.focusEnterEventHandler, CAPTURE);
                    node.addEventHandler(FocusEvent.EXIT_FOCUS, node.focusExitEventHandler, CAPTURE);
                } else {
                    log.debug("Removing focus event handler for : {}", node);
                    node.addEventHandler(FocusEvent.ENTER_FOCUS, node.focusEnterEventHandler, CAPTURE);
                    node.addEventHandler(FocusEvent.EXIT_FOCUS, node.focusExitEventHandler, CAPTURE);
                }
            }
        }, active);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        doAction(DisplayNode::setVisible, visible);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        doAction(DisplayNode::setEnabled, enabled);
    }
    //endregion

    @Override
    final void draw(T graphics) {
        super.draw(graphics);
        doAction(DisplayNode::drawNode, graphics);
    }

    @Override
    protected void drawNode(T graphics) {
        //optional
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
        if (node instanceof DisplayParent) {
            DisplayParent<T> parent = (DisplayParent<T>) node;
            //Perform action on the top level first
            for (DisplayNode<T> subNode : parent.getChildren()) {
                action.doAction(subNode, arg);
                doAction(subNode, action, arg, depth + 1);
            }
        }
    }
}
