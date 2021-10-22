import opgave.Node;
import oplossing.OptimalTree;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OptimalTreeTest {

    @Test
    public OptimalTree<Integer> randomTree() {
        OptimalTree<Integer> tree = new OptimalTree<>();
        ArrayList<Integer> listToAdd = new ArrayList<>(List.of(50, 12, 54, 6, 32, 52, 61, 35, 55, 87));
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
    public void testRemoveRoot() {
        OptimalTree<Integer> tree = randomTree();
        assertEquals(true, tree.remove(50));
        assertEquals(35, tree.root().getValue());
    }

    @Test
    public void removeOne() {
        OptimalTree<Integer> tree = new OptimalTree<>();

        assertFalse(tree.search(1));
        tree.add(1);
        assertTrue(tree.search(1));
        tree.remove(1);
        assertFalse(tree.search(1));
        assertEquals(0, tree.size());
    }

    @Test
    public void removeMultiple() {
        OptimalTree<Integer> tree = new OptimalTree<>();

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
            OptimalTree<Integer> tree = new OptimalTree<>();
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

    public List<Integer> toList(Iterable<Integer> iterable) {
        List<Integer> list = new ArrayList<>();
        for (Integer integer : iterable) {
            list.add(integer);
        }
        return list;
    }
}
