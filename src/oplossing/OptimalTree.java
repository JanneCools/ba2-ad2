package oplossing;

import opgave.Node;
import opgave.OptimizableTree;
import java.util.*;

public class OptimalTree<E extends Comparable<E>> implements OptimizableTree<E> {

    private Node<E> root;
    private HashMap<Node<E>, Node<E>> parents;
    private final NodeSearcher<E> searcher;
    private final ListMaker<E> listMaker;

    public OptimalTree() {
        root = null;
        parents = new HashMap<>();
        searcher = new NodeSearcher<>();
        listMaker = new ListMaker<>();
    }

    private void restructure(List<E> keys, int[][] roots, int start, int stop, Node<E> parent) {
        int index = roots[start][stop];
        if (Objects.equals(parent, null)) {
            root = new Node<>(keys.get(index-1));
            parents.put(root, null);
            restructure(keys, roots, start, index-1, root);
            restructure(keys, roots, index, stop, root);
        } else if (start != stop) {
            Node<E> node = new Node<>(keys.get(index-1));
            if (node.getValue().compareTo(parent.getValue()) < 0) {
                parent.setLeft(node);
            } else {
                parent.setRight(node);
            }
            parents.put(node, parent);
            restructure(keys, roots, start, index-1, node);
            restructure(keys, roots, index, stop, node);
        }
    }

    // Voor deze methode (en de methode "restructure") heb ik het filmpje gebruikt op de link:
    // https://www.youtube.com/watch?v=vLS-zRCHo-Y&list=PLPKF_TcxpTJ40Ez42V8DIqm9lECUhb7MB&index=2&t=1264s
    @Override
    public void optimize(List<E> keys, List<Double> weights) {
        int amount = keys.size();
        double[][] costs = new double[amount+1][amount+1];
        int[][] roots = new int[amount+1][amount+1];
        for (int i = 0; i <= amount; i++) {
            costs[i][i] = 0;
        }
        for (int length = 2; length <= amount+1; length++) {
            for (int start = 0; start <= amount+1-length; start++) {
                int stop = start + length - 1;
                double weight = 0.0;
                for (int i = start+1; i <= stop; i++) {
                    if (i != 0) {
                        weight += weights.get(i-1);
                    }
                }
                double minWeight = costs[0][start] + costs[start+1][stop] + weight;
                roots[start][stop] = start+1;
                for (int index = start+1; index <= stop; index++) {
                    double tempWeight = costs[start][index-1] + costs[index][stop] + weight;
                    if (tempWeight < minWeight) {
                        minWeight = tempWeight;
                        roots[start][stop] = index;
                    }
                }
                costs[start][stop] = minWeight;
            }
        }
        root = null;
        parents = new HashMap<>();
        restructure(keys, roots, 0, amount, null);
    }

    @Override
    public void optimize(List<E> keys, List<Double> internalWeights, List<Double> externalWeights) {
        int amount = keys.size();
        double[][] costs = new double[amount+1][amount+1];
        int[][] roots = new int[amount+1][amount+1];
        for (int i = 0; i <= amount; i++) {
            costs[i][i] = 0;
        }
        for (int length = 2; length <= amount+1; length++) {
            for (int start = 0; start <= amount+1-length; start++) {
                int stop = start + length - 1;
                double weight = 0.0;
                for (int i = start+1; i <= stop; i++) {
                    if (i != 0) {
                        weight += internalWeights.get(i-1);
                        weight += externalWeights.get(i-1);
                    }
                }
                weight += externalWeights.get(stop);
                double minWeight = costs[0][start] + costs[start+1][stop] + weight;
                roots[start][stop] = start+1;
                for (int index = start+1; index <= stop; index++) {
                    double tempWeight = costs[start][index-1] + costs[index][stop] + weight;
                    if (tempWeight < minWeight) {
                        minWeight = tempWeight;
                        roots[start][stop] = index;
                    }
                }
                costs[start][stop] = minWeight;
            }
        }
        root = null;
        parents = new HashMap<>();
        restructure(keys, roots, 0, amount, null);
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
