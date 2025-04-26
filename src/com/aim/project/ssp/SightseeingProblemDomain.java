package com.aim.project.ssp;


import com.aim.project.ssp.heuristics.*;
import com.aim.project.ssp.instance.InitialisationMode;
import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.instance.reader.SSPInstanceReader;
import com.aim.project.ssp.interfaces.*;

import AbstractClasses.ProblemDomain;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private SSPSolutionInterface[] solutionMemory;
	private final HeuristicInterface[] heuristics;

	public SightseeingProblemDomain(long seed) {
        super(seed);

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
			throw new IllegalStateException("Source solution is null. Did you initialise the solutions?");
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
			throw new IllegalStateException("Source solutions are null. Did you initialise the solutions?");
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
		SSPSolutionInterface solution1 = getSolution(iIndex1);
		SSPSolutionInterface solution2 = getSolution(iIndex2);

		// Ensure both solutions are not null
		if(solution1 == null || solution2 == null) {
			throw new IllegalStateException("One or both solutions are null. Ensure solutions are initialised.");
		}

		// Compare the structures of the two solutions
		return solution1.equals(solution2);
	}

	@Override
	public void copySolution(int a, int b) {

		// BEWARE this should copy the solution, not the reference to it!
		//	That is, that if we apply a heuristic to the solution in index 'b',
		//	then it does not modify the solution in index 'a' or vice-versa.
		// Retrieve the source solution
		SSPSolutionInterface sourceSolution = getSolution(a);

		// Ensure the source solution is not null
		if(sourceSolution == null) {
			throw new IllegalStateException("Source solution is null. Ensure the solution is initialised.");
		}

		// Clone the source solution and store it at the destination index
		solutionMemory[b] = sourceSolution.clone();
	}

	@Override
	public double getBestSolutionValue() {

		if (m_oBestSolution != null) {
			return m_oBestSolution.getObjectiveFunctionValue();
		}
		return -1.0d;
	}

	@Override
	public double getFunctionValue(int index) {

		// Validate the solution index
		if(index < 0 || index >= solutionMemory.length) {
			throw new IndexOutOfBoundsException("Invalid solution index: " + index);
		}

		// Retrieve the solution
		SSPSolutionInterface solution = solutionMemory[index];

		// Ensure the solution is not null
		if(solution == null) {
			throw new IllegalStateException("Solution at index " + index + " is null. Ensure it is initialised.");
		}

		//Return the objective function value of the solution
		return solution.getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {

		// Define which heuristics belong to which types based on their indices
        return switch (type) {
            case MUTATION ->
                // AdjacentSwap (0) and Reinsertion (1) are mutation operators
                    new int[]{0, 1};
            case LOCAL_SEARCH ->
                // DavissHillClimbing (2) and NextDescent (3) are local search operators
                    new int[]{2, 3};
            case CROSSOVER ->
                // OX (4) is a crossover operator
                    new int[]{4};
            case RUIN_RECREATE ->
                // No ruin-recreate heuristics in current implementation
                    null;
            default -> null;
        };
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {

		List<Integer> matchingIndices = new ArrayList<>();

		for(int i = 0; i < heuristics.length; i++) {
			if(heuristics[i].usesDepthOfSearch()) {
				matchingIndices.add(i);
			}
		}

		if(matchingIndices.isEmpty()) {
			return null;
		}

		return matchingIndices.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {

		List<Integer> matchingIndices = new ArrayList<>();

		for(int i = 0; i < heuristics.length; i++) {
			if(heuristics[i].usesIntensityOfMutation()) {
				matchingIndices.add(i);
			}
		}

		if(matchingIndices.isEmpty()) {
			return null;
		}

		return matchingIndices.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int getNumberOfHeuristics() {

		// Needs to be hardcoded because of HyFlex
		return 5;
	}

	@Override
	public int getNumberOfInstances() {

		// Update this to the number of instances you have
		return 7;
	}

	@Override
	public void initialiseSolution(int index) {

		// Create a new solution
		SSPSolutionInterface solution = m_oInstance.createSolution(InitialisationMode.RANDOM);

		// Store it in the solution memory
		solutionMemory[index] = solution;

		// Update the best solution if this is the first solution or if the new solution is better
		if(m_oBestSolution == null || solution.getObjectiveFunctionValue() < m_oBestSolution.getObjectiveFunctionValue()) {
			m_oBestSolution = solution.clone();
		}
}

	@Override
	public void loadInstance(int instanceId) {

		// Set default memory size (typically 2 for simple implementations)
		int memorySize = 2;
		setMemorySize(memorySize);

		// Validate instance ID
		if(instanceId < 0 || instanceId >= getNumberOfInstances()) {
			throw new IllegalArgumentException("Invalid instance ID: " + instanceId);
		}

		// Map instance IDs to their corresponding files
		String[] instanceFiles = {
				"square.ssp",
				"libraries-15.ssp",
				"carparks-40.ssp",
				"tramstops-85.ssp",
				"grid.ssp",
				"clustered.ssp",
				"chatgpt-instance-100.ssp"
		};

		// Get the filename for the requested instance
		String filename = instanceFiles[instanceId];

		// Create instance reader and load the instance
		SSPInstanceReader reader = new SSPInstanceReader();

		// TODO CHANGE PATH FOR LAPTOP
		String projectRoot = System.getProperty("user.dir"); // Gets the project root directory
		Path instancePath = Path.of("C:\\Users\\Taohi\\HyFlex-Hyper-Heuristics\\src\\instances\\ssp", filename);
		System.out.println("Loading instance from: " + instancePath.toString());

		try {
			// Read the instance file
			m_oInstance = reader.readSSPInstance(instancePath, rng);

			// Set the objective function in each heuristic
			ObjectiveFunctionInterface objectiveFunction = m_oInstance.getSSPObjectiveFunction();
			for(HeuristicInterface heuristic : heuristics) {
				heuristic.setObjectiveFunction(objectiveFunction);
			}

			// Reset solution memory and best solution
			Arrays.fill(solutionMemory, null);
			m_oBestSolution = null;

		} catch (Exception e) {
			throw new RuntimeException("Failed to load instance: " + filename, e);
		}
	}

	@Override
	public void setMemorySize(int size) {

		// Validate the memory size
		if(size <= 0) {
			throw new IllegalArgumentException("Memory size must be greater than 0");
		}

		// allocate the memory array but only initialise if instance is ready
		SSPSolutionInterface[] newMemory = new SSPSolutionInterface[size];
		// (copy over if you want)
		solutionMemory = newMemory;

		// only initialise if loadInstance() has already set m_oInstance
		if (m_oInstance != null) {
			for (int i = 0; i < size; i++) {
				initialiseSolution(i);
			}
		}
	}

	@Override
	public String solutionToString(int index) {

		// Validate the solution index
		if(index < 0 || index >= solutionMemory.length) {
			throw new IndexOutOfBoundsException("Invalid solution index: " + index);
		}

		// Retrieve the solution
		SSPSolutionInterface solution = solutionMemory[index];

		// Ensure the solution is not null
		if(solution == null) {
			throw new IllegalStateException("Solution at index " + index + " is null. Ensure it is initialised.");
		}

		// Return the string representation of the solution
		return solution.toString();
	}

	@Override
	public String toString() {

		return "SSP Problem Domain";
	}

	@Override
	public SSPInstanceInterface getLoadedInstance() {

		// Return the loaded instance
		return m_oInstance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {

		// Check if the best solution exists
		if(m_oBestSolution != null) {
			// Get the ordered list of locations from the best solution
			ArrayList<Location> locationList = m_oInstance.getSolutionAsListOfLocations(m_oBestSolution);
			return locationList.toArray(new Location[0]);
		}
		else {
			throw new IllegalStateException("No best solution found. Ensure a solution is initialised.");
		}
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

		if(m_oBestSolution == null) {
			System.out.println("No solution found yet");
			return;
		}

		try {
			// Get the ordered locations (hotel -> POIs -> airport)
			ArrayList<Location> locations = m_oInstance.getSolutionAsListOfLocations(m_oBestSolution);

			// Build the output string
			StringBuilder sb = new StringBuilder();

			// Append hotel
			Location hotel = locations.getFirst();
			sb.append(String.format("(%d,%d)", hotel.x(), hotel.y()));

			// Append all points of interest
			for(int i = 1; i < locations.size() - 1; i++) {
				Location loc = locations.get(i);
				sb.append(" - ").append(String.format("(%d,%d)", loc.x(), loc.y()));
			}

			// Append airport
			Location airport = locations.getLast();
			sb.append(" - ").append(String.format("(%d,%d)", airport.x(), airport.y()));

			// Print the formatted string
			System.out.println(sb.toString());

		} catch (Exception e) {
			System.out.println("Error printing solution: " + e.getMessage());
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

		// Check if we have at least one solution in memory
		if (solutionMemory == null || solutionMemory.length == 0 || solutionMemory[0] == null) {
			System.out.println("No initial solution available. Call initialiseSolution() first.");
			return;
		}

		try {
			// Get the first solution in memory (typically the initial one)
			SSPSolutionInterface initialSolution = solutionMemory[0];

			// Get the ordered locations (hotel -> POIs -> airport)
			ArrayList<Location> locations = m_oInstance.getSolutionAsListOfLocations(initialSolution);

			// Build the output string
			StringBuilder sb = new StringBuilder();

			// Append hotel
			Location hotel = locations.getFirst();
			sb.append(String.format("(%d,%d)", hotel.x(), hotel.y()));

			// Append all points of interest
			for (int i = 1; i < locations.size() - 1; i++) {
				Location loc = locations.get(i);
				sb.append(" - ").append(String.format("(%d,%d)", loc.x(), loc.y()));
			}

			// Append airport
			Location airport = locations.getLast();
			sb.append(" - ").append(String.format("(%d,%d)", airport.x(), airport.y()));

			// Print the formatted string
			System.out.println(sb.toString());

		} catch (Exception e) {
			System.out.println("Error printing initial solution: " + e.getMessage());
		}
	}

	/**
	 * Prints the objective value of the (first) initial solution generated.
	 */
	@Override
	public void printObjectiveValueOfTheInitialSolution() {

		// Check if we have at least one solution in memory
		if (solutionMemory == null || solutionMemory.length == 0 || solutionMemory[0] == null) {
			System.out.println("No initial solution available. Call initialiseSolution() first.");
			return;
		}

		// Print the objective value of the first initial solution found
		System.out.println("Initial solution objective value: " + solutionMemory[0].getObjectiveFunctionValue());
	}
}