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

    public SSPInstanceInterface m_oInstance;
	public SSPSolutionInterface m_oBestSolution;
	private final SSPSolutionInterface[] solutionMemory;
	private HeuristicInterface[] heuristics;

	public SightseeingProblemDomain(long seed) {
        super(seed);

		// Set default memory size (typically 2 for simple implementations)
		int memorySize = 2;
		setMemorySize(memorySize);
        solutionMemory = new SSPSolutionInterface[memorySize];

		// Initialise the array of low-level heuristics
        heuristics = new HeuristicInterface[]{
                // Mutation operators
                new AdjacentSwap(rng),
                new Reinsertion(rng),

                // Local search operators
                new DavissHillClimbing(rng),
                new NextDescent(rng),

                // Crossover operators
                new OX(rng)
        };
	}
	
	public SSPSolutionInterface getSolution(int index) {

		// Return the solution at the specified index
		if(index >= 0 && index < solutionMemory.length) {
			return solutionMemory[index];
		}
		else {
			throw new IndexOutOfBoundsException("Invalid solution index: " + index);
		}
	}
	
	public SSPSolutionInterface getBestSolution() {

		// Return the best solution found so far
		return m_oBestSolution;
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {

		// Check if heuristic index is valid
		if(hIndex < 0 || hIndex >= heuristics.length) {
			throw new IllegalArgumentException("Invalid heuristic index: " + hIndex);
		}

		// Get the heuristic to apply
		HeuristicInterface heuristic = heuristics[hIndex];

		// Get the source solution
		SSPSolutionInterface currentSolution = getSolution(currentIndex);

		// If this is a first-time operation, ensure solutions exist
		if(currentSolution == null) {
			throw new IllegalStateException("Source solution is null. Did you initialize the solutions?");
		}

		// Create a copy of the source solution to modify
		SSPSolutionInterface candidateSolution = currentSolution.clone();

		// Apply the heuristic to the candidate solution (default parameters for now)
		double depthOfSearch = 0.0;
		double intensityOfMutation = 0.0;

		// Apply the heuristic and get the new objective value
		double objectiveValue = heuristic.apply(candidateSolution, depthOfSearch, intensityOfMutation);

		// Save the candidate solution at the destination index
		solutionMemory[candidateIndex] = candidateSolution;

		// Record the heuristic call in the parent class
		heuristicCallRecord[hIndex]++;

		// Update best solution if necessary
		if(objectiveValue < getBestSolutionValue() && m_oBestSolution != null) {
			m_oBestSolution = candidateSolution.clone();
		}

		return objectiveValue;
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {

		// Check if heuristic index is valid
		if(hIndex < 0 || hIndex >= heuristics.length) {
			throw new IllegalArgumentException("Invalid heuristic index: " + hIndex);
		}

		// Get the heuristic to apply
		HeuristicInterface heuristic = heuristics[hIndex];

		// Get the source solutions
		SSPSolutionInterface parent1Solution = getSolution(parent1Index);
		SSPSolutionInterface parent2Solution = getSolution(parent2Index);

		// If this is a first-time operation, ensure solutions exist
		if(parent1Solution == null || parent2Solution == null) {
			throw new IllegalStateException("Source solutions are null. Did you initialize the solutions?");
		}

		// Create a copy of the first parent solution to modify
		SSPSolutionInterface candidateSolution = parent1Solution.clone();

		// Apply the heuristic to the candidate solution (default parameters for now)
		double depthOfSearch = 0.0;
		double intensityOfMutation = 0.0;

		// Apply the heuristic and get the new objective value
		double objectiveValue = heuristic.apply(candidateSolution, depthOfSearch, intensityOfMutation);

		// Save the candidate solution at the destination index
		solutionMemory[candidateIndex] = candidateSolution;

		// Record the heuristic call in the parent class
		heuristicCallRecord[hIndex]++;

		// Update best solution if necessary
		if(objectiveValue < getBestSolutionValue() && m_oBestSolution != null) {
			m_oBestSolution = candidateSolution.clone();
		}

		return objectiveValue;
	}

	@Override
	public String bestSolutionToString() {

		// Check if a best solution exists
		if (m_oBestSolution != null) {
			// Return the objective function value of the best solution
			return String.valueOf(m_oBestSolution.getObjectiveFunctionValue());
		}
		// Return a default value if no best solution exists
		return "No best solution found";
    }

	@Override
	public boolean compareSolutions(int iIndex1, int iIndex2) {

		// Retrieve the solutions from the memory
		SSPSolutionInterface solution1 = getSolution(solutionIndex1);
		SSPSolutionInterface solution2 = getSolution(solutionIndex2);

		// Ensure both solutions are not null
		if(solution1 == null || solution2 == null) {
			throw new IllegalStateException("One or both solutions are null. Ensure solutions are initialized.");
		}

		// Compare the structures of the two solutions
		return solution1.equals(solution2);
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

		// Needs to be hardcoded because of HyFlex
		return 5;
	}

	@Override
	public int getNumberOfInstances() {

		// TODO
		return -1;
	}

	@Override
	public void initialiseSolution(int index) {
    // Check that the instance is loaded
    if (m_oInstance == null) {
        throw new IllegalStateException("Problem instance not loaded. Call loadInstance first.");
    }
    
    // Create a new solution
    SSPSolutionInterface solution = m_oInstance.createSolution(SSPInstanceInterface.InitialisationMode.CONSTRUCTIVE);
    
    // Store it in the solution memory
    solutionMemory[index] = solution;
    
    // Update the best solution if needed
    if (m_oBestSolution == null) {
        m_oBestSolution = solution.clone();
    }
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