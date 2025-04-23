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

		try {
			// Call super.clone() to create a shallow copy first
			SSPSolution clone = (SSPSolution) super.clone();

			// Now perform deep cloning of the mutable fields and clone the solution representation
			clone.oRepresentation = this.oRepresentation.clone();

			return clone;
		}
		catch (CloneNotSupportedException e) {
			// This should never happen as we implement Cloneable
			throw new RuntimeException("Clone not supported", e);
		}
	}

	@Override
	public int getNumberOfLocations() {

		// Delegate to the solution representation to get the number of locations
		return this.oRepresentation.getNumberOfLocations();
	}
}
