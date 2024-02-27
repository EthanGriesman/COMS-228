package hw1;

public class TestingGround {
	public static void main(String[] args) {
	
		Point[] points1 = {new Point(4, 2), new Point(1, 5), new Point(3, 3), new Point(2, 4)};
		Point[] points2 = {new Point(4, 2), new Point(1, 5), new Point(3, 3), new Point(2, 4)};
		Point[] points4 = {new Point(4, 2)};
		Point[] points5 = {new Point(2, 3), new Point(1, 2), new Point(2, 1), new Point(1, 2)};

		
		Point[] points6 = {new Point(2, 3), new Point(1, 2), new Point(2, 1), new Point(1, 2)};
		System.out.println("SELECTION SORTER");
		for (Point point : points6) {
            System.out.println(point);
        }
        System.out.println("------...Sorting");
        SelectionSorter sorter = new SelectionSorter(points6);
        sorter.setComparator(0);
        sorter.sort();
        //System.out.println(points6);
        for (Point point : points6) {
            System.out.println(point);
        }
        
        Point[] points7 = {new Point(2, 3), new Point(1, 2), new Point(2, 1), new Point(1, 2)};
        System.out.println("INSERTION SORTER");
        for (Point point : points7) {
            System.out.println(point);
        }
        System.out.println("------");
        InsertionSorter sorter2 = new InsertionSorter(points7);
        sorter2.setComparator(0);
        sorter2.sort();
        //System.out.println(points6);
        for (Point point : points7) {
            System.out.println(point);
        }
       
        Point[] points8 = {new Point(2, 3), new Point(1, 2), new Point(2, 1), new Point(1, 2)};
        System.out.println("MERGE SORTER");
        for (Point point : points8) {
            System.out.println(point);
        }
        System.out.println("------");
        MergeSorter sorter3 = new MergeSorter(points8);
        sorter3.setComparator(0);
        sorter3.sort();
        sorter3.getPoints(points8);  // These two lines are different because of the way my mergeSort works
        //sorter3.printArray();        // Code for printArray() below 
        for (Point point : points8) {
            System.out.println(point);
        }
	}
	/*
	//Put into AbstractSorter or whichever sorter you want
	public void printArray() {
		for(int i=0; i<points.length; ++i) {
			System.out.println(points[i]);
		}
	}
	*/
	
	
}
