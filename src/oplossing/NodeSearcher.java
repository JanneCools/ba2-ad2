package oplossing;

import opgave.Node;

public class NodeSearcher<E extends Comparable<E>> {

    // Deze klasse gaat telkens een top zoeken in de boom

    private boolean found;
    private Node<E> node;
    private int nodesVisited;

    public boolean isFound() {
        return found;
    }

    public Node<E> getNode() {
        return node;
    }

    public int getNodesVisited() {
        return nodesVisited;
    }

    /*
     Deze methode staat in een aparte klasse omdat het door alle boom-klassen gebruikt wordt.
     De boolean "semi_splay" zorgt ervoor dat semi-splay al dan niet al wordt uitgevoerd bij de semi-splay boom.
     Dit is enkel het geval als een semi-splay boom deze methode oproept en hierin de methode "search" werd opgeroepen.
     Als de gezocht top niet gevonden is, wordt de laatste top, dat bezocht is, teruggegeven
     (dus in principe de ouder van deze top).
    */
    public NodeSearcher<E> searchNode(Comparable e, Node<E> root, boolean semi_splay, SemiSplayTree<E> tree) {
        node = null;
        found = false;
        Node<E> child = root;
        nodesVisited = 1;
        while (child != null && !found) {
            E value = child.getValue();
            found = e.equals(value);
            node = child;
            if (e.compareTo(value) < 0) {
                child = child.getLeft();
            } else {
                child = child.getRight();
            }
            nodesVisited ++;
        }
        if (semi_splay) {
            tree.semiSplay(node);
        }
        return this;
    }

    // Grootste linkerkind zoeken
    public Node<E> findGreatestChild(Node<E> node) {
        nodesVisited = 0;
        Node<E> child = node.getLeft();
        if (child == null) {
            return null;
        }
        nodesVisited = 1;
        while (child.getRight() != null) {
            child = child.getRight();
            nodesVisited ++;
        }
        return child;
    }

    // Kleinste rechterkind zoeken
    public Node<E> findSmallestChild(Node<E> node) {
        nodesVisited = 0;
        Node<E> child = node.getRight();
        if (child == null) {
            return null;
        }
        nodesVisited = 1;
        while (child.getLeft() != null) {
            child = child.getLeft();
            nodesVisited ++;
        }
        return child;
    }
}
