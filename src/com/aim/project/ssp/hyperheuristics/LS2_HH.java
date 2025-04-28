package com.aim.project.ssp.hyperheuristics;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/**
 * Learning-based Selection Hyper-Heuristic (Enhanced Version)
 */
public class LS2_HH extends HyperHeuristic {

    private static final int CURRENT_SOLUTION_INDEX = 0;
    private static final int CANDIDATE_SOLUTION_INDEX = 1;

    private static final double ALPHA = 0.1;
    private static final double BETA = 2.0;
    private static final int DEFAULT_SCORE = 10;
    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 100;

    private static final double INITIAL_ACCEPTANCE_RATE = 0.5;
    private static final double COOLING_RATE = 0.99;
    private static final double DECAY_RATE = 0.995;

    private int[] mutationHeuristics;
    private int[] localSearchHeuristics;
    private int[] crossoverHeuristics;
    private int[] allHeuristics;

    private Map<Integer, Double> heuristicScores;
    private Map<Integer, Integer> heuristicCalls;
    private Map<Integer, Integer> heuristicSuccesses;

    private double currentAcceptanceRate;

    public LS2_HH(long seed) {
        super(seed);
        this.heuristicScores = new HashMap<>();
        this.heuristicCalls = new HashMap<>();
        this.heuristicSuccesses = new HashMap<>();
        this.currentAcceptanceRate = INITIAL_ACCEPTANCE_RATE;
    }

    @Override
    protected void solve(ProblemDomain problem) {

        problem.setMemorySize(2);
        problem.initialiseSolution(CURRENT_SOLUTION_INDEX);

        initializeHeuristics(problem);

        for (int h : allHeuristics) {
            heuristicScores.put(h, (double) DEFAULT_SCORE);
            heuristicCalls.put(h, 0);
            heuristicSuccesses.put(h, 0);
        }

        double currentSolutionValue = problem.getFunctionValue(CURRENT_SOLUTION_INDEX);
        double bestSolutionValue = currentSolutionValue;
        int nonImprovingMoves = 0;

        while (!hasTimeExpired()) {

            int selectedHeuristic = selectHeuristic();
            double candidateSolutionValue;

            if (isCrossover(selectedHeuristic)) {
                int secondParentIndex = CANDIDATE_SOLUTION_INDEX;

                // New: Instead of random new solution, copy current and mutate
                problem.copySolution(CURRENT_SOLUTION_INDEX, secondParentIndex);
                if (mutationHeuristics.length > 0) {
                    int randomMutation = mutationHeuristics[rng.nextInt(mutationHeuristics.length)];
                    problem.applyHeuristic(randomMutation, secondParentIndex, secondParentIndex);
                }

                candidateSolutionValue = problem.applyHeuristic(
                        selectedHeuristic,
                        CURRENT_SOLUTION_INDEX,
                        secondParentIndex,
                        CANDIDATE_SOLUTION_INDEX
                );
            } else {
                candidateSolutionValue = problem.applyHeuristic(
                        selectedHeuristic,
                        CURRENT_SOLUTION_INDEX,
                        CANDIDATE_SOLUTION_INDEX
                );
            }

            heuristicCalls.put(selectedHeuristic, heuristicCalls.get(selectedHeuristic) + 1);
            double delta = currentSolutionValue - candidateSolutionValue;
            boolean accept = false;

            if (delta > 0) {
                accept = true;
                heuristicSuccesses.put(selectedHeuristic, heuristicSuccesses.get(selectedHeuristic) + 1);
                updateScore(selectedHeuristic, 1.0);
                nonImprovingMoves = 0;
            } else if (delta == 0) {
                accept = rng.nextDouble() < 0.5;
                updateScore(selectedHeuristic, 0.2);
                nonImprovingMoves++;
            } else {
                accept = rng.nextDouble() < currentAcceptanceRate;
                updateScore(selectedHeuristic, -0.5);
                nonImprovingMoves++;
            }

            if (accept) {
                problem.copySolution(CANDIDATE_SOLUTION_INDEX, CURRENT_SOLUTION_INDEX);
                currentSolutionValue = candidateSolutionValue;
                if (currentSolutionValue < bestSolutionValue) {
                    bestSolutionValue = currentSolutionValue;
                }
            }

            // Decay all heuristic scores slightly over time
            decayScores();

            // Cool down acceptance rate
            currentAcceptanceRate *= COOLING_RATE;

            // Extra Local Search if stuck for too long
            if (nonImprovingMoves >= 50 && localSearchHeuristics.length > 0) {
                int lsHeuristic = localSearchHeuristics[rng.nextInt(localSearchHeuristics.length)];
                double lsValue = problem.applyHeuristic(
                        lsHeuristic,
                        CURRENT_SOLUTION_INDEX,
                        CURRENT_SOLUTION_INDEX
                );
                currentSolutionValue = lsValue;
                if (currentSolutionValue < bestSolutionValue) {
                    bestSolutionValue = currentSolutionValue;
                }
                nonImprovingMoves = 0; // Reset after applying local search
            }

            // Occasionally apply local search for intensification
            if (rng.nextDouble() < 0.1 && localSearchHeuristics.length > 0) {
                int lsHeuristic = localSearchHeuristics[rng.nextInt(localSearchHeuristics.length)];
                double lsValue = problem.applyHeuristic(
                        lsHeuristic,
                        CURRENT_SOLUTION_INDEX,
                        CURRENT_SOLUTION_INDEX
                );
                currentSolutionValue = lsValue;
                if (currentSolutionValue < bestSolutionValue) {
                    bestSolutionValue = currentSolutionValue;
                }
            }
        }
    }

