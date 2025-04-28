package com.aim.project.ssp.hyperheuristics;

import com.aim.project.ssp.HeuristicPair;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

/**
 * Learning-based Selection Hyper-Heuristic that uses reinforcement learning
 * with a Roulette Wheel Selection mechanism and an Adaptive Acceptance criterion.
 * 
 * The hyper-heuristic learns which heuristics are most effective during the search
 * and adjusts selection probabilities accordingly.
 */
public class LS2_HH extends HyperHeuristic {
    
    // Memory indices
    private static final int CURRENT_SOLUTION_INDEX = 0;
    private static final int CANDIDATE_SOLUTION_INDEX = 1;
    
    // Parameters for reinforcement learning
    private static final double ALPHA = 0.1;  // Learning rate
    private static final double BETA = 2.0;   // Controls exploitation vs exploration
    private static final int DEFAULT_SCORE = 10;
    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 100;
    
    // Parameters for adaptive acceptance
    private static final double INITIAL_ACCEPTANCE_RATE = 0.5;
    private static final double COOLING_RATE = 0.99;
    
    // Arrays to store heuristic information
    private int[] mutationHeuristics;
    private int[] localSearchHeuristics;
    private int[] crossoverHeuristics;
    private int[] allHeuristics;
    
    // Maps to track heuristic performance
    private Map<Integer, Double> heuristicScores;
    private Map<Integer, Integer> heuristicCalls;
    private Map<Integer, Integer> heuristicSuccesses;
    
    // Acceptance variables
    private double currentAcceptanceRate;
    
    /**
     * Creates a new Learning-based Selection Hyper-Heuristic
     * 
     * @param seed Random seed for reproducibility
     */
    public LS2_HH(long seed) {
        super(seed);
        this.heuristicScores = new HashMap<>();
        this.heuristicCalls = new HashMap<>();
        this.heuristicSuccesses = new HashMap<>();
        this.currentAcceptanceRate = INITIAL_ACCEPTANCE_RATE;
    }
    
    @Override
    protected void solve(ProblemDomain problem) {
        // Initialize memory
        problem.setMemorySize(2);
        problem.initialiseSolution(CURRENT_SOLUTION_INDEX);
        
        // Get available heuristics
        initializeHeuristics(problem);
        
        // Initialize scores
        for (int h : allHeuristics) {
            heuristicScores.put(h, (double) DEFAULT_SCORE);
            heuristicCalls.put(h, 0);
            heuristicSuccesses.put(h, 0);
        }
        
        // Get initial solution quality
        double currentSolutionValue = problem.getFunctionValue(CURRENT_SOLUTION_INDEX);
        double bestSolutionValue = currentSolutionValue;
        
        // Main search loop
        while (!hasTimeExpired()) {
            // Select heuristic using roulette wheel
            int selectedHeuristic = selectHeuristic();
            
            // Apply the selected heuristic
            double candidateSolutionValue;
            
            if (isCrossover(selectedHeuristic)) {
                // For crossover heuristics, we need two parent solutions
                // Create a temporary solution for the second parent
                int secondParentIndex = CANDIDATE_SOLUTION_INDEX;
                problem.initialiseSolution(secondParentIndex);
                
                // Apply the crossover
                candidateSolutionValue = problem.applyHeuristic(
                    selectedHeuristic, 
                    CURRENT_SOLUTION_INDEX, 
                    secondParentIndex, 
                    CANDIDATE_SOLUTION_INDEX
                );
            } else {
                // For non-crossover heuristics
                candidateSolutionValue = problem.applyHeuristic(
                    selectedHeuristic, 
                    CURRENT_SOLUTION_INDEX, 
                    CANDIDATE_SOLUTION_INDEX
                );
            }
            
            // Update heuristic call count
            heuristicCalls.put(selectedHeuristic, heuristicCalls.get(selectedHeuristic) + 1);
            
            // Calculate delta (improvement)
            double delta = currentSolutionValue - candidateSolutionValue;
            
            // Acceptance decision
            boolean accept = false;
            
            if (delta > 0) {
                // Always accept improving moves
                accept = true;
                heuristicSuccesses.put(selectedHeuristic, heuristicSuccesses.get(selectedHeuristic) + 1);
                
                // Update reinforcement learning scores
                updateScore(selectedHeuristic, 1.0);
            } else if (delta == 0) {
                // Accept neutral moves with 50% probability
                accept = rng.nextDouble() < 0.5;
                
                // Neutral moves get a small positive reward
                updateScore(selectedHeuristic, 0.2);
            } else {
                // Accept worsening moves based on adaptive acceptance rate
                accept = rng.nextDouble() < currentAcceptanceRate;
                
                // Worsening moves get a negative reward
                updateScore(selectedHeuristic, -0.5);
            }
            
            // Update solution if accepted
            if (accept) {
                problem.copySolution(CANDIDATE_SOLUTION_INDEX, CURRENT_SOLUTION_INDEX);
                currentSolutionValue = candidateSolutionValue;
                
                // Update best solution if needed
                if (currentSolutionValue < bestSolutionValue) {
                    bestSolutionValue = currentSolutionValue;
                }
            }
            
            // Cool down acceptance rate gradually
            currentAcceptanceRate *= COOLING_RATE;
            
            // Periodically apply a local search intensification
            if (rng.nextDouble() < 0.1 && localSearchHeuristics.length > 0) {
                // Apply a random local search heuristic
                int lsHeuristic = localSearchHeuristics[rng.nextInt(localSearchHeuristics.length)];
                double lsValue = problem.applyHeuristic(
                    lsHeuristic, 
                    CURRENT_SOLUTION_INDEX, 
                    CURRENT_SOLUTION_INDEX
                );
                
                // Update current solution value
                currentSolutionValue = lsValue;
                
                // Update best solution if needed
                if (currentSolutionValue < bestSolutionValue) {
                    bestSolutionValue = currentSolutionValue;
                }
            }
        }
    }
    
