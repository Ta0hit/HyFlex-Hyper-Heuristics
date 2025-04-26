package com.aim.project.ssp;

import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;
import com.aim.project.ssp.solution.SSPSolution;

import java.util.ArrayList;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 */
public class SSPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final SSPInstanceInterface oInstance;
	
	public SSPObjectiveFunction(SSPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	@Override
	public int getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {

		ArrayList<Location> locations = oInstance.getSolutionAsListOfLocations(
				new SSPSolution(oSolution, 0)); // Temp solution with 0 cost

		int totalCost = 0;

		// Add cost from hotel to first location
		totalCost += getCostBetweenHotelAnd(oSolution.getSolutionRepresentation()[0]);

		// Add costs between consecutive locations
		for(int i = 0; i < oSolution.getNumberOfLocations() - 3; i++) {
			totalCost += getCost(
					oSolution.getSolutionRepresentation()[i],
					oSolution.getSolutionRepresentation()[i + 1]);
		}

		// Add cost from last location to airport
		totalCost += getCostBetweenAirportAnd(
				oSolution.getSolutionRepresentation()[oSolution.getNumberOfLocations() - 3]);

		return totalCost;
	}
	
	@Override
	public int getCost(int iLocationA, int iLocationB) {

		Location locA = oInstance.getSightseeingLocation(iLocationA);
		Location locB = oInstance.getSightseeingLocation(iLocationB);
		return (int) calculateDistance(locA, locB);
	}

	@Override
	public int getCostBetweenHotelAnd(int iLocation) {

		Location hotel = oInstance.getHotelLocation();
		Location loc = oInstance.getSightseeingLocation(iLocation);
		return (int) calculateDistance(hotel, loc);
	}

	@Override
	public int getCostBetweenAirportAnd(int iLocation) {

		Location airport = oInstance.getAirportLocation();
		Location loc = oInstance.getSightseeingLocation(iLocation);
		return (int) calculateDistance(loc, airport);
	}

	private double calculateDistance(Location loc1, Location loc2) {
		double dx = loc1.x() - loc2.x();
		double dy = loc1.y() - loc2.y();
		return Math.ceil(Math.sqrt(dx * dx + dy * dy));
	}
}
