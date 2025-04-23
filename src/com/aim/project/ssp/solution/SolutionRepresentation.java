package com.aim.project.ssp.solution;

import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 */
public class SolutionRepresentation implements SolutionRepresentationInterface {

	private int[] aiSolutionRepresentation;

	// Constants for the fixed offset (hotel and airport locations)
	private static final int FIXED_OFFSET = 2;


	public SolutionRepresentation(int[] aiRepresentation) {

		this.aiSolutionRepresentation = aiRepresentation;
	}

	@Override
	public int[] getSolutionRepresentation() {

		return aiSolutionRepresentation;
	}

	@Override
	public void setSolutionRepresentation(int[] aiSolutionRepresentation) {

		this.aiSolutionRepresentation = aiSolutionRepresentation;
	}

	@Override
	public int getNumberOfLocations() {

		// Represents the total number of locations (including HOTEL and AIRPORT)
		return aiSolutionRepresentation.length + FIXED_OFFSET;
	}

	@Override
	public SolutionRepresentationInterface clone() {

		try {
			// Call super.clone() to create a shallow copy
			SolutionRepresentation clone = (SolutionRepresentation) super.clone();

			// Create a deep copy of the array
			clone.aiSolutionRepresentation = new int[this.aiSolutionRepresentation.length];
			System.arraycopy(this.aiSolutionRepresentation, 0, clone.aiSolutionRepresentation, 0,
					this.aiSolutionRepresentation.length);

			return clone;
		} catch (CloneNotSupportedException e) {
			// This should never happen as we implement Cloneable
			throw new RuntimeException("Clone not supported", e);
		}
	}
}
