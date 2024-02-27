package hw1;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


/**
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 * 
 * @author Ethan Griesman 6/8/2023
 *
 */
public class PointScanner {
	// Array of points
	private Point[] points;

	/**
	 * Point whose x and y coordinates are respectively the medians of 
	 * the x coordinates and y coordinates of those points in the array points[]
	 */
	private Point medianCoordPnt;

	private Algorithm sortAlgorithm;
	/**
	 * Execution time in nanoseconds.
	 */
	protected long scanTime;

	/**
	 * The name of the output file returning the median coordinate point (MCP)
	 */
	private static String outputFile = "outputName";
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException {
		// Makes sure pts is valid
		if (pts == null || pts.length == 0) {
		    throw new IllegalArgumentException();
		} else {
		    // Create a new array to store the points
		    points = new Point[pts.length];
		    // Copy the points from the input array to the class member variable
		    for (int i = 0; i < pts.length; ++i) {
		        points[i] = pts[i];
		    }
		}

		try {
		    // Makes sure algo is valid
		    sortAlgorithm = algo;
		} catch (Exception InputMismatchException) {
		    // Print an error message if the algorithm is invalid
		    System.out.println("Invalid Algorithm");
		}
	}

	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException {
		
		// Create a File object for the input file
		File inputFile = new File(inputFileName);
		
		// Create an ArrayList to store the points
		ArrayList<Point> pointsList = new ArrayList<>();

		try (Scanner fileScanner = new Scanner(inputFile)) {
		    int count = 0;
		    // Read integers from the file until there are no more
		    while (fileScanner.hasNextInt()) {
		        // Read the x-coordinate
		        int x = fileScanner.nextInt();
		        // Check if there is a corresponding y-coordinate
		        if (fileScanner.hasNextInt()) {
		            // Read the y-coordinate
		            int y = fileScanner.nextInt();
		            // Create a new Point object and add it to the list
		            pointsList.add(new Point(x, y));
		            // Increment the count by 2 since both x and y coordinates are read
		            count += 2;
		        } else {
		            // Skip the last integer if it doesn't have a pair
		            break;
		        }
		    }

		    // Check if the number of integers read is odd
		    if (count % 2 != 0) {
		        throw new InputMismatchException("Invalid file: odd number of integers detected");
		    }
		} catch (FileNotFoundException e) {
		    throw new FileNotFoundException("Input file not found.");
		}

		// Convert the ArrayList to an array of Points and assign it to the points variable
		points = pointsList.toArray(new Point[0]);
		// Assign the sorting algorithm
		sortAlgorithm = algo;
	}

	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.     
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param algo
	 * @return
	 */
	public void scan() {
		
		// Check if points array is empty or null
	    if (points == null || points.length == 0) {
	        throw new IllegalArgumentException("Empty or null points array.");
	    }
	    
		AbstractSorter aSorter = null; 
		long t0 = 0;
		long t1 = 0;
		
		// create an object to be referenced by aSorter according to sortingAlgorithm. for each of the two 
		// rounds of sorting, have aSorter do the following: 
		// 
		//     a) call setComparator() with an argument 0 or 1. 
		//
		//     b) call sort(). 		
		// 
		//     c) use a new Point object to store the coordinates of the medianCoordinatePoint
		//
		//     d) set the medianCoordinatePoint reference to the object with the correct coordinates.
		//
		//     e) sum up the times spent on the two sorting rounds and set the instance variable scanTime. 
		
		switch (sortAlgorithm) {
	    case SelectionSort:
	        // If the sort algorithm is SelectionSort, create a SelectionSorter object
	        aSorter = new SelectionSorter(points);
	        break;
	    case InsertionSort:
	        // If the sort algorithm is InsertionSort, create an InsertionSorter object
	        aSorter = new InsertionSorter(points);
	        break;
	    case MergeSort:
	        // If the sort algorithm is MergeSort, create a MergeSorter object
	        aSorter = new MergeSorter(points);
	        break;
	    case QuickSort:
	        // If the sort algorithm is QuickSort, create a QuickSorter object
	        aSorter = new QuickSorter(points);
	        break;
	    default:
	        // If the sort algorithm is not recognized, throw an exception
	        throw new IllegalArgumentException("Invalid sorting algorithm.");
		}

		scanTime = 0;
		// Set the initial value of scanTime to 0
		
		// Sort points by x-coordinate
	    aSorter.setComparator(0);
	    t0 = System.nanoTime();
	    aSorter.sort();
	    t1 = System.nanoTime();
	    long firstRoundTime = t1 - t0; // The time it takes at the end.
		int xMedian = aSorter.getMedian().getX(); // Getting the median for x.
	    
		// Sort points by y-coordinate
	    aSorter.setComparator(1);
	    t0 = System.nanoTime();
	    aSorter.sort();
	    t1 = System.nanoTime();
	    long secondRoundTime = t1 - t0; // The time it takes at the end.
	    int yMedian = aSorter.getMedian().getY(); // Getting the median for x.
	    
	    // The total scan time.
	    scanTime = firstRoundTime + secondRoundTime;
	    
	    // Creating the new median object for the points.
	    medianCoordPnt = new Point(xMedian, yMedian);
	}
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats() { 
		// Convert the sorting algorithm to a string representation
		String algorithmName = sortAlgorithm.toString();

		// Format the size of the points array to have a fixed width of 6 characters
		String size = String.format(" %-6s", points.length); 

		// Convert the scanTime to a string
		String time = String.valueOf(scanTime);

		// Format the output string with the algorithm name, size, and time
		return String.format("%-16s %-6s %s", algorithmName, size, time);
	}
	
	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString() {
		// Check if the medianCoordinatePoint is null
	    if (medianCoordPnt == null) {
	        return "";
	    }
	    
	    // Format the median coordinate point string
	    return "MCP: " + medianCoordPnt.toString();
	}

	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException {
		try {
		    // Create a FileOutputStream object with the specified output file
		    FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

		    // Create a PrintWriter object, wrapping the FileOutputStream, to write to the file
		    PrintWriter writer = new PrintWriter(fileOutputStream);

		    // Write the string representation of 'this' object to the file
		    writer.write(this.toString());

		    // Close the PrintWriter to ensure all data is written and resources are released
		    writer.close();
		} catch (IOException e) {
		    // Print the stack trace if an exception occurs during file handling
		    e.printStackTrace();
		}
	}
}
