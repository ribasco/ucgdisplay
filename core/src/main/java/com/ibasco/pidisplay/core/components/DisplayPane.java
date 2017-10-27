package com.ibasco.pidisplay.core.components;

import com.ibasco.pidisplay.core.DisplayParent;
import com.ibasco.pidisplay.core.Graphics;
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
abstract public class DisplayPane<T extends Graphics> extends DisplayParent<T> {

    private static final Logger log = LoggerFactory.getLogger(DisplayPane.class);

    public DisplayPane() {
        this(null, null);
    }

    protected DisplayPane(Integer width, Integer height) {
        super(width, height);
    }
}
