package com.aim.project.ssp;


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

	public SightseeingProblemDomain(long seed) {

        super(seed);

        // TODO - set default memory size and create the array of low-level heuristics
		// ...
	}
	
	public SSPSolutionInterface getSolution(int index) {

		// TODO
		return null;
	}
	
	public SSPSolutionInterface getBestSolution() {

		// TODO 
		return null;
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

		// TODO
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

	}

	@Override
	public void loadInstance(int instanceId) {

		// TODO create instance reader and problem instance

		
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

		// TODO
		return null;
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

		// TODO
	}

	/**
	 * Prints the objective value of the best solution found.
	 */
	@Override
	public void printObjectiveValueOfTheSolutionFound() {

		// TODO
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
