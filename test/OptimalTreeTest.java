import opgave.Node;
import opgave.OptimizableTree;
import opgave.SearchTree;
import oplossing.OptimalTree;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OptimalTreeTest {

    double treeWeight(SearchTree<Integer> tree, List<Integer> keys, List<Double> internal, List<Double> external) {
    if (external == null) {
        external = new ArrayList<>();
        for (int i = 0; i < keys.size() + 1; i++) {
            external.add(0d);
        }
    }
    return nodeWeight(tree.root(), keys, internal, external, 1);
}

    double nodeWeight(Node<Integer> node, List<Integer> keys, List<Double> internal, List<Double> external, int depth) {

        int v = node.getValue();
        int i = keys.indexOf(v);
        assert i >= 0;

        // multiply the weight of the key with the current depth
        double w = depth * internal.get(i);

        if (node.getLeft() == null) {
            // we count the visited "nodes" as weight, including the NULL pointer
            w += (depth + 1) * external.get(i);
        } else {
            w += nodeWeight(node.getLeft(), keys, internal, external, depth + 1);
        }
        if (node.getRight() == null) {
            w += (depth + 1) * external.get(i + 1);
        } else {
            w += nodeWeight(node.getRight(), keys, internal, external, depth + 1);
        }
        return w;
    }

    @Test
    public void optimize() {
        OptimalTree<Integer> tree = new OptimalTree<>();
        ArrayList<Integer> keys = new ArrayList<>(List.of(10, 20, 30, 40));
        ArrayList<Double> weights = new ArrayList<>(List.of(4.0, 2.0, 6.0, 3.0));
        tree.optimize(keys,weights);
        assertEquals(30, tree.root().getValue());
        assertEquals(10, tree.root().getLeft().getValue());
        assertEquals(20, tree.root().getLeft().getRight().getValue());
        assertEquals(40, tree.root().getRight().getValue());
    }

    @Test
    void shouldThrowAwayExisting() {
        OptimalTree<Integer> tree = new OptimalTree<>();
        tree.add(1);
        tree.add(2);
        tree.add(3);
        tree.optimize(List.of(4), List.of(1d));

        Iterator<Integer> it = tree.iterator();
        assertEquals(4, it.next(), "optimize() should throw away any existing nodes");
        assertFalse(it.hasNext(), "optimize() should throw away any existing nodes");
    }

    @Test
    void singleItem() {
        OptimalTree<Integer> tree = new OptimalTree<>();
        List<Integer> keys = List.of(1);
        List<Double> weights = List.of(1d);
        tree.optimize(keys, weights);
        assertEquals(1, tree.root().getValue());
        assertNull(tree.root().getLeft());
        assertNull(tree.root().getRight());

        assertEquals(1d, treeWeight(tree, keys, weights, null));
    }

    @Test
    void twoAscending() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(1, 2);
        List<Double> weights = List.of(2d, 1d);

        tree.optimize(keys, weights);
        assertEquals(1, tree.root().getValue());
        assertEquals(2, tree.root().getRight().getValue());

        assertEquals(4d, treeWeight(tree, keys, weights, null));

        assertEquals(4d, treeWeight(tree, keys, weights, null));
    }

    @Test
    public void testRegularOperations() {
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
    }

    @Test
    public void empty() {
        OptimalTree<Integer> tree = new OptimalTree<>();
        assertEquals(0, tree.size());
    }

    @Test
    public void addOne() {
        OptimalTree<Integer> tree = new OptimalTree<>();

        assertFalse(tree.search(1));
        tree.add(1);
        assertTrue(tree.search(1));
    }

    @Test
    public void addMultiple() {
        OptimalTree<Integer> tree = new OptimalTree<>();

        for (int i = 0; i < 10; i++) {
            assertTrue(tree.add(i));
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(tree.search(i), String.format("should contain %d", i));
        }
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
