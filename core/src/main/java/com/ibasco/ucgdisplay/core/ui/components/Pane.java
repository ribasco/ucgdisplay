package com.ibasco.ucgdisplay.core.ui.components;

import com.ibasco.ucgdisplay.core.DisplayParent;
import com.ibasco.ucgdisplay.core.ui.Graphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple container for display components
 *
 * @param <T>
 *         An implementation of the {@link Graphics} interface
 *
 * @author Rafael Ibasco
 */
abstract public class Pane<T extends Graphics> extends DisplayParent<T> {

    private static final Logger log = LoggerFactory.getLogger(Pane.class);

    public Pane() {
        this(null, null);
    }

    protected Pane(Integer width, Integer height) {
        super(width, height);
    }
}
