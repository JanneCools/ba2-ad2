import opgave.Node;
import oplossing.MyTree;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTreeTest {

    @Test
    public void randomTree() {
        MyTree<Integer> tree = new MyTree<>();
        ArrayList<Integer> listToAdd = new ArrayList<>(List.of(55, 50, 61, 32, 54, 87, 35, 12, 52, 6));
        for (Integer integer: listToAdd) {
            tree.add(integer);
        }
        assertEquals(54, tree.root().getValue());
        assertEquals(35, tree.root().getLeft().getValue());
        assertEquals(61, tree.root().getRight().getValue());
        assertEquals(12, tree.root().getLeft().getLeft().getValue());
        assertEquals(50, tree.root().getLeft().getRight().getValue());
        assertEquals(87, tree.root().getRight().getRight().getValue());
        assertEquals(55, tree.root().getRight().getLeft().getValue());
        assertEquals(32, tree.root().getLeft().getLeft().getRight().getValue());
        assertEquals(6, tree.root().getLeft().getLeft().getLeft().getValue());
        assertEquals(52, tree.root().getLeft().getRight().getRight().getValue());
        assertEquals(10, tree.size());
    }

    @Test
    public void worstCase() {
        MyTree<Integer> tree = new MyTree<>();
        for (int i = 0; i < 1000; i++) {
            assertTrue(tree.add(i));
        }
    }

    @Test
    public void empty() {
        MyTree<Integer> tree = new MyTree<>();
        assertEquals(0, tree.size());
    }

    @Test
    public void addOne() {
        MyTree<Integer> tree = new MyTree<>();

        assertFalse(tree.search(1));
        tree.add(1);
        assertTrue(tree.search(1));
    }

    @Test
    public void addMultiple() {
        MyTree<Integer> tree = new MyTree<>();

        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            assertTrue(tree.add(i));
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(tree.search(i), String.format("should contain %d", i));
        }
    }

    @Test
    public void removeOne() {
        MyTree<Integer> tree = new MyTree<>();

        assertFalse(tree.search(1));
        tree.add(1);
        assertTrue(tree.search(1));
        tree.remove(1);
        assertFalse(tree.search(1));
        assertEquals(0, tree.size());
    }

    @Test
    public void removeMultiple() {
        MyTree<Integer> tree = new MyTree<>();

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
            MyTree<Integer> tree = new MyTree<>();
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
