package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.Iterator;

public class MyTree<E> implements SearchTree {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean search(Comparable o) {
        return false;
    }

    @Override
    public boolean add(Comparable o) {
        return false;
    }

    @Override
    public boolean remove(Comparable comparable) {
        return false;
    }

    @Override
    public Node root() {
        return null;
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
