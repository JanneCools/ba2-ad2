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

    public void optimize(int amount) {
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

    public static void main(String[] args) {
        OptimalTreeBenchmarks benchmarks = new OptimalTreeBenchmarks();
        benchmarks.optimize(10);
        benchmarks.optimize(50);
        benchmarks.optimize(100);
        benchmarks.optimize(500);
        benchmarks.optimize(1000);
    }
}
