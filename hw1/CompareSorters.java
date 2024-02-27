package hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner; 

/**
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 * @author Ethan Griesman 6/8/2023
 * 
 */
public class CompareSorters {
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {
		// Array to store PointScanner objects
		PointScanner[] scanners = new PointScanner[4]; 
		// Random object for generating random numbers
		Random rand = new Random(); 
		// Scanner object for user input
		Scanner scanner = new Scanner(System.in);

		System.out.println("Performances of Four Sorting Algorithms");
		
		// Counter for the trial number
		int trial = 1; 

		while (true) {
			System.out.println("Options:\n1. Generate Random Integers (1) \n2. Read File Input (2) \n3. Exit program (3) ");
            System.out.print("Please enter your choice: ");
            int choice = scanner.nextInt(); // Read user's choice
            System.out.println();

		    if (choice == 3) {
		    	// Exit the loop if choice is 3
		    	System.out.println();
		    	System.out.print("Terminating program...");
		        break; 
		    }
		    System.out.println("Trial " + trial + ": " + choice);
		    if (choice == 1) {
		        System.out.println("Enter number of random points: ");
		        // Read the number of random points
		        int numPoints = scanner.nextInt(); 
		        // Generate random points
		        randPoint(numPoints, rand, scanners); 
		    } else if (choice == 2) {
		    	System.out.print("Enter the file name: ");
                // Read the file name
                String fileName = scanner.next();
                System.out.println();
                // Read points from a file
                filePoint(fileName, scanners);
                // Write median coordinate point to a file
                scanners[3].writeMCPToFile();
		    }
		    // Increment the trial counter
		    trial++; 
		}

	    System.out.println("  Algorithm    |  Size  | Sorting Time (ns)");
	    System.out.println("-------------------------------------------");

		// Perform scanning and print statistics for each scanner
		for (PointScanner s : scanners) {
			// Perform scanning
		    s.scan(); 
		    // Print statistics
		    System.out.println(s.stats());
		}
	}
	
	/**
	 * Reads points from a file and performs scanning using different sorting algorithms.
	 * Prints out the statistics table.
	 * 
	 * @param fileString the file name to read points from
	 * @param scanners   the array of PointScanners to store the different sorting algorithms
	 * @throws InputMismatchException  if the input file has invalid data
	 * @throws FileNotFoundException if the input file is not found
	 */
	private static void filePoint(String fileString, PointScanner[] scanners)
			throws InputMismatchException, FileNotFoundException {
		// Check if the input file exists
	    File inputFile = new File(fileString);
	    if (!inputFile.exists()) {
	        throw new FileNotFoundException("Input file does not exist.");
	    }
		// Initialize the scanners with different sorting algorithms
		scanners[0] = new PointScanner(fileString, Algorithm.QuickSort);
		scanners[1] = new PointScanner(fileString, Algorithm.MergeSort);
		scanners[2] = new PointScanner(fileString, Algorithm.InsertionSort);
		scanners[3] = new PointScanner(fileString, Algorithm.SelectionSort);

		// Print header for the output table
	    System.out.println("  Algorithm   |  Size  | Sorting Time (ns)");
	    System.out.println("-------------------------------------------");
		
		// Perform scanning and print statistics for each scanner
		for (int i = 0; i < 4; i++) {
			scanners[i].scan();
			System.out.println(scanners[i].stats());
		}
	}
		
	/**
	 * Generates random points and creates PointScanners with different sorting algorithms to analyze their performance.
	 * 
	 * @param randNumPoints the number of random points to generate
	 * @param rand the Random object used for generating random numbers
	 * @param scanners an array of PointScanner objects to store the generated PointScanners
	 */
	private static void randPoint(int randNumPoints, Random rand, PointScanner[] scanners) {
		// Generate the random points array
	    Point[] randPnts = generateRandomPoints(randNumPoints, rand);
	    
	    // Create an array of PointScanners with different sorting algorithms
	    scanners[0] = new PointScanner(randPnts, Algorithm.QuickSort);
	    scanners[1] = new PointScanner(randPnts, Algorithm.MergeSort);
	    scanners[2] = new PointScanner(randPnts, Algorithm.InsertionSort);
	    scanners[3] = new PointScanner(randPnts, Algorithm.SelectionSort);

	    // Print header for the output tables
	    System.out.println("  Algorithm    |  Size  | Sorting Time (ns)");
	    System.out.println("-------------------------------------------");

	    // Iterate through each PointScanner and display its statistics
	    for (int i = 0; i < 4; i++) {
	        scanners[i].scan();
	        System.out.println(scanners[i].stats());
	    }
	}

	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] × [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException { 
		// Check if the number of points is less than 1
		if (numPts < 1) {
		    throw new IllegalArgumentException(); // Invalid number of points
		}
		// Create an array to store random points
		Point[] randPnts = new Point[numPts];
		// Generate random points and assign them to the array
		for (int i = 0; i < numPts; i++) {
		    // Create a new Point object with random x and y coordinates in the range [-50, 50]
		    int randX = rand.nextInt(101) - 50;
		    int randY = rand.nextInt(101) - 50;
		    Point p = new Point(randX, randY);
		    // Assign the new Point object to the array
		    randPnts[i] = p;
		}
		// Return the array of random points
		return randPnts;
	}
}
