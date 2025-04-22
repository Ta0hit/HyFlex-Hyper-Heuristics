package com.aim.project.ssp.heuristics;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;

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

		// TODO implementation of adjacent swap
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

}
