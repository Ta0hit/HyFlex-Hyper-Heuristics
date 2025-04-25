package com.aim.project.ssp.heuristics;

import java.util.Random;

import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.XOHeuristicInterface;

/**
 *
 * @author Warren G Jackson
 * @since 17/03/2025
 * 
 * See `COMP2001-Project-2025.docx` for further details.
 *
 */
public class OX implements XOHeuristicInterface {
	
	private final Random random;
	
	private ObjectiveFunctionInterface f;

	public OX(Random random) {
		
		this.random = random;
	}

	@Override
	public double apply(SSPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// For single solution case, just copy the solution as per requirements
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public double apply(SSPSolutionInterface p1, SSPSolutionInterface p2,
						SSPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {

		int[] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation();
		int[] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation();
		int length = parent1.length;

		// Select two cut points (ensuring at least one element outside the segment)
		int cut1 = random.nextInt(length - 1);
		int cut2;
		do {
			cut2 = random.nextInt(length);
		} while (cut2 <= cut1 || (cut2 - cut1) >= length - 1);

		// Create child solution
		int[] child = new int[length];
		boolean[] inSegment = new boolean[length];

		// Copy segment from parent1 to child
		for (int i = cut1; i <= cut2; i++) {
			child[i] = parent1[i];
			inSegment[parent1[i]] = true;
		}

		// Fill remaining positions from parent2
		int currentPos = (cut2 + 1) % length;
		int parent2Pos = (cut2 + 1) % length;

		while (currentPos != cut1) {
			int gene = parent2[parent2Pos];
			if (!inSegment[gene]) {
				child[currentPos] = gene;
				currentPos = (currentPos + 1) % length;
			}
			parent2Pos = (parent2Pos + 1) % length;
		}

		// Update child solution
		c.getSolutionRepresentation().setSolutionRepresentation(child);
		int newValue = f.getObjectiveFunctionValue(c.getSolutionRepresentation());
		c.setObjectiveFunctionValue(newValue);

		return newValue;
	}


	@Override
	public boolean isCrossover() {

		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {

		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {

		return false;
	}

	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.f = f;
	}
}
