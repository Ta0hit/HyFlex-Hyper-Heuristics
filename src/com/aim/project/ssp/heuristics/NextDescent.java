package com.aim.project.ssp.heuristics;


import java.util.Random;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;
import com.aim.project.ssp.solution.SolutionRepresentation;


/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {
	
	public NextDescent(Random random) {
	
		super(random);
	}

	@Override
	public double apply(SSPSolutionInterface solution, double dos, double iom) {

		SolutionRepresentationInterface representation = solution.getSolutionRepresentation();
		int[] currentSolution = representation.getSolutionRepresentation().clone();
		int currentCost = solution.getObjectiveFunctionValue();
		int length = currentSolution.length;

		int iterations = calculateNumberOfIterations(dos);
		boolean improvementFound = false;

		// Perform iterations until improvement found or max iterations reached
		for (int iter = 0; iter < iterations && !improvementFound; iter++) {
			// Try all possible adjacent swaps
			for (int i = 0; i < length - 1 && !improvementFound; i++) {
				// Create neighbor by swapping adjacent elements
				int[] neighbor = currentSolution.clone();
				swap(neighbor, i, i + 1);

				// Evaluate neighbor
				int neighborCost = m_oObjectiveFunction.getObjectiveFunctionValue(
						new SolutionRepresentation(neighbor));

				// If improvement found, accept it immediately
				if (neighborCost < currentCost) {
					representation.setSolutionRepresentation(neighbor);
					solution.setObjectiveFunctionValue(neighborCost);
					currentCost = neighborCost;
					improvementFound = true;
				}
			}
		}

		return currentCost;
	}

	@Override
	public boolean isCrossover() {

		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return true;
	}
}
