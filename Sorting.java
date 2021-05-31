/**
 * Class to experiment with sorting algorithms, focusing on integer array sorting.
 * 
 * @author Rafael Rocha
 */
public class Sorting {
    
    /**
     * Method that implements the Selection Sort algorithm. 
     * Throws IllegalArgumentException if array is null or empty.
     * 
     * @param array The array to be sorted
     * @throws IllegalArgumentException
     */
    public static void selectionSort(int[] array) {

        for (int lastPlace = array.length - 1; lastPlace > 0; lastPlace--) {

            int maxLoc = 0;
            for (int j = 1; j <= lastPlace; j++) {
                if (array[j] > array[maxLoc]) {

                    maxLoc = j;
                }
            }

            int temp = array[maxLoc];
            array[maxLoc] = array[lastPlace];
            array[lastPlace] = temp;
        }
    }

    /**
     * Method that implements the Quicksort algorithm.
     * Throws IllegalArgumentException if the array is null or empty.
     * 
     * @param array The array to be sorted.
     * @param lo The start of the quicksort step. Should be started at 0.
     * @param hi The end of the quicksort step. Should be started at array.length - 1.
     * @throws IllegalArgumentException
     */
    public static void quicksort(int[] array, int lo, int hi) {

        Helper.validateArray(array);

        if (hi <= lo) {
            return;
        } else {
            int pivotPosition = quicksortStep(array, lo, hi);
            quicksort(array, lo, pivotPosition - 1);
            quicksort(array, pivotPosition + 1, hi);
        }
    }

    private static int quicksortStep(int[] array, int lo, int hi) {

        int pivot = array[lo];

        while (hi > lo) {
            // Loop invariant: array[i] <= pivot for i < lo and array[i] >= pivor for i > hi
            while (hi > lo && array[hi] >= pivot) {
                hi--;
            }

            if (hi == lo) {
                break;
            }

            array[lo] = array[hi];
            lo++;

            while (hi > lo && array[lo] <= pivot){
                lo++;
            }

            if (hi == lo) {
                break;
            }

            array[hi] = array[lo];

            hi--;
        }

        array[lo] = pivot;

        return lo;
    }
}
