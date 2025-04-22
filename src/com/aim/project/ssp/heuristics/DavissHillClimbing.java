package com.aim.project.ssp.heuristics;

import java.util.Random;
import java.util.stream.IntStream;

import com.aim.project.ssp.interfaces.HeuristicInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;


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

		// TODO implementation of Davis's Hill Climbing
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
