package oplossing;

import jdk.swing.interop.SwingInterOpUtils;
import opgave.Node;
import opgave.SearchTree;

import java.lang.reflect.Array;
import java.util.*;

public class MyTree<E extends Comparable<E>> implements SearchTree {

    private Node<E> root;
    public HashMap<Node<E>, Node<E>> parents;
    private final NodeSearcher<E> searcher;
    private final ListMaker<E> listMaker;
    private int nodesVisited;   // wordt gebruikt voor de benchmarks om het aantal bezochte toppen te berekenen

    public MyTree() {
        root = null;
        parents = new HashMap<>();
        searcher = new NodeSearcher<>();
        listMaker = new ListMaker<>();
        nodesVisited = 0;
    }

    public int getNodesVisited() {
        return nodesVisited;
    }

    public void setNodesVisited(int nodesVisited) {
        this.nodesVisited = nodesVisited;
    }

    public int[] getDepthAndNodes(Node<E> node) {
        int leftDepth = -1;
        int rightDepth = -1;
        int leftNodes = 0;
        int rightNodes = 0;
        if (! Objects.equals(node, null)) {
            if (!Objects.equals(node.getLeft(), null)) {
                int[] left = getDepthAndNodes(node.getLeft());
                leftDepth = left[0];
                leftNodes = left[1];
            }
            if (!Objects.equals(node.getRight(), null)) {
                int[] right = getDepthAndNodes(node.getRight());
                rightDepth = right[0];
                rightNodes = right[1] ;
            }
        }
        return new int[] {Math.max(leftDepth, rightDepth)+1, leftNodes+rightNodes+1};
    }

    private Node<E> rebalance(Node<E> node) {
        // Een kopie van de node maken (zonder kinderen) om deze later te kunnen toevoegen in de boom
        Node<E> copy = new Node<>(node.getValue());
        Node<E> replacement;
        boolean greatest;
        int[] left = getDepthAndNodes(node.getLeft());
        int[] right = getDepthAndNodes(node.getRight());
        if (left[1] > right[1]) {
            replacement = searcher.findGreatestChild(node);
            greatest = true;
        } else if (left[1] < right[1]) {
            replacement = searcher.findSmallestChild(node);
            greatest = false;
        } else {
            if (left[0] > Math.log(left[1]) / Math.log(2)) {
                replacement = searcher.findGreatestChild(node);
                greatest = true;
            } else {
                replacement = searcher.findSmallestChild(node);
                greatest = false;
            }
        }
        nodesVisited += searcher.getNodesVisited();
        node.setValue(replacement.getValue());
        Node<E> parent = parents.get(replacement);
        if (Objects.equals(parent.getRight(), replacement)) {
            if (greatest) {
                parent.setRight(replacement.getLeft());
                parents.replace(replacement.getLeft(), parent);
            } else {
                parent.setRight(replacement.getRight());
                parents.replace(replacement.getRight(), parent);
            }
        } else {
            if (greatest) {
                parent.setLeft(replacement.getLeft());
                parents.replace(replacement.getLeft(), parent);
            } else {
                parent.setLeft(replacement.getRight());
                parents.replace(replacement.getRight(), parent);
            }
        }
        parents.remove(replacement);
        // De node "copy" terug toevoegen door de juiste plaats te zoeken vanaf de top "node" (die van waarde is veranderd).
        Node<E> newParent = node;
        boolean smaller = copy.getValue().compareTo(newParent.getValue()) < 0;
        while ((smaller && !Objects.equals(newParent.getLeft(), null))
                || (!smaller && !Objects.equals(newParent.getRight(), null))) {
            if (smaller) {
                newParent = newParent.getLeft();
            } else {
                newParent = newParent.getRight();
            }
            smaller = copy.getValue().compareTo(newParent.getValue()) < 0;
            nodesVisited ++;
        }
        if (smaller) {
            newParent.setLeft(copy);
        } else {
            newParent.setRight(copy);
        }
        parents.put(copy, newParent);
        return copy;
    }

    @Override
    public int size() {
        return parents.size();
    }

    @Override
    public boolean search(Comparable e) {
        searcher.searchNode(e, root, false, null);
        nodesVisited += searcher.getNodesVisited();
        return searcher.isFound();
    }

    @Override
    public boolean add(Comparable e) {
        if (Objects.equals(root, null)) {
            root = new Node<>(e);
            parents.put(root, null);
            nodesVisited ++;
            return true;
        }
        boolean present = searcher.searchNode(e, root, false, null).isFound();
        if (! present) {
            nodesVisited += searcher.getNodesVisited();
            Node<E> parent = searcher.getNode();
            Node<E> newNode = new Node<>(e);
            if (e.compareTo(parent.getValue()) < 0) {
                parent.setLeft(newNode);
            } else {
                parent.setRight(newNode);
            }
            parents.put(newNode, parent);

            // Voor alle voorouders van de toegevoegde top bereken ik de diepte en het aantal toppen in die deelboom.
            // Als de diepte groter is dan log n (met n het aantal toppen) dan herbalanceer ik de boom.
            Node<E> node = parent;
            while (! Objects.equals(node, null)) {
                int[] depthNodes = getDepthAndNodes(node);
                int depth = depthNodes[0];
                int nodes = depthNodes[1];
                if (depth > Math.log(nodes) / Math.log(2)) {
                    node = rebalance(node);
                } else {
                    node = parents.get(node);
                }
            }
        }
        return ! present;
    }

    @Override
    public boolean remove(Comparable e) {
        searcher.searchNode(e, root, false, null);
        nodesVisited += searcher.getNodesVisited();
        if (searcher.isFound()) {
            Node<E> node = searcher.getNode();
            Node<E> greatestChild = searcher.findGreatestChild(node);
            if (greatestChild == null && node.getRight() == null) {     // De te verwijderen top is een blad
                Node<E> parent = parents.get(node);
                nodesVisited ++;
                if (Objects.equals(node, root)) {
                    root = null;
                } else if (Objects.equals(node, parent.getLeft())) {
                    parent.setLeft(null);
                    nodesVisited ++;
                } else {
                    parent.setRight(null);
                    nodesVisited ++;
                }
                parents.remove(node);
            } else if (greatestChild == null) {         // De top heeft geen linkerkinderen
                Node<E> child = node.getRight();
                Node<E> parent = parents.get(node);
                nodesVisited += 2;
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
                nodesVisited ++;
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
        nodesVisited ++;
        return root;
    }

    @Override
    public Iterator iterator() {
        ArrayList<E> list = listMaker.makeList(new ArrayList<>(), root);
        return list.listIterator();
    }
}
