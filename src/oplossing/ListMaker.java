package oplossing;

import opgave.Node;

import java.util.ArrayList;
import java.util.Objects;

public class ListMaker<E extends Comparable<E>> {

    public ArrayList<E> makeList(ArrayList<E> list, Node<E> node) {
        if (!Objects.equals(node.getLeft(), null)) {
            list = makeList(list, node.getLeft());
        }
        list.add(node.getValue());
        if (!Objects.equals(node.getRight(), null)) {
            list = makeList(list, node.getRight());
        }
        return list;
    }
}
