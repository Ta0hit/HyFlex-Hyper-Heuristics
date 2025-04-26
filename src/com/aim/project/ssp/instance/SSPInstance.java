package com.aim.project.ssp.instance;

import java.util.ArrayList;
import java.util.Random;

import com.aim.project.ssp.SSPObjectiveFunction;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.solution.SSPSolution;
import com.aim.project.ssp.solution.SolutionRepresentation;

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

	private ObjectiveFunctionInterface f;

	public SSPInstance(int iNumberOfLocations, Location[] aoLocations, Location oHotelLocation, Location oAirportLocation, Random random) {

		this.iNumberOfLocations = iNumberOfLocations;
		this.oRandom = random;
		this.aoLocations = aoLocations;
		this.oHotelLocation = oHotelLocation;
		this.oAirportLocation = oAirportLocation;
		this.f = new SSPObjectiveFunction( this);
	}

	@Override
	public SSPSolution createSolution(InitialisationMode mode) {
		// Initialize the solution representation based on the mode
		int[] representation = new int[iNumberOfLocations];

		if(mode == InitialisationMode.RANDOM) {
			// Create a random ordering of location indices

			// First, initialize the array with sequential indices
			for(int i = 0; i < iNumberOfLocations; i++) {
				representation[i] = i;
			}

			// Then, shuffle the array using Fisher-Yates algorithm
			for(int i = iNumberOfLocations - 1; i > 0; i--) {
				int j = oRandom.nextInt(i + 1);
				// Swap elements at i and j
				int temp = representation[i];
				representation[i] = representation[j];
				representation[j] = temp;
			}

		}
		else if(mode == InitialisationMode.CONSTRUCTIVE) {
			// Use nearest neighbor greedy algorithm

			// Keep track of which locations have been visited
			boolean[] visited = new boolean[iNumberOfLocations];

			// Start from the hotel
			Location currentLocation = oHotelLocation;

			// Build the route one location at a time
			for(int i = 0; i < iNumberOfLocations; i++) {
				// Find the nearest unvisited location
				int nearestLocationIndex = -1;
				double minDistance = Double.MAX_VALUE;

				for(int j = 0; j < iNumberOfLocations; j++) {
					if(!visited[j]) {
						double distance = calculateDistance(currentLocation, aoLocations[j]);
						if(distance < minDistance) {
							minDistance = distance;
							nearestLocationIndex = j;
						}
					}
				}

				// Add the nearest location to the route
				representation[i] = nearestLocationIndex;
				visited[nearestLocationIndex] = true;
				currentLocation = aoLocations[nearestLocationIndex];
			}

			// Build solution object
			SolutionRepresentation solutionRep = new SolutionRepresentation(representation);
			SSPSolution solution = new SSPSolution(solutionRep, 0);

			// Evaluate and set correct objective value
			int cost = f.getObjectiveFunctionValue(solutionRep);
			solution.setObjectiveFunctionValue(cost);

			return solution;
		}

		// Create the solution representation
		SolutionRepresentation solutionRep = new SolutionRepresentation(representation);

		// Build the SSPSolution
		SSPSolution solution = new SSPSolution(solutionRep, 0);

		// Evaluate and set the true objective value
		int cost = f.getObjectiveFunctionValue(solutionRep);
		solution.setObjectiveFunctionValue(cost);

		return solution;
	}

	/**
	 * Calculate the Euclidean distance between two locations
	 */
	private double calculateDistance(Location loc1, Location loc2) {
		double dx = loc1.x() - loc2.x();
		double dy = loc1.y() - loc2.y();
		return Math.ceil(Math.sqrt(dx * dx + dy * dy));
	}
	
	@Override
	public ObjectiveFunctionInterface getSSPObjectiveFunction() {
		return f;
	}

	@Override
	public int getNumberOfLocations() {
		return iNumberOfLocations;
	}

	@Override
	public Location getSightseeingLocation(int iLocationId) {
		if (iLocationId >= 0 && iLocationId < iNumberOfLocations) {
			return aoLocations[iLocationId];
		}
		return null;
	}

	@Override
	public Location getHotelLocation() {
		return oHotelLocation;
	}

	@Override
	public Location getAirportLocation() {
		return oAirportLocation;
	}
	
	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(SSPSolutionInterface oSolution) {
		ArrayList<Location> locationList = new ArrayList<>();
		
		// Add the hotel as the starting point
		locationList.add(oHotelLocation);
		
		// Add each location in the order defined by the solution
		int[] solutionRepresentation = oSolution.getSolutionRepresentation().getSolutionRepresentation();
        for(int j : solutionRepresentation) {
            locationList.add(aoLocations[j]);
        }
		
		// Add the airport as the ending point
		locationList.add(oAirportLocation);
		
		return locationList;
	}
}