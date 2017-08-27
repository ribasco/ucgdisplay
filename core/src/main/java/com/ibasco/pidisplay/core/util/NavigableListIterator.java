package com.ibasco.pidisplay.core.util;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A {@link ListIterator} implementation with additional support for peeking and retrieval of current element
 *
 * @param <T>
 *         The element type of the iterator
 *
 * @author Rafael Ibasco
 */
public interface NavigableListIterator<T> extends ListIterator<T> {
    /**
     * Returns the current element the iterator is pointing at.
     *
     * @return The current element the iterator is pointing at.
     */
    T current();

    /**
     * Returns the current index of the iterator
     *
     * @return Returns the current index of the iterator
     */
    int currentIndex();

    /**
     * Returns the next element in the iteration, without advancing the iteration.
     *
     * <p>Calls to {@code peekNext()} should not change the state of the iteration,
     * except that it <i>may</i> prevent removal of the most recent element via
     * {@link #remove()}.
     *
     * @throws NoSuchElementException
     *         if the iteration has no more elements
     *         according to {@link #hasNext()}
     */
    T peekNext();

    /**
     * Returns the previous element in the iteration, without advancing the iteration.
     *
     * <p>Calls to {@code peekPrevious()} should not change the state of the iteration,
     * except that it <i>may</i> prevent removal of the most recent element via
     * {@link #remove()}.
     *
     * @throws NoSuchElementException
     *         if the iteration has no more elements
     *         according to {@link #hasPrevious()}
     */
    T peekPrevious();

    /**
     * Resets the current position of the iterator
     */
    void reset();
}