    /**
     * Initialize arrays of available heuristics by type
     */
    private void initializeHeuristics(ProblemDomain problem) {
        mutationHeuristics = problem.getHeuristicsOfType(HeuristicType.MUTATION);
        localSearchHeuristics = problem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);
        crossoverHeuristics = problem.getHeuristicsOfType(HeuristicType.CROSSOVER);
        
        // Count total number of heuristics
        int totalHeuristics = 
            (mutationHeuristics != null ? mutationHeuristics.length : 0) +
            (localSearchHeuristics != null ? localSearchHeuristics.length : 0) +
            (crossoverHeuristics != null ? crossoverHeuristics.length : 0);
        
        // Create array of all heuristics
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
    
    /**
     * Select a heuristic using reinforcement learning
     * with roulette wheel selection
     */
    private int selectHeuristic() {
        // Calculate selection probabilities using scores
        double[] probabilities = new double[allHeuristics.length];
        double totalProbability = 0.0;
        
        for (int i = 0; i < allHeuristics.length; i++) {
            // Apply exponentiation to increase contrast between good and bad heuristics
            probabilities[i] = Math.pow(heuristicScores.get(allHeuristics[i]), BETA);
            totalProbability += probabilities[i];
        }
        
        // Normalize probabilities
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= totalProbability;
        }
        
        // Roulette wheel selection
        double spinTheWheel = rng.nextDouble();
        double cumulativeProbability = 0.0;
        
        for (int i = 0; i < allHeuristics.length; i++) {
            cumulativeProbability += probabilities[i];
            if (spinTheWheel <= cumulativeProbability) {
                return allHeuristics[i];
            }
        }
        
        // Default case (should not happen if probabilities sum to 1.0)
        return allHeuristics[0];
    }
    
    /**
     * Update the score for a heuristic based on performance
     */
    private void updateScore(int heuristic, double reward) {
        double currentScore = heuristicScores.get(heuristic);
        double newScore = currentScore + ALPHA * (reward - currentScore);
        
        // Ensure score stays within bounds
        newScore = Math.max(MIN_SCORE, Math.min(MAX_SCORE, newScore));
        
        heuristicScores.put(heuristic, newScore);
    }
    
    /**
     * Check if a heuristic is a crossover operator
     */
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
        return "Learning-based Selection Hyper-Heuristic";
    }
}