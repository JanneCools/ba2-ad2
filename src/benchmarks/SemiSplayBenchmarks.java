package benchmarks;

import opgave.samplers.Sampler;
import oplossing.SemiSplayTree;

import java.util.List;
import java.util.Random;

public class SemiSplayBenchmarks {

    private SemiSplayTree<Integer> tree;

    public SemiSplayBenchmarks() {
        tree = new SemiSplayTree<>();
    }

    public void addElements(int amount) {
        List<Integer> samples = new Sampler(new Random(), amount).getElements();
        long start = System.currentTimeMillis();
        for (Integer sample: samples) {
            tree.add(sample);
        }
        long stop = System.currentTimeMillis();
        System.out.println("Tijd van toevoegen van " + amount + " elementen: " + (stop - start) + " ms.");
        System.out.println("Aantal bezochte toppen bij toevoegen van " + amount + " elementen: " + tree.getNodesVisited());
    }

    public void removeElements(int amount) {
        tree.setNodesVisited(0);
        List<Integer> samplesToRemove = new Sampler(new Random(), amount).getElements();
        long start = System.currentTimeMillis();
        for (Integer sample: samplesToRemove) {
            tree.remove(sample);
        }
        long stop = System.currentTimeMillis();
        System.out.println("Tijd van verwijderen van " + amount + " elementen: " + (stop - start) + " ms.");
        System.out.println("Aantal bezochte toppen bij verwijderen van " + amount + " elementen: " + tree.getNodesVisited());
    }

    public void searchElements(int amount) {
        tree.setNodesVisited(0);
        long start = System.currentTimeMillis();
        List<Integer> samplesToRemove = new Sampler(new Random(), amount).getElements();
        for (Integer sample: samplesToRemove) {
            tree.search(sample);
        }
        long stop = System.currentTimeMillis();
        System.out.println("Tijd van opzoeken van " + amount + " elementen: " + (stop - start) + " ms.");
        System.out.println("Aantal bezochte toppen bij opzoeken van " + amount + " elementen: " + tree.getNodesVisited());
    }

    public void addIncreasingNumbers(int amount) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            tree.remove(i);
        }
        long stop = System.currentTimeMillis();
        System.out.println("Tijd van toevoegen van " + amount + " opeenvolgende elementen: " + (stop - start) + " ms.");
        System.out.println("Aantal bezochte toppen bij toevoegen van " + amount + " opeenvolgende elementen: " + tree.getNodesVisited());
    }

    public static void main(String[] args) {
        SemiSplayBenchmarks benchmarks = new SemiSplayBenchmarks();

        //Toevoegen, opzoeken en verwijderen van willekeurige sleutels
        //benchmarks.addElements(10);
        //benchmarks.searchElements(10);
        //benchmarks.removeElements(10);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addElements(50);
        //benchmarks.searchElements(50);
        //benchmarks.removeElements(50);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addElements(100);
        //benchmarks.searchElements(100);
        //benchmarks.removeElements(100);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addElements(500)
        //benchmarks.searchElements(500);
        //benchmarks.removeElements(500);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addElements(1000);
        //benchmarks.searchElements(1000);
        //benchmarks.removeElements(1000);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addElements(5000);
        //benchmarks.searchElements(5000);
        //benchmarks.removeElements(5000);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addElements(10000);
        //benchmarks.searchElements(10000);
        //benchmarks.removeElements(10000);
        //benchmarks.tree = new SemiSplayTree<>();
        benchmarks.addElements(100000);
        //benchmarks.searchElements(100000);
        //benchmarks.removeElements(100000);

        //Toevoegen van opeenvolgende sleutels
        //benchmarks.addIncreasingNumbers(10);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addIncreasingNumbers(50);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addIncreasingNumbers(100);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addIncreasingNumbers(500);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addIncreasingNumbers(1000);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addIncreasingNumbers(5000);
        //benchmarks.tree = new SemiSplayTree<>();
        //benchmarks.addIncreasingNumbers(10000);
        //benchmarks.tree = new SemiSplayTree<>();
        benchmarks.addIncreasingNumbers(100000);
        benchmarks.tree = new SemiSplayTree<>();
    }
}
