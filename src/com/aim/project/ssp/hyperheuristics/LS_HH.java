package com.aim.project.ssp.hyperheuristics;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/**
 * Learning-based Selection Hyper-Heuristic (Probability Matching / Reinforcement Learning)
 *
 * Components:
 * 1. Heuristic Set: All low-level heuristics provided by the ProblemDomain.
 * 2. Credit Assignment: Reward heuristics when they produce improving moves.
 * 3. Quality Measure: Q-values updated via exponential recency-weighted average.
 * 4. Probability Matching: Selection probabilities proportional to Q-values^beta.
 * 5. Acceptance Criterion: Only accept moves that do not worsen the current solution.
 */
public class LS_HH extends HyperHeuristic {

    private final int[] heuristics;
    private final double[] qValues;
    private final double[] probabilities;
    private final double learningRate = 0.2;    // alpha
    private final double beta = 1.0;            // influences exploration vs exploitation

    public LS_HH(long seed) {
        super(seed);
        // Gather all heuristics (mutation, local search, crossover)
        heuristics = new int[getNumberOfHeuristicsOfAllTypes()];
        int idx = 0;
        for (HeuristicType type : HeuristicType.values()) {
            int[] h = getHeuristicsOfType(type);
            if (h != null) {
                for (int id : h)
                    heuristics[idx++] = id;
            }
        }
        // Initialize Q-values and probabilities
        qValues = new double[idx];
        probabilities = new double[idx];
        for (int i = 0; i < idx; i++) {
            qValues[i] = 1.0;   // default score
            probabilities[i] = 1.0 / idx;
        }
    }

    @Override
    protected void solve(ProblemDomain problem) {
        // Two-slot memory: current and candidate
        int currentIndex = 0, candidateIndex = 1;
        problem.initialiseSolution(currentIndex);
        double currentCost = problem.getFunctionValue(currentIndex);

        while (!hasTimeExpired()) {
            // 1. Select a heuristic via probability matching
            int hIndex = selectHeuristic();
            int hID = heuristics[hIndex];

            // 2. Apply heuristic to generate candidate
            double candidateCost = problem.applyHeuristic(hID, currentIndex, candidateIndex);

            // 3. Compute reward: 1 for improvement, 0 otherwise
            double reward = (candidateCost < currentCost) ? 1.0 : 0.0;

            // 4. Update Q-value for selected heuristic
            qValues[hIndex] = qValues[hIndex] + learningRate * (reward - qValues[hIndex]);

            // 5. Recompute probabilities (probability matching)
            double sum = 0.0;
            for (int i = 0; i < qValues.length; i++) {
                probabilities[i] = Math.pow(qValues[i], beta);
                sum += probabilities[i];
            }
            for (int i = 0; i < probabilities.length; i++) {
                probabilities[i] /= sum;
            }

            // 6. Accept only non-worsening moves
            if (candidateCost <= currentCost) {
                // swap indices
                int tmp = currentIndex;
                currentIndex = candidateIndex;
                candidateIndex = tmp;
                currentCost = candidateCost;
            }
        }
    }

    @Override
    public String toString() {
        return "LearningSelection_HH";
    }

    /**
     * Roulette-wheel selection of heuristic index based on probabilities array.
     */
    private int selectHeuristic() {
        double r = rng.nextDouble();
        double cum = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            cum += probabilities[i];
            if (r <= cum)
                return i;
        }
        return probabilities.length - 1; // fallback
    }

    /**
     * Utility to count total heuristics across all types
     */
    private int getNumberOfHeuristicsOfAllTypes() {
        int count = 0;
        for (HeuristicType type : HeuristicType.values()) {
            int[] h = getHeuristicsOfType(type);
            if (h != null) count += h.length;
        }
        return count;
    }

    private int[] getHeuristicsOfType(HeuristicType type) {

        // Define which heuristics belong to which types based on their indices
        return switch (type) {
            case MUTATION ->
                // AdjacentSwap (0) and Reinsertion (1) are mutation operators
                    new int[]{0, 1};
            case LOCAL_SEARCH ->
                // DavissHillClimbing (2) and NextDescent (3) are local search operators
                    new int[]{2, 3};
            case CROSSOVER ->
                // OX (4) is a crossover operator
                    new int[]{4};
            case RUIN_RECREATE ->
                // No ruin-recreate heuristics in current implementation
                    null;
            default -> null;
        };
    }
}
