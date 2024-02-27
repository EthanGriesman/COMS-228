package hw1;

/**
 * Represents a point with x and y coordinates.
 * Provides comparison functionality based on the static variable xORy.
 * Implements the Comparable interface for comparing points.
 * 
 * @author Ethan Griesman 6/8/2023
 */
public class Point implements Comparable<Point> {

	private int x;
	private int y;
	// Compare x coordinates if xORy == true and y coordinates otherwise
	public static boolean xORy; 

	// Default constructor
	public Point() {
		// x and y get default value 0
	}

	/**
	 * Constructs a point with the given x and y coordinates.
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param p Point to be copied
	 */
	public Point(Point p) {
		x = p.getX();
		y = p.getY();
	}

	/**
	 * Get the x-coordinate of the point.
	 * 
	 * @return x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Get the y-coordinate of the point.
	 * 
	 * @return y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Set the value of the static instance variable xORy.
	 * 
	 * @param xORy true to compare x coordinates, false to compare y coordinates
	 */
	public static void setXorY(boolean xORy) {
		Point.xORy = xORy;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
		    // If the compared object is the same as the current object, return true
		    return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
		    // If the compared object is null or belongs to a different class, return false
		    return false;
		}

		Point o = (Point) obj;
		// Cast the compared object to Point type

		return x == o.x && y == o.y;
		// Return true if both the x and y coordinates of the current object
		// are equal to the x and y coordinates of the compared object
	}

	/**
	 * Compare this point with a second point q depending on the value of the static
	 * variable xORy.
	 * 
	 * @param q Second point to compare
	 * @return -1 if (xORy == true && (this.x < q.x || (this.x == q.x && this.y <
	 *         q.y))) || (xORy == false && (this.y < q.y || (this.y == q.y && this.x
	 *         < q.x))) 0 if this.x == q.x && this.y == q.y) 1 otherwise
	 */
	@Override
	public int compareTo(Point q) {
		if ((xORy && (x < q.x || (x == q.x && y < q.y))) ||
	            (!xORy && (y < q.y || (y == q.y && x < q.x)))) {
			// Return -1 if this point is smaller
	        return -1; 
	    } else if (x == q.x && y == q.y) {
	    	// Return 0 if the points are equal
	        return 0; 
	    } else {
	    	// Return 1 if this point is greater
	        return 1; 
	    }
	}

	/**
	 * Output a point in the standard form (x, y).
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
