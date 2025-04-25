package com.aim.project.ssp.heuristics;

import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;

import java.util.Random;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 * Implement any common functionality here so that your heuristics can reuse them.
 *	E.g.  you may want to implement the swapping of two sight-seeing locations here.
 *
 */
public class HeuristicOperators {

	protected ObjectiveFunctionInterface m_oObjectiveFunction;

	protected Random m_oRandom;

	public HeuristicOperators(Random oRandom) {

		m_oRandom = oRandom;
	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		m_oObjectiveFunction = f;
	}

	protected void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	protected int calculateNumberOfIterations(double depthOfSearch) {
		if(depthOfSearch < 0.2) return 1;
		if(depthOfSearch < 0.4) return 2;
		if(depthOfSearch < 0.6) return 3;
		if(depthOfSearch < 0.8) return 4;
		return 5; // For depthOfSearch >= 0.8
	}

	protected void shuffleArray(int[] array) {
		for(int i = array.length - 1; i > 0; i--) {
			int index = m_oRandom.nextInt(i + 1);
			// Simple swap
			int temp = array[index];
			array[index] = array[i];
			array[i] = temp;
		}
	}
}
