package com.ibasco.ucgdisplay.core.util;

/**
 * A functional Consumer type interface which process {@link Node} and maintains the current depth level.
 */
@FunctionalInterface
public interface NodeTreeProcessor<T> {
    void process(Node<T> node, int depth);
}
