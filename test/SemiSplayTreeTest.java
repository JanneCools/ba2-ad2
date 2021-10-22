import opgave.Node;
import org.junit.jupiter.api.Test;
import oplossing.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Dit is een voorbeeld van een test met Junit 5.
 * Je mag dit bestand verwijderen.
 */
public class SemiSplayTreeTest {

    //Achtereenvolgens de getallen 1 tot en met 10 toevoegen + 5 opzoeken en de semi-splay nakijken +
    // 5 verwijderen en semi-splay nakijken.
    @Test
    public void testOneToTen() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        for (int i = 1; i <= 10; i++) {
            tree.add(i);
        }
        assertEquals(9, tree.root().getValue(), "Wrong root");
        assertEquals(10, tree.root().getRight().getValue(), "Wrong right child of root");
        assertEquals(true, tree.search(5));
        assertEquals(8, tree.root().getValue(), "Wrong root after searching 5 and applying semi-splay");
        assertEquals(9, tree.root().getRight().getValue());
        assertEquals(6, tree.root().getLeft().getValue());
        assertEquals(7, tree.root().getLeft().getRight().getValue());
        assertEquals(4, tree.root().getLeft().getLeft().getLeft().getValue());
        assertEquals(true, tree.remove(5));
        assertEquals(6, tree.root().getValue());
        assertEquals(7, tree.root().getRight().getLeft().getValue());
        assertEquals(4, tree.root().getLeft().getValue());
    }

    @Test
    public SemiSplayTree<Integer> randomTree() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        ArrayList<Integer> listToAdd = new ArrayList<>(List.of(55, 50, 61, 32, 54, 87, 35, 12, 52, 6));
        for (Integer integer: listToAdd) {
            tree.add(integer);
        }
        assertEquals(50, tree.root().getValue());
        assertEquals(12, tree.root().getLeft().getValue());
        assertEquals(54, tree.root().getRight().getValue());
        assertEquals(6, tree.root().getLeft().getLeft().getValue());
        assertEquals(32, tree.root().getLeft().getRight().getValue());
        assertEquals(52, tree.root().getRight().getLeft().getValue());
        assertEquals(61, tree.root().getRight().getRight().getValue());
        assertEquals(35, tree.root().getLeft().getRight().getRight().getValue());
        assertEquals(55, tree.root().getRight().getRight().getLeft().getValue());
        assertEquals(87, tree.root().getRight().getRight().getRight().getValue());
        assertEquals(10, tree.size());
        return tree;
    }

    @Test
    public void removeRootAnd61() {
        SemiSplayTree<Integer> tree = randomTree();

        //50 verwijderen
        assertEquals(true, tree.remove(50));
        assertEquals(32, tree.root().getValue());
        assertEquals(12, tree.root().getLeft().getValue());
        assertEquals(35, tree.root().getRight().getValue());
        assertEquals(null, tree.root().getRight().getLeft());
        assertEquals(54, tree.root().getRight().getRight().getValue());

        //61 verwijderen
        assertEquals(true, tree.remove(61));
        assertEquals(32, tree.root().getValue());
        assertEquals(12, tree.root().getLeft().getValue());
        assertEquals(54, tree.root().getRight().getValue());
        assertEquals(55, tree.root().getRight().getRight().getValue());
        assertEquals(null, tree.root().getLeft().getRight());
        assertEquals(52, tree.root().getRight().getLeft().getRight().getValue());
        assertEquals(false, tree.search(50));
        assertEquals(false, tree.search(61));

    }

    @Test
    public void removeLeaf() {
        SemiSplayTree<Integer> tree = randomTree();

        assertEquals(true, tree.remove(35));
        assertEquals(32, tree.root().getValue());
        assertEquals(12, tree.root().getLeft().getValue());
        assertEquals(null, tree.root().getRight().getLeft());
        assertEquals(50, tree.root().getRight().getValue());
        assertEquals(false, tree.search(35));
    }

    @Test
    public void removeNodeWithoutLeftChildren() {
        SemiSplayTree<Integer> tree = randomTree();

        assertEquals(true, tree.remove(32));
        assertEquals(35, tree.root().getValue());
        assertEquals(null, tree.root().getLeft().getRight());
        assertEquals(50, tree.root().getRight().getValue());
        assertEquals(55, tree.root().getRight().getRight().getRight().getLeft().getValue());
        assertEquals(false, tree.search(32));

    }

    @Test
    public void iterator() {
        SemiSplayTree<Integer> tree = randomTree();
        Iterator<Node<Integer>> iterator = tree.iterator();
        ArrayList<Integer> list = new ArrayList<>(List.of(50, 12, 54, 6, 32, 52, 61, 35, 55, 87));
        int index = 0;
        while (iterator.hasNext()) {
            int value = iterator.next().getValue();
            assertEquals(list.get(index), value, value + " moet eigenlijk " + list.get(index) + " zijn");
            index ++;
        }
    }
}
