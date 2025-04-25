package com.aim.project.ssp.heuristics;

import java.util.Random;
import java.util.stream.IntStream;

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
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {
	
	public DavissHillClimbing(Random random) {
	
		super(random);
	}

	@Override
	public double apply(SSPSolutionInterface solution, double dos, double iom) {

		SolutionRepresentationInterface representation = solution.getSolutionRepresentation();
		int[] currentSolution = representation.getSolutionRepresentation().clone();
		int currentCost = solution.getObjectiveFunctionValue();
		int length = currentSolution.length;

		int iterations = calculateNumberOfIterations(dos);
		boolean improvementFound;

		for(int iter = 0; iter < iterations; iter++) {
			improvementFound = false;

			// Create random order of indices to try swaps
			int[] indices = IntStream.range(0, length - 1).toArray();
			shuffleArray(indices);

			// Try all possible adjacent swaps in random order
			for(int i : indices) {
				// Create neighbor by swapping adjacent elements
				int[] neighbor = currentSolution.clone();
				swap(neighbor, i, i + 1);

				// Evaluate neighbor
				int neighborCost = m_oObjectiveFunction.getObjectiveFunctionValue(
						new SolutionRepresentation(neighbor));

				// If improvement found, accept it and restart
				if(neighborCost < currentCost) {
					System.arraycopy(neighbor, 0, currentSolution, 0, length);
					currentCost = neighborCost;
					improvementFound = true;
					break; // Restart with new solution
				}
			}

			// If no improvement found in full pass, terminate
			if(!improvementFound) {
				break;
			}
		}

		// Update the solution if improvements were found
		if(currentCost != solution.getObjectiveFunctionValue()) {
			representation.setSolutionRepresentation(currentSolution);
			solution.setObjectiveFunctionValue(currentCost);
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
