package oplossing;

import opgave.Node;

import java.util.ArrayList;
import java.util.Objects;

public class IteratorFactory<E extends Comparable<E>> {

    // Deze klasse wordt door elke boomklasse gebruikt om een lijst te krijgen van alle toppen in de juiste volgorde
    // zodat deze gebruikt kan worden bij de methode "iterator"

    public ArrayList<Node<E>> makeList(Node<E> root, int height) {
        ArrayList<Node<E>> list = new ArrayList<>();
        /*
         Ik veronderstel dat het een links-complete binaire boom is, omdat ik dan gebruik kan maken van
         de impliciete representatie van de boom met een arraylist (zie p189 in de cursus van AD1)
         Als de hoogte gelijk is aan height, dan zijn er maximaal 2^(height+1) - 1 toppen
         Hier zorg ik ervoor dat dit aantal plaatsen al worden gemaakt in de lijst.
        */
        for (int i = 0; i <= Math.pow(2, height+1)-1; i++) {
            list.add(null);
        }
        list.set(1, root);
        list = addChildren(list, root, 1);

        // In het begin heb ik verondersteld dat de boom links-compleet is, maar dit is niet altijd het geval.
        // Daarom verwijder ik alle objecten die nog null zijn.
        list.removeIf(Objects::isNull);
        return list;
    }

    // Voor deze methode heb ik mij deels gebaseerd op de pseudocode op de site
    // https://www.baeldung.com/cs/binary-tree-height
    public int getHeight(Node<E> parent, String side) {
        if (side.equals("left")) {
            if (parent.getLeft() == null) {
                return 0;
            }
            int leftHeight = getHeight(parent.getLeft(), "left");
            int rightHeight = getHeight(parent.getLeft(), "right");
            return Math.max(leftHeight, rightHeight) + 1;
        } else {
            if (parent.getRight() == null) {
                return 0;
            }
            int leftHeight = getHeight(parent.getRight(), "left");
            int rightHeight = getHeight(parent.getRight(), "right");
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    // Hier voeg ik telkens de kinderen van de gegeven node toe aan de lijst op index "index*2" en "index*2+1",
    // dit is gebaseerd op de impliciete representatie van een links-complete binaire boom (p189 cursus AD1)
    private ArrayList<Node<E>> addChildren(ArrayList<Node<E>> list, Node<E> node, int index) {
        Node<E> leftChild = node.getLeft();
        Node<E> rightChild = node.getRight();
        if (leftChild != null) {
            list.set(index*2, leftChild);
            list = addChildren(list, leftChild, index*2);
        }
        if (rightChild != null) {
            list.set(index*2 + 1, rightChild);
            list = addChildren(list, rightChild, index*2 + 1);
        }
        return list;
    }
}
