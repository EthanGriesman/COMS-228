package hw1;

/**
 * This class implements insertion sort.
 * 
 * @author Ethan Griesman 6/8/2023
 * 
 */
public class InsertionSorter extends AbstractSorter {
	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts  
	 */
	public InsertionSorter(Point[] pts) {
		// call to superclass constructor
		super(pts);
		super.algorithm = "insertion sort";
		// additional code to check and initialize points array
	    if (pts == null) {
	        throw new IllegalArgumentException("Points array cannot be null.");
	    }
	    this.points = pts;
	}	
	
	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 */
	@Override 
	public void sort() {
		for (int i = 1, j; i < points.length; i++) {
	        Point k = points[i];
	        
	        // Shift elements to the right
	        for (j = i - 1; j >= 0 && points[j].compareTo(k) > 0; j--)
	            points[j + 1] = points[j];
	        
	        // Insert the key at the correct position
	        points[j + 1] = k;
	    }
	}		
}
