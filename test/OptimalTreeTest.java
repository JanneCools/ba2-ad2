import oplossing.OptimalTree;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
