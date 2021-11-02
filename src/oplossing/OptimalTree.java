package oplossing;

import opgave.Node;
import opgave.OptimizableTree;

import java.lang.reflect.Array;
import java.util.*;

public class OptimalTree<E extends Comparable<E>> implements OptimizableTree<E> {

    private Node<E> root;
    private HashMap<Node<E>, Node<E>> parents;
    private final NodeSearcher<E> searcher;
    private final ListMaker<E> listMaker;
    public Node<E>[][] greatests;

    public OptimalTree() {
        root = null;
        parents = new HashMap<>();
        searcher = new NodeSearcher<>();
        listMaker = new ListMaker<>();
    }

    private static class Greatest<E extends Comparable<E>> {
        public int index;
        public E key;
        public double weight;
    }

    private Greatest<E> findGreatest(List<E> keys, List<Double> weights) {
        Greatest<E> greatest = new Greatest<>();
        greatest.index = 0;
        greatest.key = keys.get(0);
        greatest.weight = weights.get(0);
        for (int i = 0; i < keys.size(); i++) {
            E key = keys.get(i);
            double weight = weights.get(i);
            if (weight > greatest.weight) {
                greatest.index = i;
                greatest.key = key;
                greatest.weight = weight;
            }
        }
        return greatest;
    }

    @Override
    public void optimize(List<E> keys, List<Double> weights) {
        Greatest<E> greatest = findGreatest(keys, weights);
        //double[][] treeWeights = new double[keys.size()][keys.size()];
        greatests = new Node[keys.size()][keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            for (int j = 0; j < keys.size(); j++) {
                if (keys.get(i).compareTo(keys.get(j)) <= 0) {
                    Greatest<E> greatest1 = findGreatest(keys.subList(i,j+1), weights.subList(i,j+1));
                    greatests[i][j] = new Node<>(greatest1.key);
                }
            }
        }
        parents = new HashMap<>();
        root = new Node<>(greatest.key);
        createNewTree(0, keys.size()-1, greatest.index, root, keys);
    }

    private void createNewTree(int start, int stop, int indexRoot, Node<E> root, List<E> keys) {
        if (start >= 0 && start < keys.size() && stop >= 0 && stop < keys.size() && indexRoot > 0
                && indexRoot < keys.size() - 1) {
            Node<E> left = greatests[start][indexRoot-1];
            Node<E> right = greatests[indexRoot+1][stop];
            if (! Objects.equals(left, null)) {
                root.setLeft(left);
                createNewTree(start, indexRoot-1, keys.indexOf(left.getValue()), left, keys);
            }
            if (! Objects.equals(right, null)) {
                root.setRight(right);
                createNewTree(indexRoot+1, stop, keys.indexOf(right.getValue()), right, keys);
            }
        }
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