    private void initializeHeuristics(ProblemDomain problem) {
        mutationHeuristics = problem.getHeuristicsOfType(HeuristicType.MUTATION);
        localSearchHeuristics = problem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);
        crossoverHeuristics = problem.getHeuristicsOfType(HeuristicType.CROSSOVER);

        int totalHeuristics =
                (mutationHeuristics != null ? mutationHeuristics.length : 0) +
                        (localSearchHeuristics != null ? localSearchHeuristics.length : 0) +
                        (crossoverHeuristics != null ? crossoverHeuristics.length : 0);

        allHeuristics = new int[totalHeuristics];
        int index = 0;

        if (mutationHeuristics != null) {
            for (int h : mutationHeuristics) {
                allHeuristics[index++] = h;
            }
        }

        if (localSearchHeuristics != null) {
            for (int h : localSearchHeuristics) {
                allHeuristics[index++] = h;
            }
        }

        if (crossoverHeuristics != null) {
            for (int h : crossoverHeuristics) {
                allHeuristics[index++] = h;
            }
        }
    }

    private int selectHeuristic() {
        double[] probabilities = new double[allHeuristics.length];
        double totalProbability = 0.0;

        for (int i = 0; i < allHeuristics.length; i++) {
            probabilities[i] = Math.pow(heuristicScores.get(allHeuristics[i]), BETA);
            totalProbability += probabilities[i];
        }

        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= totalProbability;
        }

        double spinTheWheel = rng.nextDouble();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < allHeuristics.length; i++) {
            cumulativeProbability += probabilities[i];
            if (spinTheWheel <= cumulativeProbability) {
                return allHeuristics[i];
            }
        }

        return allHeuristics[0];
    }

    private void updateScore(int heuristic, double reward) {
        double currentScore = heuristicScores.get(heuristic);
        double newScore = currentScore + ALPHA * (reward - currentScore);
        newScore = Math.max(MIN_SCORE, Math.min(MAX_SCORE, newScore));
        heuristicScores.put(heuristic, newScore);
    }

    private void decayScores() {
        for (int h : allHeuristics) {
            double score = heuristicScores.get(h) * DECAY_RATE;
            heuristicScores.put(h, Math.max(MIN_SCORE, score));
        }
    }

    private boolean isCrossover(int heuristic) {
        if (crossoverHeuristics == null) {
            return false;
        }
        for (int h : crossoverHeuristics) {
            if (h == heuristic) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Enhanced Learning-based Selection Hyper-Heuristic";
    }
}
