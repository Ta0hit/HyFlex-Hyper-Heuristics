package com.aim.project.ssp.heuristics;

import java.util.Random;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 * Mutation operator that selects indices i<j and reverses the subsequence [i…j].
 */
public class InversionMutation extends HeuristicOperators implements HeuristicInterface {

    public InversionMutation(Random random) {
        super(random);
    }

    @Override
    public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
        SolutionRepresentationInterface rep = solution.getSolutionRepresentation();
        int[] sol = rep.getSolutionRepresentation();
        int n = sol.length;

        // Determine number of inversions based on intensityOfMutation
        int numInversions = 1;
        if (intensityOfMutation > 0.2) {
            numInversions = (int)(1 + intensityOfMutation * 4); // 1-5 inversions based on intensity
        }

        for (int k = 0; k < numInversions; k++) {
            // pick two cut points
            int i = m_oRandom.nextInt(n);
            int j = m_oRandom.nextInt(n);

            // ensure i < j
            if (i > j) {
                int tmp = i;
                i = j;
                j = tmp;
            }

            // reverse subsequence [i..j]
            while (i < j) {
                int tmp = sol[i];
                sol[i] = sol[j];
                sol[j] = tmp;
                i++;
                j--;
            }
        }

        // no need to write back since we modified the array directly

        // re-evaluate
        int newCost = m_oObjectiveFunction.getObjectiveFunctionValue(rep);
        solution.setObjectiveFunctionValue(newCost);

        return newCost;
    }

    @Override
    public boolean isCrossover() {
        return false;
    }

    @Override
    public boolean usesDepthOfSearch() {
        return false;
    }

    @Override
    public boolean usesIntensityOfMutation() {
        return true;
    }
}
