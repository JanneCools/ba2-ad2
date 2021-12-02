package benchmarks;

import opgave.samplers.Sampler;
import opgave.samplers.ZipfSampler;
import oplossing.MyTree;
import oplossing.SemiSplayTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyTreeBenchmarks {

    private MyTree<Integer> tree;

    public MyTreeBenchmarks() {
        tree = new MyTree<>();
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

    public void addElementsZipf(int amount) {
        List<Integer> samples = new ZipfSampler(new Random(), amount).getElements();
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
        long start = System.currentTimeMillis();
        List<Integer> samplesToRemove = new Sampler(new Random(), amount).getElements();
        for (Integer sample: samplesToRemove) {
            tree.remove(sample);
        }
        long stop = System.currentTimeMillis();
        System.out.println("Tijd van verwijderen van " + amount + " elementen: " + (stop - start) + " ms.");
        System.out.println("Aantal bezochte toppen bij verwijderen van " + amount + " elementen: " + tree.getNodesVisited());
    }

    public void removeElementsZipf(int amount) {
        tree.setNodesVisited(0);
        long start = System.currentTimeMillis();
        List<Integer> samplesToRemove = new ZipfSampler(new Random(), amount).getElements();
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
        for (Integer sample : samplesToRemove) {
            tree.search(sample);
        }
        long stop = System.currentTimeMillis();
        System.out.println("Tijd van opzoeken van " + amount + " elementen: " + (stop - start) + " ms.");
        System.out.println("Aantal bezochte toppen bij opzoeken van " + amount + " elementen: " + tree.getNodesVisited());
    }

    public void searchElementsZipf(int amount) {
        tree.setNodesVisited(0);
        long start = System.currentTimeMillis();
        List<Integer> samplesToRemove = new ZipfSampler(new Random(), amount).getElements();
        for (Integer sample : samplesToRemove) {
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
    public void benchmarkSampler() {
        ArrayList<Integer> amounts = new ArrayList<>(List.of(10, 50, 100, 500, 1000, 5000, 10000, 50000, 100000));
        for (int amount: amounts) {
            addElements(amount);
            searchElements(amount);
            removeElements(amount);
            tree = new MyTree<>();
        }
    }

    public void benchmarkZipfSamples() {
        ArrayList<Integer> amounts = new ArrayList<>(List.of(10, 50, 100, 500, 1000, 5000, 10000, 50000, 100000));
        for (int amount: amounts) {
            addElementsZipf(amount);
            searchElementsZipf(amount);
            removeElementsZipf(amount);
            tree = new MyTree<>();
        }
    }

    public static void main(String[] args) {
        MyTreeBenchmarks benchmarks = new MyTreeBenchmarks();
        //Toevoegen, opzoeken en verwijderen van elementen met Sampler klasse
        System.out.println("Benchmarks met Sampler");
        benchmarks.benchmarkSampler();

        //Toevoegen, opzoeken en verwijderen van elementen met ZipfSampler klasse
        System.out.println("\nBenchmarks met ZipfSampler");
        benchmarks.benchmarkZipfSamples();

        //Toevoegen van opeenvolgende sleutels
        System.out.println("\nSlechtste geval");
        ArrayList<Integer> amounts = new ArrayList<>(List.of(10, 50, 100, 500, 1000, 5000, 10000, 50000, 100000));
        for (int amount: amounts) {
            benchmarks.addIncreasingNumbers(amount);
            benchmarks.tree = new MyTree<>();
        }
    }
}
