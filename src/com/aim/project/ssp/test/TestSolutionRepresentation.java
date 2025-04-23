package com.aim.project.ssp.test;

import com.aim.project.ssp.solution.SolutionRepresentation;
import com.aim.project.ssp.interfaces.SolutionRepresentationInterface;

public class TestSolutionRepresentation {

    public static void main(String[] args) {
        // Test basic functionality
        testBasicFunctionality();

        // Test cloning
        testCloning();

        System.out.println("All tests passed successfully!");
    }

    private static void testBasicFunctionality() {
        // Create a sample representation
        int[] testArray = {1, 3, 5, 7, 9};
        SolutionRepresentation solution = new SolutionRepresentation(testArray);

        // Test getNumberOfLocations()
        int expectedLocations = testArray.length + 2; // +2 for hotel and airport
        int actualLocations = solution.getNumberOfLocations();
        assert expectedLocations == actualLocations :
                "getNumberOfLocations() returned " + actualLocations + ", expected " + expectedLocations;
        System.out.println("getNumberOfLocations test passed!");

        // Test getSolutionRepresentation()
        int[] returnedArray = solution.getSolutionRepresentation();
        assert returnedArray.length == testArray.length :
                "Returned array length mismatch: " + returnedArray.length + " vs " + testArray.length;
        for (int i = 0; i < testArray.length; i++) {
            assert returnedArray[i] == testArray[i] :
                    "Array element at index " + i + " doesn't match: " + returnedArray[i] + " vs " + testArray[i];
        }
        System.out.println("getSolutionRepresentation test passed!");

        // Test setSolutionRepresentation()
        int[] newArray = {2, 4, 6, 8, 10};
        solution.setSolutionRepresentation(newArray);
        int[] updatedArray = solution.getSolutionRepresentation();
        for (int i = 0; i < newArray.length; i++) {
            assert updatedArray[i] == newArray[i] :
                    "Array element at index " + i + " doesn't match after set: " + updatedArray[i] + " vs " + newArray[i];
        }
        System.out.println("setSolutionRepresentation test passed!");
    }

    private static void testCloning() {
        // Create a sample representation
        int[] testArray = {1, 3, 5, 7, 9};
        SolutionRepresentation original = new SolutionRepresentation(testArray);

        // Clone the solution
        SolutionRepresentationInterface clone = original.clone();

        // Verify the clone has the same values
        int[] originalArray = original.getSolutionRepresentation();
        int[] clonedArray = clone.getSolutionRepresentation();

        assert clonedArray.length == originalArray.length :
                "Cloned array length mismatch: " + clonedArray.length + " vs " + originalArray.length;

        for (int i = 0; i < originalArray.length; i++) {
            assert clonedArray[i] == originalArray[i] :
                    "Cloned array element at index " + i + " doesn't match: " + clonedArray[i] + " vs " + originalArray[i];
        }

        // Verify deep cloning - modifying the original shouldn't affect the clone
        originalArray[0] = 999;
        clonedArray = clone.getSolutionRepresentation();
        assert clonedArray[0] != 999 :
                "Deep cloning failed: modification to original affected the clone";

        // Verify deep cloning in the other direction - modifying the clone shouldn't affect the original
        int[] newArray = {100, 200, 300, 400, 500};
        clone.setSolutionRepresentation(newArray);
        originalArray = original.getSolutionRepresentation();
        assert originalArray[0] == 999 :
                "Deep cloning failed: modification to clone affected the original";

        System.out.println("Cloning tests passed!");
    }
}