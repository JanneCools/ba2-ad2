package benchmarks;

import opgave.samplers.Sampler;
import oplossing.OptimalTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OptimalTreeBenchmarks {

    private OptimalTree<Integer> tree;

    public OptimalTreeBenchmarks() {
        tree = new OptimalTree<>();
    }

    public void optimizeInternal(int amount) {
        List<Integer> keys = new Sampler(new Random(), amount).getElements();
        List<Double> weights = new ArrayList<>();
        List<Integer> intWeights = new Sampler(new Random(), amount).getElements();
        for (int weight: intWeights) {
            weights.add((double) weight);
        }
        long start = System.currentTimeMillis();
        tree.optimize(keys, weights);
        long stop = System.currentTimeMillis();
        System.out.println("Tijd van optimaliseren van " + amount + " elementen: " + (stop - start) + " ms.");
    }

    public void optimizeExternal(int amount) {
        List<Integer> keys = new Sampler(new Random(), amount).getElements();
        List<Double> interalWeights = new ArrayList<>();
        List<Integer> intWeights = new Sampler(new Random(), amount).getElements();
        for (int weight: intWeights) {
            interalWeights.add((double) weight);
        }
        List<Double> externalWeight = new ArrayList<>();
        List<Integer> extWeights = new Sampler(new Random(), amount+1).getElements();
        for (int weight: extWeights) {
            externalWeight.add((double) weight);
        }
        long start = System.currentTimeMillis();
        tree.optimize(keys, interalWeights, externalWeight);
        long stop = System.currentTimeMillis();
        System.out.println("Tijd van optimaliseren van " + amount + " elementen: " + (stop - start) + " ms.");
    }

    public static void main(String[] args) {
        OptimalTreeBenchmarks benchmarks = new OptimalTreeBenchmarks();
        benchmarks.optimizeInternal(10);
        benchmarks.optimizeInternal(50);
        benchmarks.optimizeInternal(100);
        benchmarks.optimizeInternal(500);
        benchmarks.optimizeInternal(1000);
    }
}
