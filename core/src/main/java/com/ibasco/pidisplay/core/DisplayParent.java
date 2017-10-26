package com.ibasco.pidisplay.core;

import com.ibasco.pidisplay.core.beans.ListChangeListener;
import com.ibasco.pidisplay.core.beans.ObservableList;
import com.ibasco.pidisplay.core.beans.ObservableListWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class DisplayParent<T extends Graphics> extends DisplayNode<T> {

    public static final Logger log = LoggerFactory.getLogger(DisplayParent.class);

    protected ObservableList<DisplayNode<T>> children = new ObservableListWrapper<DisplayNode<T>>() {
        @Override
        protected void invalidatedList(ListChangeListener.Change<? extends DisplayNode<T>> changeDetails) {
            if (changeDetails.removed()) {
                log.debug("Children list removed");
                changeDetails.getRemoved().forEach(n -> n.setParent(null));
            } else if (changeDetails.added()) {
                log.debug("Children list added");
            }
        }
    };

    protected DisplayParent(Integer width, Integer height) {
        super(width, height);
    }

    protected DisplayParent(Integer left, Integer top, Integer width, Integer height) {
        super(left, top, width, height);
    }

    protected ObservableList<DisplayNode<T>> getChildren() {
        return children;
    }

    //region Propagated Properties
    @Override
    void setActive(boolean active) {
        super.setActive(active);
        doAction(DisplayNode::setActive, active);
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
    protected void drawNode(T graphics) {
        //draw child nodes
    }
}
