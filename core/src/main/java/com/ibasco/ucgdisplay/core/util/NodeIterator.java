package com.ibasco.ucgdisplay.core.util;

/**
 * An extension of {@link NavigableListIterator} with the added feature of allowing to move one level down/up to the
 * parent/child node elements
 *
 * @param <T>
 *         The underlying data type of the {@link Node}
 *
 * @author Rafael Ibasco
 * @see NavigableListIterator
 */
public interface NodeIterator<T> extends NavigableListIterator<Node<T>> {
    /**
     * Moves one level down to the child nodes of the current parent {@link Node}
     *
     * @return Returns the first child {@link Node} of the parent
     *
     * @see #exit()
     */
    Node<T> enter();

    /**
     * This will move back the iterator one level up to the parent of the current node.
     *
     * @return Returns the parent {@link Node}
     *
     * @see #enter()
     */
    Node<T> exit();

    /**
     * Returns {@code True} if the current element is in the root element of the tree
     *
     * @return {@code True} if the current element is in the root element of the tree
     */
    boolean isRoot();

    /**
     * @return Returns {@code True} if the current {@link Node} contains child elements
     */
    boolean hasChildren();

    /**
     * @return Returns {@code True} if the current {@link Node} has a parent {@link Node}
     */
    boolean hasParent();

    /**
     * The depth level of the current {@link Node}
     *
     * @return Returns the depth level of the current {@link Node}
     */
    int depth();
}
