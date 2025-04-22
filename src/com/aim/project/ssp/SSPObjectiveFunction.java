package com.aim.project.ssp;

import com.aim.project.ssp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.ssp.interfaces.SSPInstanceInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

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
		
		// TODO calculate and return the objective value of the solution `oSolution`
		return -1;
	}
	
	@Override
	public int getCost(int iLocationA, int iLocationB) {
		
		// TODO calculate and return the distance between two locations
		return -1;
	}

	@Override
	public int getCostBetweenHotelAnd(int iLocation) {
		
		// TODO calculate and return the distance between the hotel and the location
		return -1;
	}

	@Override
	public int getCostBetweenAirportAnd(int iLocation) {
		
		// TODO calculate and return the distance between the location and the airport
		return -1;
	}
}
