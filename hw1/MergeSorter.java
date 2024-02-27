package hw1;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * This class implements the mergesort algorithm.   
 *
 * @author Ethan Griesman 6/8/2023
 * 
 */
public class MergeSorter extends AbstractSorter {
	
	// Other private instance variables if needed
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) {
		// Call to superclass constructor
	    super(pts);
	    
	    // Set the algorithm in the superclass
	    super.algorithm = "mergesort";
	    
	    // Check and initialize the points array
	    if (pts == null) {
	        // Throw an exception if the points array is null
	        throw new IllegalArgumentException("Points array cannot be null.");
	    }
	    
	    // Assign the points array to the instance variable
	    this.points = pts;
	}
	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort() {
		mergeSortRec(points);
	}
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts) {
		if (pts.length <= 1) {
	        // Base case: Array with 0 or 1 element is already sorted
	        return;
	    }

	    // Divide the array into two halves
	    int m = pts.length / 2;
	    Point[] l = new Point[m];
	    Point[] r = new Point[pts.length - m];

	    // Copy elements to the left and right subarrays
	    System.arraycopy(pts, 0, l, 0, m);
	    System.arraycopy(pts, m, r, 0, pts.length - m);

	    // Recursively sort the left and right subarrays
	    mergeSortRec(l);
	    mergeSortRec(r);

	    // Merge the sorted subarrays back into the original array
	    merge(pts, l, r);
	}
	/**
	 * 
	 * @param pts
	 * @param ptsLeft
	 * @param ptsRight
	 */
	private void merge(Point[] pts, Point[] ptsLeft, Point[] ptsRight) {
		int x = 0, y = 0, z = 0; // Index variables

		// Merge elements from the left and right subarrays into the merged array
		while (x < ptsLeft.length || y < ptsRight.length) {
		    // If elements are available in both left and right subarrays
		    if (x < ptsLeft.length && y < ptsRight.length) {
		        // Compare the current elements from the left and right subarrays
		        // Place the smaller or equal element into the merged array
		        pts[z++] = ptsLeft[x].compareTo(ptsRight[y]) <= 0 ? ptsLeft[x++] : ptsRight[y++];
		    } 
		    // If only elements are available in the left subarray
		    else if (x < ptsLeft.length) {
		        // Copy the remaining elements from the left subarray to the merged array
		        pts[z++] = ptsLeft[x++];
		    }
		    // If only elements are available in the right subarray
		    else {
		        // Copy the remaining elements from the right subarray to the merged array
		        pts[z++] = ptsRight[y++];
		    }
		}
	}
	// Other private methods if needed ...
}
