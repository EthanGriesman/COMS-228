package hw1;

import java.util.Comparator;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 * This abstract class is extended by SelectionSort, InsertionSort, MergeSort, and QuickSort.
 * It stores the input (later the sorted) sequence. 
 * 
 * @author Ethan Griesman 6/8/2023
 * 
 */
public abstract class AbstractSorter {
	// Array of points operated on by a sorting algorithm. 
	protected Point[] points; 
	
	// Stores ordered points after a call to sort().
	// "selection sort", "insertion sort", "mergesort", or
    // "quicksort". Initialized by a subclass constructor.
	protected String algorithm = null;
	
	// Used for point comparison, compares x and y coordinates
	public Comparator<Point> pointComparator = null;  
	
	// Add other protected or private instance variables you may need. 
	protected AbstractSorter() {
		// No implementation needed. Provides a default super constructor to subclasses. 
		// Removable after implementing SelectionSorter, InsertionSorter, MergeSorter, and QuickSorter.
	}
	
	/**
	 * This constructor accepts an array of points as input. Copy the points into the array points[]. 
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	protected AbstractSorter(Point[] pts) throws IllegalArgumentException {
		// Check if pts is null or empty
		if (pts == null || pts.length == 0) {
		    throw new IllegalArgumentException();
		} 
	}
	
	/**
	 * Generates a comparator on the fly that compares by x-coordinate if order == 0, by y-coordinate
	 * if order == 1. Assign the 
     * comparator to the variable pointComparator. 
     *  
	 * @param order  0   by x-coordinate 
	 * 				 1   by y-coordinate
	 * 
	 * @throws IllegalArgumentException if order is less than 0 or greater than 1
	 *        
	 */
	public void setComparator(int order) throws IllegalArgumentException {
		// Validate the value of order
	    if (order != 0 && order != 1) {
	        throw new IllegalArgumentException("Order must be 0 or 1");
	    }

	    // Set the sorting criteria based on the order value
	    boolean srtbyX = (order == 0);
	    Point.setXorY(srtbyX);

	    // Create a Comparator for Point objects based on the sorting criteria
	    pointComparator = Comparator.comparing(Point::getX).thenComparing(Point::getY);
	}

	/**
	 * Use the created pointComparator to conduct sorting.  
	 * 
	 * Should be protected. Made public for testing. 
	 */
	public abstract void sort(); 
	
	/**
	 * Obtain the point in the array points[] that has median index 
	 * 
	 * @return	median point 
	 */
	public Point getMedian() {
		return points[points.length/2]; 
	}

	/**
	 * Copys the array points[] onto the array pts[]. 
	 * 
	 * @param pts
	 */
	public void getPoints(Point[] pts) {
		// Check if the provided array is large enough
		if (pts.length < points.length) {
	        throw new IllegalArgumentException("Invalid array entry");
	    }

	    // Copy the points array to the provided pts array
	    System.arraycopy(points, 0, pts, 0, points.length);
	}
	
	/**
	 * Swaps the two elements indexed at i and j respectively in the array points[]. 
	 * 
	 * @param i 
	 * @param j
	 */
	protected void swap(int i, int j) {
		// Store the value at index i in a temporary variable
	    Point t = points[i];
	    // Replace the value at index i with the value at index j
	    points[i] = points[j];
	    // Replace the value at index j with the value stored in the temporary variable
	    points[j] = t;
	}
}
