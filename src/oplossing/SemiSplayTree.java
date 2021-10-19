package oplossing;

import jdk.swing.interop.SwingInterOpUtils;
import opgave.Node;
import opgave.SearchTree;
import opgave.samplers.Sampler;
import java.util.*;

public class SemiSplayTree<E extends Comparable<E>> implements SearchTree {

    private Node<E> root;
    private HashMap<Node<E>, Node<E>> parents;

    public SemiSplayTree() {
        root = null;
        parents = new HashMap<>();
    }

    public static class NodeSearcher<E extends Comparable<E>> {
        public boolean found;
        public Node<E> node;
    }

    // De boolean "semi_splay" zorgt ervoor dat semi-splay al dan niet al wordt uitgevoerd.
    // Dit is enkel het geval als de methode "search" werd opgeroepen.
    private NodeSearcher<E> searchNode(Comparable e, boolean semi_splay) {
        NodeSearcher<E> s = new NodeSearcher<>();
        s.node = null;
        s.found = false;
        Node<E> child = root;
        while (child != null && !s.found) {
            E value = child.getValue();
            s.found = e.equals(value);
            s.node = child;
            if (e.compareTo(value) < 0) {
                child = child.getLeft();
            } else {
                child = child.getRight();
            }
        }
        if (semi_splay) {
            semiSplay(s.node);
        }
        return s;
    }

    private Node<E> findGreatestChild(Node<E> e) {
        Node<E> child = e.getLeft();
        if (child == null) {
            return findSmallestChild(e);
        }
        while (child.getRight() != null) {
            child = child.getRight();
        }
        return child;
    }

    // Hierbij wordt de kleinste sleutel uit de rechterdeelboom gezocht (enkel als de node geen linkerdeelboom bevat).
    private Node<E> findSmallestChild(Node<E> e) {
        Node<E> child = e.getRight();
        if (child == null) {
            return null;
        }
        while (child.getLeft() != null) {
            child = child.getLeft();
        }
        return child;
    }

    // Node, parent1 en parent2 zijn de 3 toppen waarop semi-splay wordt toegepast
    private void semiSplay(Node<E> node) {
        Node<E> parent1 = parents.get(node);
        while (node != root && parent1 != root) {
            //De derde top voor semi-splay zoeken en zijn ouder
            Node<E> parent2 = parents.get(parent1);
            Node<E> parent3 = null;
            if (! parent2.equals(root)) {
                parent3 = parents.get(parent2);
            }
            // De 3 toppen rangschikken volgens waarde zodat je ze makkelijk in de juiste variabele kan plaatsen
            ArrayList<Node<E>> list = new ArrayList<>(List.of(node, parent1, parent2));
            list.sort((n1,n2) -> n1.getValue().compareTo(n2.getValue()));
            Node<E> leftChild = list.get(0);
            Node<E> parent = list.get(1);
            Node<E> rightChild = list.get(2);

            // Als de hoogste top van de semi-splay (parent2) de wortel is, dan wordt de middelste waarde (parent) nu de wortel
            if (parent2 == root) {
                root = parent;
            }

            // Alle kinderen van de 3 toppen in een lijst zetten en rangschikken volgens stijgende waarde
            ArrayList<Node<E>> allChildren = new ArrayList<>();
            allChildren.add(leftChild.getLeft());
            allChildren.add(leftChild.getRight());
            allChildren.add(parent.getRight());
            allChildren.add(parent.getLeft());
            allChildren.add(rightChild.getLeft());
            allChildren.add(rightChild.getRight());
            allChildren.removeIf(Objects::isNull);
            allChildren.removeIf(e -> e.equals(leftChild) || e.equals(parent) || e.equals(rightChild));
            allChildren.sort((n1,n2) -> n1.getValue().compareTo(n2.getValue()));

            // De middelste top wordt het (nieuwe) kind van parent3
            if (parent3 != null) {                          // Dit betekent dat parent2 niet de wortel is
                if (parent2.equals(parent3.getRight())) {
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
            while (! allChildren.isEmpty()) {
                Node<E> child = allChildren.get(0);
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
                allChildren.remove(0);
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
        NodeSearcher<E> searcher = searchNode(e, true);
        return searcher.found;
    }

    @Override
    public boolean add(Comparable e) {
        if (root == null) {
            root = new Node<>(e);
            parents.put(root, null);
        }
        boolean present = searchNode(e, false).found;
        if (! present) {
            Node<E> parent = null;
            Node<E> node = root;
            while (node != null) {
                E value = node.getValue();
                parent = node;
                if (e.compareTo(value) < 0) {
                    node = node.getLeft();
                } else {
                    node = node.getRight();
                }
            }
            node = new Node<>(e);
            if (e.compareTo(parent.getValue()) < 0) {
                parent.setLeft(node);
            } else {
                parent.setRight(node);
            }
            parents.put(node, parent);
            semiSplay(node);
        }
        return present;
    }

    @Override
    public boolean remove(Comparable e) {
        NodeSearcher<E> s = searchNode(e, false);
        if (s.found) {
            Node<E> parent = parents.get(s.node);
            Node<E> greatestChild = findGreatestChild(s.node);
            Node<E> lastNodeOfPath = parents.get(greatestChild); //Te gebruiken voor semi-splay
            if (lastNodeOfPath.equals(s.node)) {
                lastNodeOfPath = greatestChild;
            }
            if (s.node.equals(root)) {
                root = greatestChild;
                parents.replace(greatestChild, null);
            } else {
                if (greatestChild != null) {
                    if (s.node.getValue().compareTo(parent.getValue()) < 0) {
                        parent.setLeft(greatestChild);
                    } else {
                        parent.setRight(greatestChild);
                    }
                    parents.replace(greatestChild, parent);
                } else {
                    greatestChild = parent;
                }
            }

            // De top die de verwijderde top vervangt ("greatestChild") krijgt de kinderen van de verwijderde top
            // en de ouder deze kinderen wordt vervangen in de hashmap "parents"
            if (! s.node.getLeft().equals(greatestChild)) {
                greatestChild.setLeft(s.node.getLeft());
                parents.replace(s.node.getLeft(), greatestChild);
            }
            if (! s.node.getRight().equals(greatestChild)) {
                greatestChild.setRight(s.node.getRight());
                parents.replace(s.node.getRight(), greatestChild);
            }
            parents.remove(s.node);
            semiSplay(lastNodeOfPath);
        }
        return s.found;
    }

    @Override
    public Node root() {
        return root;
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}

