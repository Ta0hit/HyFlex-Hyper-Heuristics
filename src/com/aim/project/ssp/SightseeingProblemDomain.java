package com.aim.project.ssp;


import com.aim.project.ssp.heuristics.*;
import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.interfaces.*;

import AbstractClasses.ProblemDomain;

/**
 * @author Warren G Jackson
 * @since 17/03/2025
 *
 * Ensure that you reference https://people.cs.nott.ac.uk/pszwj1/chesc2011/javadoc/index.html?help-doc.html
 * when implementing each method to be HyFlex API compliant.
 */
public class SightseeingProblemDomain extends ProblemDomain implements Visualisable, InLabPracticalExamInterface {

	private final HeuristicInterface[] heuristics;
	public SSPInstanceInterface m_oInstance;
	public SSPSolutionInterface m_oBestSolution;

	public SightseeingProblemDomain(long seed) {
        super(seed);
    
        // Set default memory size (typically 2 for simple implementations)
        setMemorySize(2);

		// Create the array of low-level heuristics with your implemented heuristics
		HeuristicInterface[] heuristics = new HeuristicInterface[] {
				// Mutation operators
				new AdjacentSwap(rng),         // Swap adjacent locations
				new Reinsertion(rng),          // Remove and reinsert a location

				// Local search operators
				new DavissHillClimbing(rng),   // Davis's hill climbing
				new NextDescent(rng),          // Next descent local search

				// Crossover operators
				new OX(rng)                    // Order crossover
		};

		// Store the heuristics internally so they can be accessed through the HyFlex interface
		this.heuristics = heuristics;
	}
	
	public SSPSolutionInterface getSolution(int index) {

		// TODO
		return null;
	}
	
	public SSPSolutionInterface getBestSolution() {

		// Return the best solution found so far
		return m_oBestSolution;
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {

		// TODO
		return -1.0d;
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {


		// TODO
		return -1.0d;
	}

	@Override
	public String bestSolutionToString() {

		// TODO
		return null;
	}

	@Override
	public boolean compareSolutions(int iIndex1, int iIndex2) {

		// TODO
		return false;
	}

	@Override
	public void copySolution(int a, int b) {

		// TODO - BEWARE this should copy the solution, not the reference to it!
		//			That is, that if we apply a heuristic to the solution in index 'b',
		//			then it does not modify the solution in index 'a' or vice-versa.
	}

	@Override
	public double getBestSolutionValue() {

		// TODO: Return the objective function value of the best solution
		if (m_oBestSolution != null) {
			return m_oBestSolution.getObjectiveFunctionValue();
		}
		return -1.0d;
	}

	@Override
	public double getFunctionValue(int index) {

		// TODO
		return -1.0d;
	}

	// TODO
	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {

		// TODO
		return null;
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {

		// TODO
		return null;
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {

		return null;
	}

	@Override
	public int getNumberOfHeuristics() {

		// TODO - has to be hard-coded due to the design of the HyFlex framework
		return -1;
	}

	@Override
	public int getNumberOfInstances() {

		// TODO
		return -1;
	}

	@Override
	public void initialiseSolution(int index) {

		// TODO - make sure that you also update the best solution/first initial solution!
		// When initializing the first solution, you should also set it as the best solution:
		// if (m_oBestSolution == null) {
		//     m_oBestSolution = getSolution(index);
		// }
	}

	@Override
	public void loadInstance(int instanceId) {

		// TODO create instance reader and problem instance
		// Initialize m_oInstance with the loaded instance
		// For example:
		// m_oInstance = new SSPInstance(...);

		// TODO set the objective function in each of the heuristics


	}

	@Override
	public void setMemorySize(int size) {

		// TODO

	}

	@Override
	public String solutionToString(int index) {

		// TODO
		return null;
	}

	@Override
	public String toString() {

		// TODO update username(s)
		return "[Username's] SSP Domain";
	}

	@Override
	public SSPInstanceInterface getLoadedInstance() {

		// Return the loaded instance
		return m_oInstance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {

		// TODO
		return null;
	}

	/**
	 * Should print the best solution found in the form:
	 * (h_x,h_y) - (l_x0,l_y0) - ... - (l_x{n-1},l_y{n-1}) - (a_x,a_y)
	 * where:
	 * `h` is the hotel
	 * `a` is the airport
	 * `l_xi` is the x-coordinate of the location in the i^th index in the solution.
	 * `l_yi` is the y-coordinate of the location in the i^th index in the solution.
	 * <p>
	 * For example:
	 * (0,0) - (1,1) - (2,2) - (3,3) - (4,4)
	 */
	@Override
	public void printBestSolutionFound() {

		// Print the best solution found
		if (m_oBestSolution != null) {
			// TODO: Implement the printing logic
		} else {
			System.out.println("No solution found yet");
		}
	}

	/**
	 * Prints the objective value of the best solution found.
	 */
	@Override
	public void printObjectiveValueOfTheSolutionFound() {

		// Print the objective value of the best solution found
		if (m_oBestSolution != null) {
			System.out.println("Best solution objective value: " + m_oBestSolution.getObjectiveFunctionValue());
		} else {
			System.out.println("No solution found yet");
		}
	}

	/**
	 * Should print the initial solution:
	 * (h_x,h_y) - (l_x0,l_y0) - ... - (l_x{n-1},l_y{n-1}) - (a_x,a_y)
	 * where:
	 * `h` is the hotel
	 * `a` is the airport
	 * `l_xi` is the x-coordinate of the location in the i^th index in the solution.
	 * `l_yi` is the y-coordinate of the location in the i^th index in the solution.
	 * <p>
	 * For example:
	 * (0,0) - (2,2) - (1,1) - (3,3) - (4,4)
	 */
	@Override
	public void printInitialSolution() {

		// TODO
	}

	/**
	 * Prints the objective value of the (first) initial solution generated.
	 */
	@Override
	public void printObjectiveValueOfTheInitialSolution() {

		// TODO
	}
}