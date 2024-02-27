package hw1;
import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 * This class implements selection sort.  
 * 
 * @author Ethan Griesman 6/8/2023
 *
 */

public class SelectionSorter extends AbstractSorter
{
	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts  
	 */
	public SelectionSorter(Point[] pts) {
		// call to superclass constructor
	    super(pts);
	    super.algorithm = "selection sort";
	    // additional code to check and initialize points array
	    if (pts == null) {
	        throw new IllegalArgumentException("Points array cannot be null.");
	    }
	    this.points = pts;
	}	
	
	/** 
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.  
	 * 
	 */
	@Override 
	public void sort() {
		int length = points.length; // Length of the points array
	    
	    // Moves the boundary of the unsorted array.
	    for (int i = 0; i < length - 1; i++) {
	    	// Index of the minimum element
	        int minIndex = i;
	        
	        // Find the index of the minimum element in the remaining unsorted array
	        for (int j = i + 1; j < length; j++) {
	            if (points[j].compareTo(points[minIndex]) < 0) {
	                minIndex = j;
	            }
	        }
	        
	        // Swap the current element with the minimum element
	        swap(i, minIndex);
	    }
	}
	
	
}