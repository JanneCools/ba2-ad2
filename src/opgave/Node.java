package opgave;

/**
 * A node in a balanced binary search tree.
 *
 * @param <E> the type of the value included in this node.
 */
public class Node<E extends Comparable<E>> {
    protected E value;
    protected Node<E> left;
    protected Node<E> right;

    /**
     * Create a new node with the specified value
     *
     * @param value that should be used as key for this node
     */
    public Node(E value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot not be null");
        }
        this.value = value;
        this.left = null;
        this.right = null;
    }

    /**
     * Retrieves the value included in this node
     *
     * @return the value included in this node
     */
    public E getValue() {
        return value;
    }

    /**
     * Sets the value included in this node
     *
     * @param value the new value to be used as key for this node
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Sets the given node as left child of this node, replacing the left
     * subtree of this node.
     *
     * @param left the node to be set as left child
     */
    public void setLeft(Node<E> left) {
        this.left = left;
    }

    /**
     * Sets the given node as right child of this node, replacing the eight
     * subtree of this node.
     *
     * @param right the node to be set as right child
     */
    public void setRight(Node<E> right) {
        this.right = right;
    }

    /**
     * Get the left child node of this node, may be <tt>null</tt>
     *
     * @return the left child node of this node
     */
    public Node<E> getLeft() {
        return left;
    }

    /**
     * Get the right child node of this node, may be <tt>null</tt>
     *
     * @return the right child node of this node
     */
    public Node<E> getRight() {
        return right;
    }
}
