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

		// TODO implementation of Order Crossover
		return -1.0d;
	}

	@Override
	public double apply(SSPSolutionInterface p1, SSPSolutionInterface p2,
						SSPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {

		// TODO implementation of Order Crossover
		return -1.0d;
	}

	/*
	 * TODO update the methods below to return the correct boolean value.
	 */

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

		return false;
	}

	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.f = f;
	}
}
