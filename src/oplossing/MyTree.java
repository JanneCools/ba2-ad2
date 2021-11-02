package oplossing;

import opgave.Node;
import opgave.SearchTree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class MyTree<E extends Comparable<E>> implements SearchTree {

    private Node<E> root;
    private HashMap<Node<E>, Node<E>> parents;
    private final NodeSearcher<E> searcher;
    private final ListMaker<E> listMaker;

    public MyTree() {
        root = null;
        parents = new HashMap<>();
        searcher = new NodeSearcher<>();
        listMaker = new ListMaker<>();
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
            if (greatestChild == null && node.getRight() == null) {     // De te verwijderen top is een blad
                Node<E> parent = parents.get(node);
                if (Objects.equals(node, root)) {
                    root = null;
                } else if (Objects.equals(node, parent.getLeft())) {
                    parent.setLeft(null);
                } else {
                    parent.setRight(null);
                }
                parents.remove(node);
            } else if (greatestChild == null) {         // De top heeft geen linkerkinderen
                Node<E> child = node.getRight();
                Node<E> parent = parents.get(node);
                if (Objects.equals(root, node)) {
                    root = child;
                } else if (Objects.equals(parent.getRight(), node)) {
                    parent.setRight(child);
                } else {
                    parent.setLeft(child);
                }
                parents.replace(child, parent);
                parents.remove(node);
            } else {
                Node<E> child = greatestChild.getLeft();
                Node<E> parent = parents.get(greatestChild);
                node.setValue(greatestChild.getValue());
                if (Objects.equals(parent.getRight(), greatestChild)) {
                    parent.setRight(child);
                } else {
                    parent.setLeft(child);
                }
                parents.replace(child, parent);
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
        ArrayList<E> list = listMaker.makeList(new ArrayList<>(), root);
        return list.listIterator();
    }
}
