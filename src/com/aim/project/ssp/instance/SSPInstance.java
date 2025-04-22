package com.aim.project.ssp.instance;


import java.util.ArrayList;
import java.util.Random;

import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.solution.SSPSolution;

/**
 * @author Warren G. Jackson
 * @since 17/03/2025
 *
 */
public class SSPInstance implements SSPInstanceInterface {
	
	private final Location[] aoLocations;
	
	private final Location oHotelLocation;
	
	private final Location oAirportLocation;
	
	private final int iNumberOfLocations;
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface f = null;
	
	public SSPInstance(int iNumberOfLocations, Location[] aoLocations, Location oHotelLocation, Location oAirportLocation, Random random) {
		
		this.iNumberOfLocations = iNumberOfLocations;
		this.oRandom = random;
		this.aoLocations = aoLocations;
		this.oHotelLocation = oHotelLocation;
		this.oAirportLocation = oAirportLocation;
	}

	@Override
	public SSPSolution createSolution(InitialisationMode mode) {
		
		// TODO
		return null;
	}
	
	@Override
	public ObjectiveFunctionInterface getSSPObjectiveFunction() {

		// TODO
		return null;
	}

	@Override
	public int getNumberOfLocations() {

		// TODO
		return -1;
	}

	@Override
	public Location getSightseeingLocation(int iLocationId) {

		// TODO
		return null;
	}

	@Override
	public Location getHotelLocation() {

		// TODO
		return null;
	}

	@Override
	public Location getAirportLocation() {

		// TODO
		return null;
	}
	
	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(SSPSolutionInterface oSolution) {

		// TODO
		return null;
	}
}
