package oplossing;

import opgave.Node;
import opgave.OptimizableTree;
import java.util.*;

public class OptimalTree<E extends Comparable<E>> implements OptimizableTree<E> {

    private Node<E> root;
    private HashMap<Node<E>, Node<E>> parents;
    private final NodeSearcher<E> searcher;
    private final IteratorFactory<E> iterator;

    public OptimalTree() {
        root = null;
        parents = new HashMap<>();
        searcher = new NodeSearcher<>();
        iterator = new IteratorFactory<>();
    }

    @Override
    public void optimize(List keys, List weights) {

    }

    @Override
    public void optimize(List keys, List internalWeights, List externalWeights) {

    }

    @Override
    public int size() {
        return parents.size();
    }

    @Override
    public boolean search(Comparable o) {
        return searcher.searchNode(o, root, false, null).isFound();
    }

    @Override
    public boolean add(Comparable o) {
        if (root == null) {
            root = new Node<>(o);
            parents.put(root, null);
            return true;
        }
        boolean present = searcher.searchNode(o, root, false, null).isFound();
        if (!present) {
            Node<E> parent = searcher.getNode();
            Node<E> newNode = new Node<>(o);
            if (o.compareTo(parent.getValue()) < 0) {
                parent.setLeft(newNode);
            } else {
                parent.setRight(newNode);
            }
            parents.put(newNode, parent);
        }
        return !present;
    }

    @Override
    public boolean remove(Comparable comparable) {
        searcher.searchNode(comparable, root, false, null);
        if (searcher.isFound()) {
            Node<E> node = searcher.getNode();
            Node<E> greatestChild = searcher.findGreatestChild(node);
            Node<E> smallestChild = searcher.findSmallestChild(node);
            if (greatestChild == null && smallestChild == null) {     // De te verwijderen top is een blad
                Node<E> parent = parents.get(node);
                if (Objects.equals(node, parent.getLeft())) {
                    parent.setLeft(null);
                } else {
                    parent.setRight(null);
                }
                parents.remove(node);
            } else if (greatestChild == null) {
                Node<E> child = smallestChild.getRight();
                Node<E> parent = parents.get(smallestChild);
                node.setValue(smallestChild.getValue());
                if (Objects.equals(parent, node)) {
                    parent.setRight(child);
                } else {
                    parent.setLeft(child);
                }
                parents.remove(smallestChild);
            } else {
                Node<E> child = greatestChild.getLeft();
                Node<E> parent = parents.get(greatestChild);
                node.setValue(greatestChild.getValue());
                if (Objects.equals(parent, node)) {
                    parent.setLeft(child);
                } else {
                    parent.setRight(child);
                }
                parents.get(greatestChild);
                parents.remove(greatestChild);
            }
        }
        return searcher.isFound();
    }

    @Override
    public Node root() {
        return root;
    }

    @Override
    public Iterator iterator() {
        // Ik bereken de hoogte van de boom.
        int leftHeight = iterator.getHeight(root, "left");
        int rightHeight = iterator.getHeight(root, "right");
        int height = Math.max(leftHeight, rightHeight);

        ArrayList<Node<E>> list = iterator.makeList(root, height);
        return list.listIterator();
    }
}
