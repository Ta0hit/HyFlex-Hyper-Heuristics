package com.aim.project.ssp.solution;

import com.aim.project.ssp.interfaces.SSPSolutionInterface;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 */
public class SSPSolution implements SSPSolutionInterface {

	private SolutionRepresentationInterface oRepresentation;
	
	private int iObjectiveFunctionValue;
	
	public SSPSolution(SolutionRepresentationInterface oRepresentation, int iObjectiveFunctionValue) {
		
		this.oRepresentation = oRepresentation;
		this.iObjectiveFunctionValue = iObjectiveFunctionValue;
	}

	@Override
	public int getObjectiveFunctionValue() {

		return iObjectiveFunctionValue;
	}

	@Override
	public void setObjectiveFunctionValue(int objectiveFunctionValue) {
		
		this.iObjectiveFunctionValue = objectiveFunctionValue;
	}

	@Override
	public SolutionRepresentationInterface getSolutionRepresentation() {
		
		return this.oRepresentation;
	}
	
	@Override
	public SSPSolutionInterface clone() {

		// Create a deep clone of the solution
		// Need to clone the solution representation to avoid having 2 solution objects referencing the same repr.
		SolutionRepresentationInterface clonedRepresentation = this.oRepresentation.clone();

		// Create a new SSPSolution with the cloned representation and the same objective value
		return new SSPSolution(clonedRepresentation, this.iObjectiveFunctionValue);

	}

	@Override
	public int getNumberOfLocations() {

		// Delegate to the solution representation to get the number of locations
		return this.oRepresentation.getNumberOfLocations();
	}
}
