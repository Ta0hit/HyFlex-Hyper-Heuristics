package com.aim.project.ssp.heuristics;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

import java.util.Random;


/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	public AdjacentSwap(Random random) {

		super(random);
	}

	@Override
	public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// Get the solution representation
		SolutionRepresentationInterface representation = solution.getSolutionRepresentation();
		int[] solutionArray = representation.getSolutionRepresentation();
		int length = solutionArray.length;

		// Determine number of swaps based on intensityOfMutation
		int numSwaps = calculateNumberOfSwaps(intensityOfMutation);

		// Perform the swaps
		for (int i = 0; i < numSwaps; i++) {
			// Select a random position (avoiding first and last elements)
			int position = m_oRandom.nextInt(length - 1);

			// Perform the adjacent swap
			swap(solutionArray, position, position + 1);
		}

		// Calculate new objective value
		int newValue = m_oObjectiveFunction.getObjectiveFunctionValue(representation);
		solution.setObjectiveFunctionValue(newValue);

		return newValue;
	}

	private int calculateNumberOfSwaps(double intensityOfMutation) {
		if(intensityOfMutation >= 0.0 && intensityOfMutation < 0.2) return 1;
		if(intensityOfMutation >= 0.2 && intensityOfMutation < 0.4) return 2;
		if(intensityOfMutation >= 0.4 && intensityOfMutation < 0.6) return 4;
		if(intensityOfMutation >= 0.6 && intensityOfMutation < 0.8) return 8;
		if(intensityOfMutation >= 0.8 && intensityOfMutation < 1.0) return 16;
		if(intensityOfMutation == 1.0) return 32;
		return 1; // default
	}

	@Override
	public boolean isCrossover() {

		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return false;
	}

}
