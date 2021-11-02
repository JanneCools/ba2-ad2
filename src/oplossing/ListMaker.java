package oplossing;

import opgave.Node;

import java.util.ArrayList;

public class ListMaker<E extends Comparable<E>> {

    public ArrayList<E> makeList(ArrayList<E> list, Node<E> node) {
        if (node.getLeft() != null) {
            list = makeList(list, node.getLeft());
        }
        list.add(node.getValue());
        if (node.getRight() != null) {
            list = makeList(list, node.getRight());
        }
        return list;
    }
}
