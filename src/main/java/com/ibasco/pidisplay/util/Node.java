package com.ibasco.pidisplay.util;

import com.ibasco.pidisplay.core.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

/**
 * A Iterable Tree based model used by the {@link Display} library
 *
 * @param <T>
 *         The underlying Data Type of the {@link Node}
 *
 * @author Rafael Ibasco
 */
public class Node<T> implements Iterable<Node<T>> {

    private static final Logger log = LoggerFactory.getLogger(Node.class);

    /**
     * The {@link Node} name
     */
    private String name;

    /**
     * The underlying data of this {@link Node}
     */
    private T data;

    /**
     * The contains the parent {@link Node} of this instance
     */
    private Node<T> parent;

    /**
     * The internal list of {@link Node} under this instance
     */
    private List<Node<T>> children;

    /**
     * Default Constructor. Default name is set to 'root'
     */
    public Node() {
        this("root");
    }

    /**
     * Default Constructor. The Data and Parent {@link Node} property will be set to {@code null} by default
     *
     * @param name
     *         The name of this {@link Node}
     */
    public Node(String name) {
        this(name, null);
    }

    /**
     * Node Constructor. Data property will be set to {@code null} by default.
     *
     * @param name
     *         The name of this {@link Node}
     * @param parent
     */
    public Node(String name, Node<T> parent) {
        this(name, parent, null);
    }

