package com.aim.project.ssp.heuristics;

import java.util.Random;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 * A one-point crossover that pairs the current solution with a freshly generated random solution.
 * Takes a random cut point and combines the first part of parent1 with the second part of parent2.
 */
public class OnePointX extends HeuristicOperators implements HeuristicInterface {

    public OnePointX(Random random) {
        super(random);
    }

    @Override
    public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
        // Get the existing solution representation
        SolutionRepresentationInterface rep1 = solution.getSolutionRepresentation();
        int[] parent1 = rep1.getSolutionRepresentation();
        int n = parent1.length;

        // Create a randomized second parent by cloning the first and shuffling
        int[] parent2 = parent1.clone();
        shuffleArray(parent2);

        // Choose a random crossover point (excluding 0 and n to ensure mix)
        int crossoverPoint = 1 + m_oRandom.nextInt(n - 2);

        // Create offspring by taking first part of parent1 and second part of parent2
        int[] offspring = new int[n];

        // Copy first part from parent1
        System.arraycopy(parent1, 0, offspring, 0, crossoverPoint);

        // Copy second part from parent2, ensuring no duplicates
        int offspringPos = crossoverPoint;
        for (int i = 0; i < n; i++) {
            boolean alreadyExists = false;
            for (int j = 0; j < crossoverPoint; j++) {
                if (parent2[i] == offspring[j]) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists && offspringPos < n) {
                offspring[offspringPos++] = parent2[i];
            }
        }

        // If we somehow didn't fill the array, fill remaining positions
        // (shouldn't happen for valid permutation representation)
        if (offspringPos < n) {
            boolean[] used = new boolean[n];
            for (int i = 0; i < offspringPos; i++) {
                used[offspring[i] - 1] = true; // assuming 1-based values
            }

            for (int i = 0; i < n && offspringPos < n; i++) {
                if (!used[i]) {
                    offspring[offspringPos++] = i + 1;
                }
            }
        }

        // Update solution
        rep1.setSolutionRepresentation(offspring);
        int newValue = m_oObjectiveFunction.getObjectiveFunctionValue(rep1);
        solution.setObjectiveFunctionValue(newValue);

        return newValue;
    }

    @Override
    public boolean isCrossover() {
        return true;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return false;
    }
}