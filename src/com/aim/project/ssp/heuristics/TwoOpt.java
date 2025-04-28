package com.aim.project.ssp.heuristics;

import java.util.Random;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 * Local search that tries all i<j 2-opt reversals, takes the first improving one.
 */
public class TwoOpt extends HeuristicOperators implements HeuristicInterface {

    public TwoOpt(Random random) {
        super(random);
    }

    @Override
    public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
        SolutionRepresentationInterface rep = solution.getSolutionRepresentation();
        int[] original = rep.getSolutionRepresentation();
        int n = original.length;
        int baseCost = solution.getObjectiveFunctionValue();

        // Determine search intensity based on depthOfSearch
        int maxIterations = calculateMaxIterations(n, depthOfSearch);
        int iterationsPerformed = 0;

        // try every pair (i,j) until we find an improvement or reach maxIterations
        for (int i = 0; i < n - 1 && iterationsPerformed < maxIterations; i++) {
            for (int j = i + 1; j < n && iterationsPerformed < maxIterations; j++) {
                iterationsPerformed++;

                int[] candidate = original.clone();

                // reverse [i..j]
                int a = i, b = j;
                while (a < b) {
                    int tmp = candidate[a];
                    candidate[a] = candidate[b];
                    candidate[b] = tmp;
                    a++; b--;
                }

                // evaluate
                rep.setSolutionRepresentation(candidate);
                int cost = m_oObjectiveFunction.getObjectiveFunctionValue(rep);
                if (cost < baseCost) {
                    solution.setObjectiveFunctionValue(cost);
                    return cost;
                }
            }
        }

        // no improvement: restore and carry on
        rep.setSolutionRepresentation(original);
        return baseCost;
    }

    /**
     * Calculates the maximum number of iterations based on problem size and depth of search
     */
    private int calculateMaxIterations(int problemSize, double depthOfSearch) {
        // The maximum possible iterations is n*(n-1)/2 (all possible pairs)
        int maxPossibleIterations = problemSize * (problemSize - 1) / 2;

        // Scale by depthOfSearch (0.0-1.0)
        int iterations = (int) Math.max(1, Math.min(maxPossibleIterations,
                maxPossibleIterations * depthOfSearch));

        return iterations;
    }

    @Override
    public boolean isCrossover() {
        return false;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return true;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return false;
    }
}
