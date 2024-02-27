package hw1;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 * 
 * This class implements the version of the quicksort algorithm presented in the lecture.   
 *
 * @author Ethan Griesman 6/8/2023
 * 
 */
public class QuickSorter extends AbstractSorter
{
	// Other private instance variables if you need ... 
		
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *   
	 * @param pts   input array of integers
	 */
	public QuickSorter(Point[] pts) {
		// Call to superclass constructor and set algorithm
		super(pts);
		super.algorithm = "quicksort";

		// Check and initialize points array
		if (pts == null) {
		    throw new IllegalArgumentException("Points array cannot be null.");
		}
		points = pts;
	}
		
	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.  
	 * 
	 */
	@Override 
	public void sort() {
		quickSortRec(0, points.length - 1);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last) {
		// Base case: Check if the range is already sorted
	    if (first >= last) {
	        return;
	    }
	    
	    int p = partition(first, last);
	    
	    // Recursive calls
	    quickSortRec(first, p - 1);
	    quickSortRec(p + 1, last);
	}
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * @param arr 
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last) {
		// Choosing the first pivot.
		Point pivot = points[last]; // Choosing the first pivot.
		int i = first - 1; // Smaller element which indicates the right position of pivot found at the moment

		for (int j = first; j <= last - 1; j++) {
		    if (points[j].compareTo(pivot) <= 0) {
		    	// Increases small index.
		        i++; 
		        swap(i, j); // swap method
		    }
		}

		swap(i + 1, last); 
		return i + 1;
	}	
	// Other private methods if needed ...
}
