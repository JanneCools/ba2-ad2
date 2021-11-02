package oplossing;

import opgave.Node;
import opgave.SearchTree;
import java.util.*;

public class SemiSplayTree<E extends Comparable<E>> implements SearchTree<E> {

    private Node<E> root;
    private HashMap<Node<E>, Node<E>> parents;
    private final NodeSearcher<E> searcher;
    private final ListMaker<E> listMaker;

    public SemiSplayTree() {
        root = null;
        parents = new HashMap<>();
        searcher = new NodeSearcher<>();
        listMaker = new ListMaker<>();
    }

    // Node, parent1 en parent2 zijn de 3 toppen waarop semi-splay wordt toegepast
    public void semiSplay(Node<E> node) {
        Node<E> parent1 = parents.get(node);
        while (!(Objects.equals(node, null) || Objects.equals(node, root) || Objects.equals(parent1, root))) {
            //De derde top voor semi-splay zoeken en zijn ouder
            Node<E> parent2 = parents.get(parent1);
            Node<E> parent3 = null;
            if (! Objects.equals(parent2, root)) {
                parent3 = parents.get(parent2);
            }
            // De 3 toppen rangschikken volgens waarde zodat je ze makkelijk in de juiste variabele kan plaatsen
            ArrayList<Node<E>> list = new ArrayList<>(List.of(node, parent1, parent2));
            list.sort(Comparator.comparing(Node::getValue));
            Node<E> leftChild = list.get(0);
            Node<E> parent = list.get(1);
            Node<E> rightChild = list.get(2);

            // Als de hoogste top van de semi-splay (parent2) de wortel is, dan wordt de middelste waarde (parent) nu de wortel
            if (parent2 == root) {
                root = parent;
            }

            // Alle kinderen van de 3 toppen in een lijst zetten en rangschikken volgens stijgende waarde
            Set<Node<E>> allChildren = new HashSet<>();
            allChildren.add(leftChild.getLeft());
            allChildren.add(leftChild.getRight());
            allChildren.add(parent.getRight());
            allChildren.add(parent.getLeft());
            allChildren.add(rightChild.getLeft());
            allChildren.add(rightChild.getRight());
            allChildren.removeIf(Objects::isNull);
            allChildren.removeIf(e -> e.equals(leftChild) || e.equals(parent) || e.equals(rightChild));

            // De middelste top wordt het (nieuwe) kind van parent3
            if (parent3 != null) {                          // Dit betekent dat parent2 niet de wortel is
                if (Objects.equals(parent2, parent3.getRight())) {
                    parent3.setRight(parent);
                } else {
                    parent3.setLeft(parent);
                }
            }
            parents.replace(parent, parent3);

            // De linker- en rechtertop als kinderen toevoegen aan de middelste top (parent)
            parent.setLeft(leftChild);
            parent.setRight(rightChild);
            parents.replace(leftChild, parent);
            parents.replace(rightChild, parent);

            // De kinderen van leftChild en rightChild verwijderen omdat later andere kinderen worden toegevoegd
            leftChild.setLeft(null);
            leftChild.setRight(null);
            rightChild.setLeft(null);
            rightChild.setRight(null);

            // Alle kinderen van de toppen, die semi-splay ondergaan, krijgen hun nieuwe ouder
            for (Node<E> child : allChildren) {
                Node<E> newParent = leftChild;
                if (child.getValue().compareTo(parent.getValue()) > 0) {
                    newParent = rightChild;
                }
                if (child.getValue().compareTo(newParent.getValue()) < 0) {
                    newParent.setLeft(child);
                } else {
                    newParent.setRight(child);
                }
                parents.replace(child, newParent);
            }

            // 3 nieuwe toppen kiezen die (misschien) semi-splay zullen ondergaan
            node = parent;
            parent1 = parents.get(node);
        }
    }


    @Override
    public int size() {
        return parents.size();
    }

    @Override
    public boolean search(Comparable e) {
        searcher.searchNode(e, root, true, this);
        return searcher.isFound();
    }

    @Override
    public boolean add(Comparable e) {
        if (root == null) {
            root = new Node<>(e);
            parents.put(root, null);
            return true;
        }
        boolean present = searcher.searchNode(e, root, false, null).isFound();
        if (! present) {
            Node<E> parent = searcher.getNode();
            Node<E> newNode = new Node<>(e);
            if (e.compareTo(parent.getValue()) < 0) {
                parent.setLeft(newNode);
            } else {
                parent.setRight(newNode);
            }
            parents.put(newNode, parent);
            semiSplay(newNode);
        }
        return ! present;
    }

    @Override
    public boolean remove(Comparable e) {
        searcher.searchNode(e, root, false, null);
        if (searcher.isFound()) {
            Node<E> node = searcher.getNode();
            Node<E> greatestChild = searcher.findGreatestChild(node);
            Node<E> lastNodeOfPath = parents.get(greatestChild);        // Te gebruiken voor semi-splay
            if (greatestChild == null && node.getRight() == null) {     // De te verwijderen top is een blad
                Node<E> parent = parents.get(node);
                if (Objects.equals(node, root)) {
                    root = null;
                } else if (Objects.equals(node, parent.getLeft())) {
                    parent.setLeft(null);
                } else {
                    parent.setRight(null);
                }
                lastNodeOfPath = parents.get(node);
                parents.remove(node);
            } else if (greatestChild == null) {
                Node<E> child = node.getRight();
                Node<E> parent = parents.get(node);
                if (Objects.equals(node, root)) {
                    root = child;
                } else if (Objects.equals(parent.getRight(), node)) {
                    parent.setRight(child);
                } else {
                    parent.setLeft(child);
                }
                parents.replace(child, parent);
                lastNodeOfPath = parents.get(child);
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
            semiSplay(lastNodeOfPath);
        } else {
            semiSplay(searcher.getNode());
        }
        return searcher.isFound();
    }

    @Override
    public Node<E> root() {
        return root;
    }

    @Override
    public Iterator iterator() {
        ArrayList<E> list = listMaker.makeList(new ArrayList<>(), root);
        return list.listIterator();
    }

}

