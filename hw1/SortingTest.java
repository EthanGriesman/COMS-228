package hw1;
import hw1.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static hw1.Algorithm.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SortingTest {
    // #############################################################################
    // Your AbstractSorter.sort() method needs to be public, so you may want to add
    // a TODO: comment like so to remind yourself to revert it back to protected
    // later
    // #############################################################################

    /**
     * Number of randomly generated arrays, overall increases the execution time
     * of every algorithm linearly.
     */
    private static final int NUM_ARRAYS = 1000;

    /**
     * Number of elements in each array, increases the execution time of each
     * algorithm by their time complexity.
     */
    private static final int ARR_SIZE = 800;

    /**
     * The Random object, you may change the seed by adding a number as an argument
     * to the new Random() constructor call.
     */
    private final Random rng = new Random();

    /**
     * A Stream<Point[]> object, which is analogous to a collection (not really) of
     * Point[] arrays. Allows me to write the tests as one-liners.
     */
    private final Stream<Point[]> arrays = IntStream.range(0, NUM_ARRAYS)
            .mapToObj(a -> IntStream.range(0, ARR_SIZE)
                    .mapToObj(n -> new Point(rng.nextInt(101) - 50, rng.nextInt(101) - 50))
                    .toArray(Point[]::new));

    /**
     * Holder array for the purposes of using AbstractSorter.getPoints()
     */
    private Point[] holder = new Point[ARR_SIZE];

    /**
     * This is necessary for reasons that I can't articulate in a javadoc.
     */
    @Test
    public void $DoThisFirst() {
        Point[] ignore = arrays.findAny().orElseThrow(RuntimeException::new);
        Arrays.stream(Algorithm.values()).forEach(algo -> testSorter(algo, ignore));
        System.gc();
    }

    @Test
    public void mergeSortTest() {
        arrays.forEach(a -> testSorter(MergeSort, a));
    }

    @Test
    public void quickSortTest() {
        arrays.forEach(a -> testSorter(QuickSort, a));
    }

    @Test
    @Order(1)
    public void selectionSortTest() {
        arrays.forEach(a -> testSorter(SelectionSort, a));
    }

    @Test
    @Order(0)
    public void insertionSortTest() {
        arrays.forEach(a -> testSorter(InsertionSort, a));
    }

    /**
     * This method dispatches the corresponding sorting algorithm based on the
     * parameter algo, and instantiates a new sorter based on that. It then performs
     * the sort on one array, then validates the whether it was sorted correctly by
     * calling either compareAgainstSorted or compareOrdering. Will automatically
     * raise a JUnit test failure if the check fails.
     *
     * This method instantiates a new sorter for every array we want to sort, which
     * will add some performance overhead. But since the points instance variable in
     * AbstractSorter is effectively final (i.e. there is no canonical way to mutate
     * the array), this is necessary if we want to sort multiple arrays.
     * Furthermore,
     * the overhead should be identical for each sorting algorithm, so the relative
     * performance comparison between algorithms shouldn't be affected by this.
     *
     * @param algo The algorithm to use
     * @param arr  The array to sort
     */
    private void testSorter(Algorithm algo, Point[] arr) {
        AbstractSorter sorter = switch (algo) {
            case SelectionSort -> new SelectionSorter(arr);
            case InsertionSort -> new InsertionSorter(arr);
            case MergeSort -> new MergeSorter(arr);
            case QuickSort -> new QuickSorter(arr);
        };

        sorter.setComparator(0);
        sorter.sort();
        sorter.getPoints(holder);

        compareAgainstSorted(holder, sorter.pointComparator);
        // compareOrdering(holder, sorter.pointComparator);
    }

    /**
     * Validates the resultant array after AbstractSorter.sort() by comparing it
     * with Java's Arrays.sort(). Slower, Is guaranteed to catch any erroneously
     * sorted array up to .equals().
     *
     * @param arr  The array that we want to validate whether it's been sorted
     *             correctly
     * @param comp The comparator it was sorted with
     */
    private static void compareAgainstSorted(Point[] arr, Comparator<Point> comp) {
        Point[] cpy = Arrays.copyOf(arr, arr.length);
        Arrays.sort(cpy, comp);
        assertArrayEquals(arr, cpy);
    }

    /**
     * Validates the resultant array after AbstractSorter.sort() by checking the
     * natural ordering of each adjacent element in the array. Faster, but may not
     * catch every erroneously sorted array, for example, [1, 1, 1, 1, 1] would pass
     * this check, regardless of what the original array was.
     *
     * @param arr  The array that we want to validate whether it's been sorted
     *             correctly
     * @param comp The comparator it was sorted with
     */
    private static void compareOrdering(Point[] arr, Comparator<Point> comp) {
        for (int i = 1; i < arr.length; i++)
            assertTrue(comp.compare(arr[i], arr[i - 1]) >= 0);
    }
}