package com.aim.project.ssp.test;

import com.aim.project.ssp.instance.InitialisationMode;
import com.aim.project.ssp.instance.Location;
import com.aim.project.ssp.instance.SSPInstance;
import com.aim.project.ssp.solution.SSPSolution;

import java.util.ArrayList;
import java.util.Random;

public class TestSSPInstance {

    private static SSPInstance instance;

    public static void main(String[] args) {
        // Create test data
        int numLocations = 5;
        Location[] locations = new Location[numLocations];
        
        // Initialise locations with some test coordinates
        locations[0] = new Location(1, 2);
        locations[1] = new Location(5, 5);
        locations[2] = new Location(3, 1);
        locations[3] = new Location(8, 3);
        locations[4] = new Location(2, 7);
        
        // Create hotel and airport locations
        Location hotel = new Location(0, 0);
        Location airport = new Location(10, 10);
        
        // Create a random number generator with a fixed seed for reproducibility
        Random random = new Random(42);
        
        // Create the SSP instance
        SSPInstance instance = new SSPInstance(numLocations, locations, hotel, airport, random);
        
        // Test random solution creation
        System.out.println("=== Testing Random Solution ===");
        SSPSolution randomSolution = instance.createSolution(InitialisationMode.RANDOM);
        printSolution(instance, randomSolution);
        
        // Test constructive (nearest neighbour) solution creation
        System.out.println("\n=== Testing Constructive Solution ===");
        SSPSolution constructiveSolution = instance.createSolution(InitialisationMode.CONSTRUCTIVE);
        printSolution(instance, constructiveSolution);
        
        // Verify that all locations are visited exactly once in both solutions
        verifyAllLocationsVisited(instance, randomSolution, numLocations);
        verifyAllLocationsVisited(instance, constructiveSolution, numLocations);
    }
    
    private static void printSolution(SSPInstance instance, SSPSolution solution) {
        // Get the solution as a list of locations
        ArrayList<Location> locationList = instance.getSolutionAsListOfLocations(solution);
        
        // Print the route
        System.out.print("Route: ");
        for (int i = 0; i < locationList.size(); i++) {
            System.out.print(locationList.get(i));
            if (i < locationList.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
        
        // Print the solution representation (location indices)
        System.out.print("Representation: [");
        int[] rep = solution.getSolutionRepresentation().getSolutionRepresentation();
        for (int i = 0; i < rep.length; i++) {
            System.out.print(rep[i]);
            if (i < rep.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    private static void verifyAllLocationsVisited(SSPInstance instance, SSPSolution solution, int numLocations) {
        TestSSPInstance.instance = instance;
        int[] rep = solution.getSolutionRepresentation().getSolutionRepresentation();
        
        // Check if the solution length is correct
        if (rep.length != numLocations) {
            System.out.println("ERROR: Solution length is incorrect. Expected: " + numLocations + ", Actual: " + rep.length);
            return;
        }
        
        // Check if each location is visited exactly once
        boolean[] visited = new boolean[numLocations];
        for (int j : rep) {
            if (j < 0 || j >= numLocations) {
                System.out.println("ERROR: Invalid location index: " + j);
                return;
            }
            
            if (visited[j]) {
                System.out.println("ERROR: Location " + j + " is visited more than once");
                return;
            }
            
            visited[j] = true;
        }
        
        // Check if any location is missed
        for (int i = 0; i < numLocations; i++) {
            if (!visited[i]) {
                System.out.println("ERROR: Location " + i + " is not visited");
                return;
            }
        }
        
        System.out.println("Verification passed: All locations are visited exactly once");
    }

    public static SSPInstance getInstance() {
        return instance;
    }

    public static void setInstance(SSPInstance instance) {
        TestSSPInstance.instance = instance;
    }
}