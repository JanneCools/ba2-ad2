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

    }@Test
    public void threeNodes() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();

        tree.add(7);
        tree.add(1);
        tree.add(9);

        //    7
        //   / \
        //  1   9
        assertEquals(7, tree.root().getValue());
        assertEquals(1, tree.root().getLeft().getValue());
        assertEquals(9, tree.root().getRight().getValue());
    }


    @Test
    public void noSplayWhenNotDeep() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();

        tree.add(7);
        tree.add(1);
        tree.add(9);

        // search with depth < 2 should not splay
        assertTrue(tree.search(9));

        //    7
        //   / \
        //  1   9
        assertEquals(7, tree.root().getValue());
        assertEquals(1, tree.root().getLeft().getValue());
        assertEquals(9, tree.root().getRight().getValue());
    }

    @Test
    public void fourNodesShouldSplay() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();

        tree.add(7);
        tree.add(1);
        tree.add(9);

        // first path of 3 nodes, should splay
        tree.add(3);

        //    3
        //   / \
        //  1   7
        //       \
        //        9
        assertEquals(3, tree.root().getValue());
        assertEquals(1, tree.root().getLeft().getValue());
        assertEquals(7, tree.root().getRight().getValue());
        assertEquals(9, tree.root().getRight().getRight().getValue());
    }

    @Test
    public void removeReplacesWithGreatestLeft() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();

        tree.add(7);
        tree.add(1);
        tree.add(9);
        tree.add(3);
        tree.add(8);

        // should replace with left child
        tree.remove(8);

        //    3
        //   / \
        //  1   7
        //       \
        //        9
        assertEquals(3, tree.root().getValue());
        assertEquals(1, tree.root().getLeft().getValue());
        assertEquals(7, tree.root().getRight().getValue(), "should replace with greatest left child when removing");
        assertEquals(9, tree.root().getRight().getRight().getValue());
    }

    @Test
    public void checkWithExample() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();

        for (int i: List.of(5, 7, 2, 6, 1, 3, 8, 4, 9, 0, 11, 15, 13, 12)) {
            tree.add(i);
        }

        assertEquals(6, tree.root().getValue());
        assertEquals(3, tree.root().getLeft().getValue());
        assertEquals(12, tree.root().getRight().getValue());
        assertEquals(1, tree.root().getLeft().getLeft().getValue());
        assertEquals(4, tree.root().getLeft().getRight().getValue());
        assertEquals(11, tree.root().getRight().getLeft().getValue());
        assertEquals(13, tree.root().getRight().getRight().getValue());
        assertEquals(0, tree.root().getLeft().getLeft().getLeft().getValue());
        assertEquals(2, tree.root().getLeft().getLeft().getRight().getValue());
        assertEquals(5, tree.root().getLeft().getRight().getRight().getValue());

        assertEquals(9, tree.root().getRight().getLeft().getLeft().getValue());
        assertEquals(8, tree.root().getRight().getLeft().getLeft().getLeft().getValue());
        assertEquals(7, tree.root().getRight().getLeft().getLeft().getLeft().getLeft().getValue());

        assertEquals(15, tree.root().getRight().getRight().getRight().getValue());
    }

    @Test
    public void removeShouldSplayFromReplacement() {
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        SortedSet<Integer> inserted = new TreeSet<>();
        Random rng = new Random(2021);
        for (int i = 0; i < 30; i++) {
            int key = rng.nextInt(100);
            tree.add(key);
            inserted.add(key);
        }

        // We should end up with the following tree:
        //
        //    58
        //   /  \
        //  ..   91
        //      /  \
        //    69   ..
        //   /  \
        //  ..   85
        //      /  \
        //    78    87
        //   /  \
        // 74    84
        //      /
        //     82

        assertEquals(58, tree.root().getValue());
        assertEquals(91, tree.root().getRight().getValue());
        assertEquals(69, tree.root().getRight().getLeft().getValue());

        // Key we will remove
        assertEquals(85, tree.root().getRight().getLeft().getRight().getValue());
        // Child of key we will remove
        assertEquals(78, tree.root().getRight().getLeft().getRight().getLeft().getValue());
        // Key with which removed key should be replaced
        assertEquals(84, tree.root().getRight().getLeft().getRight().getLeft().getRight().getValue());
        // Replacement has a child
        assertEquals(82, tree.root().getRight().getLeft().getRight().getLeft().getRight().getLeft().getValue());
        // Child has no more children
        assertNull(tree.root().getRight().getLeft().getRight().getLeft().getRight().getLeft().getLeft());
        assertNull(tree.root().getRight().getLeft().getRight().getLeft().getRight().getLeft().getRight());

        // Remove Node<85>
        assertTrue(tree.remove(85), "should return True when removing 85");

        // A few actions should happen:
        // - Node<85> should be replaced by Node<84>
        // - Since Node<84> had left child Node<82>, this will become the new right child of Node<78>
        // - Splay nodes 78, 84, 69
        // - Splay nodes 78, 91, 58
        //
        // Resulting tree:
        //
        //        78
        //       /   \
        //     58     91
        //    / \     / \
        //   51  69 84  96
        //   /\  /\ /\   /\
        //  ... ... ... ...


        assertEquals(78, tree.root().getValue());
        assertEquals(91, tree.root().getRight().getValue());
        assertEquals(58, tree.root().getLeft().getValue());


        assertEquals(51, tree.root().getLeft().getLeft().getValue());
        assertEquals(69, tree.root().getLeft().getRight().getValue());

        assertEquals(84, tree.root().getRight().getLeft().getValue());
        assertEquals(96, tree.root().getRight().getRight().getValue());

        inserted.remove(85);
        assertIterableEquals(inserted, tree, "Tree should still contain all elements except 85");
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
