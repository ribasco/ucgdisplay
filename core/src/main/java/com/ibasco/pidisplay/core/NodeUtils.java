package com.ibasco.pidisplay.core;

/**
 * Utility methods for {@link DisplayNode}
 *
 * @author Rafael Ibasco
 */
public class NodeUtils {

    public interface Action<X extends Graphics, Y> {
        void doAction(DisplayNode<X> node, Y arg);
    }

    /**
     * Performs a recursive operation on the node tree.
     *
     * @param node
     *         The {@link DisplayNode} to process
     * @param action
     *         The {@link DisplayParent.Action} which contains the operations to be performed on the child nodes
     * @param arg
     *         The argument of the operation
     * @param depth
     *         The current depth level on the node tree
     * @param <Y>
     *         The type of the argument of the operation
     */
    public static <Y, G extends Graphics> void doAction(DisplayNode<G> node, Action<G, Y> action, Y arg, int depth) {
        node.setDepth(depth);
        if (node instanceof DisplayParent) {
            DisplayParent<G> parent = (DisplayParent<G>) node;
            //Perform action on the top level first
            for (DisplayNode<G> subNode : parent.getChildren()) {
                action.doAction(subNode, arg);
                doAction(subNode, action, arg, depth + 1);
            }
        }
    }
}