    /**
     * Node Constructor.
     *
     * @param name
     *         The name of this {@link Node}
     * @param parent
     *         The parent of this current {@link Node}
     * @param data
     *         The underlying data for this {@link Node}
     */
    public Node(String name, Node<T> parent, T data) {
        this.name = name;
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    /**
     * Check if the current {@link Node} is the root level node. This is similar to !{@link #hasParent()}
     *
     * @return {@code True} if the current {@link Node} is the root {@link Node}
     */
    public boolean isRoot() {
        return this.parent != null && this.parent.getParent() == null;
    }

    /**
     * Check if the current {@link Node} contains children
     *
     * @return {@code True} if the current contains child {@link Node} instances
     */
    public boolean hasChildren() {
        return this.children != null && !this.children.isEmpty();
    }

    /**
     * Check if this {@link Node} is associated with a parent {@link Node}
     *
     * @return {@code True} if this {@link Node} is associated with a parent {@link Node}
     */
    public boolean hasParent() {
        return this.parent != null;
    }

    /**
     * Retrieve the children of the node. The underlying implementation of the list is a {@link LinkedList}
     *
     * @return A {@link List} of {@link Node} instances representing the children of the current {@link Node}
     */
    public List<Node<T>> getChildren() {
        return children;
    }

    /**
     * Set the parent {@link Node}
     *
     * @param parent
     *         The parent {@link Node} instance
     */
    private void setParent(Node<T> parent) {
        this.parent = parent;
    }

    /**
     * Retrieve the underlying parent of this {@link Node}
     *
     * @return The parent {@link Node} otherwise {@code Null} if no parent is associated.
     */
    public Node<T> getParent() {
        return parent;
    }

    /**
     * Get the underlying data of this {@link Node}
     *
     * @return The underlying data of this {@link Node}
     */
    public T get() {
        return data;
    }

    /**
     * Sets the underlying data of this {@link Node}
     *
     * @param data
     *         The underlying data of this {@link Node}
     */
    public void set(T data) {
        this.data = data;
    }

    /**
     * Adds a child {@link Node} under the current {@link Node}
     *
     * @param child
     *         The child {@link Node} to add. The parent property of the child will be automatically updated by this
     *         method.
     */
    public void addChild(Node<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Removes a child {@link Node} from the current {@link Node}
     *
     * @param childNode
     *         The child {@link Node} to remove from the parent {@link Node}
     */
    public void removeChild(Node<T> childNode) {
        this.children.remove(childNode);
    }

    /**
     * Check if the specified child belongs to the {@link Node}
     *
     * @param childNode
     *         The child {@link Node} to search for
     *
     * @return {@code True} if the specified {@link Node} is a child member of the current {@link Node}
     */
    public boolean hasChild(Node<T> childNode) {
        return this.children.contains(childNode);
    }

    /**
     * The name of the node
     *
     * @return A {@link String} representing the name of the {@link Node}
     */
    public String getName() {
        return name;
    }

    /**
     * Get the last {@link Node} added to the list
     *
     * @return The last {@link Node} added with {@link #addChild(Node)}
     */
    public Node<T> getLast() {
        return this.children.get(this.children.size() - 1);
    }

    /**
     * Get the first {@link Node} added to the list
     *
     * @return The first {@link Node} added with {@link #addChild(Node)}
     */
    public Node<T> getFirst() {
        return this.children.get(0);
    }

    /**
     * <p>A convenience method that will recurse and process through all available nodes in the tree. Processing is done
     * by the specified {@link NodeTreeProcessor}.</p>
     *
     * @param node
     *         The starting/root level {@link Node} to process
     * @param processor
     *         A functional interface of type {@link NodeTreeProcessor} that will be used for processing each {@link
     *         Node} item in the tree.
     */
    public static <A> void recurse(Node<A> node, final NodeTreeProcessor<A> processor) {
        recurse(node, processor, 1);
    }

    /**
     * A convenience method that will recurse and process through all available nodes in the tree. Processing is done by
     * the specified {@link NodeTreeProcessor}.
     *
     * @param node
     *         The starting/root level {@link Node} to process
     * @param processor
     *         A functional interface of type {@link NodeTreeProcessor} that will be used for processing each {@link
     *         Node} item in the tree.
     * @param depth
     *         The starting depth. Default is 1. This will be automatically incremented for each {@link Node}
     *         level.
     */
    private static <A> void recurse(Node<A> node, NodeTreeProcessor<A> processor, int depth) {
        for (Node<A> i : node) {
            processor.process(i, depth);
            recurse(i, processor, depth + 1);
        }
    }

    public NavigableListIterator<Node<T>> listIterator() {
        return new ListItr(0);
    }

    /**
     * Returns a special {@link java.util.ListIterator} that allows you to traverse parent/child nodes.
     *
     * @return A {@link NodeIterator} instance.
     */
    @Override
    public NodeIterator<T> iterator() {
        return new NodeItr<>();
    }

    @Override
    public void forEach(Consumer<? super Node<T>> action) {
        this.children.forEach(action);
    }

    @Override
    public Spliterator<Node<T>> spliterator() {
        return this.children.spliterator();
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Inner class implementation of {@link NavigableListIterator}
     */
    private class ListItr implements NavigableListIterator<Node<T>> {

        private int currentPos = -1;

        ListItr(int index) {
            currentPos = index;
        }

        @Override
        public boolean hasNext() {
            Node<T> item = null;
            if (isValidIndex(currentPos + 1))
                item = Node.this.children.get(currentPos + 1);
            return item != null;
        }

        @Override
        public Node<T> next() {
            return Node.this.children.get(++currentPos);
        }

        @Override
        public boolean hasPrevious() {
            Node<T> item = null;
            if (isValidIndex(currentPos - 1))
                item = Node.this.children.get(currentPos - 1);
            return item != null;
        }

        @Override
        public Node<T> previous() {
            if (!isValidIndex(currentPos - 1))
                return null;
            return Node.this.children.get(--currentPos);
        }

        @Override
        public int nextIndex() {
            return currentPos + 1;
        }

        @Override
        public int previousIndex() {
            return currentPos - 1;
        }

        @Override
        public void remove() {
            Node.this.children.remove(currentPos);
        }

        @Override
        public void set(Node<T> element) {
            Node.this.children.set(currentPos, element);
        }

        @Override
        public void add(Node<T> element) {
            Node.this.children.add(element);
        }

        @Override
        public Node<T> current() {
            if (!isValidIndex(currentPos))
                throw new NoSuchElementException("Invalid index: " + currentPos + ", List Size: " + Node.this.children.size());
            return Node.this.children.get(currentPos);
        }

        @Override
        public int currentIndex() {
            return currentPos;
        }

        @Override
        public Node<T> peekNext() {
            int peekIndex = currentPos + 1;
            if (isValidIndex(peekIndex))
                return Node.this.children.get(peekIndex);
            return null;
        }

        @Override
        public Node<T> peekPrevious() {
            int peekIndex = currentPos - 1;
            if (isValidIndex(peekIndex))
                return Node.this.children.get(peekIndex);
            return null;
        }

        @Override
        public void reset() {
            currentPos = -1;
        }

        private boolean isValidIndex(int index) {
            return index >= 0 && index < Node.this.children.size();
        }
    }

    private class NodeItr<T> implements NodeIterator<T> {

        private Stack<NavigableListIterator<Node<T>>> parentNodeStack = new Stack<>();

        private NavigableListIterator<Node<T>> currentNodeIterator;

        NodeItr() {
            currentNodeIterator = new Node<T>.ListItr(-1);
        }

        @Override
        public Node<T> enter() {
            if (current() != null && current().hasChildren()) {
                log.debug("Entering");
                parentNodeStack.add(currentNodeIterator);
                currentNodeIterator = current().iterator();
                return currentNodeIterator.next();
            }
            log.debug("Not entering. Current = {}, Current has Children: {}", current(), current().hasChildren());
            return null;
        }

        @Override
        public Node<T> exit() {
            if (!parentNodeStack.isEmpty()) {
                currentNodeIterator = parentNodeStack.pop();
                log.debug("Exiting. Using Stack Method = {}", currentNodeIterator.current());
                return currentNodeIterator.current();
            } else if (current() != null && current().hasParent()) {
                log.debug("Exiting. Using Parent Method = {}", current());
                currentNodeIterator = current().getParent().iterator();
                return currentNodeIterator.next();
            }
            log.debug("Not exiting: Node Stack Empty: {}, Current has Parent: {}", parentNodeStack.isEmpty(), currentNodeIterator.current().hasParent());
            return null;
        }

        @Override
        public boolean isRoot() {
            return current().isRoot();
        }

        @Override
        public boolean hasChildren() {
            return current().hasChildren();
        }

        @Override
        public boolean hasParent() {
            return current().hasParent();
        }

        @Override
        public int depth() {
            return parentNodeStack.size() + 1;
        }

        @Override
        public Node<T> current() {
            return currentNodeIterator.current();
        }

        @Override
        public int currentIndex() {
            return currentNodeIterator.currentIndex();
        }

        @Override
        public Node<T> peekNext() {
            return currentNodeIterator.peekNext();
        }

        @Override
        public Node<T> peekPrevious() {
            return currentNodeIterator.peekPrevious();
        }

        @Override
        public void reset() {
            currentNodeIterator.reset();
        }

        @Override
        public boolean hasNext() {
            return currentNodeIterator.hasNext();
        }

        @Override
        public Node<T> next() {
            return currentNodeIterator.next();
        }

        @Override
        public boolean hasPrevious() {
            return currentNodeIterator.hasPrevious();
        }

        @Override
        public Node<T> previous() {
            return currentNodeIterator.previous();
        }

        @Override
        public int nextIndex() {
            return currentNodeIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return currentNodeIterator.previousIndex();
        }

        @Override
        public void remove() {
            currentNodeIterator.remove();
        }

        @Override
        public void set(Node<T> tNode) {
            currentNodeIterator.set(tNode);
        }

        @Override
        public void add(Node<T> tNode) {
            currentNodeIterator.add(tNode);
        }

        private <X extends Node> X checkNode(X node) {
            if (node == null)
                throw new NullPointerException("Root node cannot be null");
            if (node.isRoot() && !node.hasChildren())
                throw new IllegalStateException("Root node does not contain any children");
            return node;
        }
    }
}
