package com.aim.project.ssp.heuristics;

import java.util.Random;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	public Reinsertion(Random random) {

		super(random);
	}

	@Override
	public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		SolutionRepresentationInterface representation = solution.getSolutionRepresentation();
		int[] tour = representation.getSolutionRepresentation();
		int length = tour.length;

		// Calculate number of reinsertions based on intensity
		int numReinsertions = calculateNumberOfReinsertions(intensityOfMutation);

		for(int i = 0; i < numReinsertions; i++) {
			// Select random element to remove (all positions are valid)
			int removePos = m_oRandom.nextInt(length);
			int element = tour[removePos];

			// Select random insertion position (different from original)
			int insertPos;
			do {
				insertPos = m_oRandom.nextInt(length);
			} while (insertPos == removePos);

			// Perform reinsertion
			if(removePos < insertPos) {
				// Shift elements left
				System.arraycopy(tour, removePos + 1, tour, removePos, insertPos - removePos);
			}
			else {
				// Shift elements right
				System.arraycopy(tour, insertPos, tour, insertPos + 1, removePos - insertPos);
			}
			tour[insertPos] = element;
		}

		// Update solution
		int newValue = m_oObjectiveFunction.getObjectiveFunctionValue(representation);
		solution.setObjectiveFunctionValue(newValue);
		return newValue;
	}

	private int calculateNumberOfReinsertions(double intensityOfMutation) {
		if(intensityOfMutation < 0.2) return 1;
		if(intensityOfMutation < 0.4) return 2;
		if(intensityOfMutation < 0.6) return 3;
		if(intensityOfMutation < 0.8) return 4;
		return 5; // For intensityOfMutation >= 0.8
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
