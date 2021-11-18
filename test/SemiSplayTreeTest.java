import org.junit.jupiter.api.Test;
import oplossing.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Dit is een voorbeeld van een test met Junit 5.
 * Je mag dit bestand verwijderen.
 */
public class SemiSplayTreeTest {

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
        assertTrue(tree.remove(50));
        assertEquals(32, tree.root().getValue());
        assertEquals(12, tree.root().getLeft().getValue());
        assertEquals(35, tree.root().getRight().getValue());
        assertNull(tree.root().getRight().getLeft());
        assertEquals(54, tree.root().getRight().getRight().getValue());

        //61 verwijderen
        assertTrue(tree.remove(61));
        assertEquals(32, tree.root().getValue());
        assertEquals(12, tree.root().getLeft().getValue());
        assertEquals(54, tree.root().getRight().getValue());
        assertEquals(55, tree.root().getRight().getRight().getValue());
        assertNull(tree.root().getLeft().getRight());
        assertEquals(52, tree.root().getRight().getLeft().getRight().getValue());
        assertFalse(tree.search(50));
        assertFalse(tree.search(61));

    }

    @Test
    public void removeLeaf() {
        SemiSplayTree<Integer> tree = randomTree();

        assertTrue(tree.remove(35));
        assertEquals(32, tree.root().getValue());
        assertEquals(12, tree.root().getLeft().getValue());
        assertNull(tree.root().getRight().getLeft());
        assertEquals(50, tree.root().getRight().getValue());
        assertFalse(tree.search(35));
    }

    @Test
    public void removeNodeWithoutLeftChild() {
        SemiSplayTree<Integer> tree = randomTree();

        assertTrue(tree.remove(32));
        assertEquals(35, tree.root().getValue());
        assertEquals(12, tree.root().getLeft().getValue());
        assertEquals(50, tree.root().getRight().getValue());
        assertNull(tree.root().getLeft().getRight());
        assertEquals(52, tree.root().getRight().getRight().getLeft().getValue());
    }

    @Test
    public List<Integer> toList(Iterable<Integer> iterable) {
        List<Integer> list = new ArrayList<>();
        for (Integer integer : iterable) {
            list.add(integer);
        }
        return list;
    }

    @Test
    public void empty() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        assertEquals(0, tree.size());
    }

    @Test
    public void addOne() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();

        assertFalse(tree.search(1));
        tree.add(1);
        assertTrue(tree.search(1));
    }

    @Test
    public void removeOne() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();

        assertFalse(tree.search(1));
        tree.add(1);
        assertTrue(tree.search(1));
        tree.remove(1);
        assertFalse(tree.search(1));
        assertEquals(0, tree.size());
    }

    @Test
    public void addMultiple() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();

        for (int i = 0; i < 10; i++) {
            assertTrue(tree.add(i));
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(tree.search(i), String.format("should contain %d", i));
        }
    }

    @Test
    public void removeMultiple() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();

        for (int i = 0; i < 10; i++) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(tree.search(i), String.format("should contain %d", i));
            assertTrue(tree.remove(i), String.format("should change when removing %d", i));
            assertFalse(tree.search(i), String.format("should not contain %d anymore", i));
        }
        assertEquals(0, tree.size(), "should be empty");
    }

    @Test
    public void compareWithTreeSet() {
        for (int seed = 1; seed < 10000; seed *= 10) {
            TreeSet<Integer> oracle = new TreeSet<>();
            Random random = new Random(seed);
            SemiSplayTree<Integer> tree = new SemiSplayTree<>();
            for (int i = 0; i < 1000; i++) {
                int x = random.nextInt(100);
                boolean add = random.nextFloat() < .9;
                if (add) {
                    assertEquals(oracle.add(x), tree.add(x),
                            String.format("Mismatch when comparing add() with TreeSet (parameters: seed=%d i=%d x=%d)", seed, i, x));
                } else {
                    assertEquals(oracle.remove(x), tree.remove(x),
                            String.format("Mismatch when comparing remove() with TreeSet (parameters: seed=%d i=%d x=%d)", seed, i, x));
                }
                for (int j = 0; j < 100; j++) {
                    assertEquals(oracle.contains(j), tree.search(j),
                            String.format("Mismatch when comparing search(%d) with TreeSet (parameters: seed=%d i=%d x=%d)", j, seed, i, x));
                }
                assertEquals(toList(oracle), toList(tree),
                        String.format("Mismatch when comparing iterator() with TreeSet (parameters: seed=%d i=%d x=%d)", seed, i, x));
            }
        }
    }
}
