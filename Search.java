/**
 * Class to experiment with different searching algorithms, focusing on integer array search.
 * 
 * @author Rafael Rocha
 */
public class Search {
    
    /**
     * Binary search algorithm to find specific values in an integer array. 
     * Returns the index of the value specific, or -1 if the value is not present in the array.
     * If the array is empty or null, throws an IllegalArgumentException.
     * O(log(n)) efficiency.
     * 
     * @param array The array to be searched on.
     * @param loIndex The index to slice the array search. Should start at 0.
     * @param hiIndex The index to end the array search. Should start at array.length - 1.
     * @param value The value to search for.
     * @throws IllegalArgumentException
     * @return The index of the value, or -1 if not present.
     */
    public static int binarySearch(int[] array, int loIndex, int hiIndex, int value) {

        Helper.validateArray(array);

        if (loIndex > hiIndex) { // List is empty
            return -1; 
        } else {
            int middle = (loIndex + hiIndex) / 2;
            if (value == array[middle]) {
                return middle;
            } else if (value < array[middle]) {
                return binarySearch(array, loIndex, middle - 1, value);
            } else {
                return binarySearch(array, middle + 1, hiIndex, value);
            }
        }
    }

    /**
     * Basic full array search method. Will go through the entire array one by one to try to find the value.
     * Returns the index of the value or -1 if not found.
     * If the array is empty or null, throws an IllegalArgumentException.
     * O(n) efficiency, but simpler interface.
     * 
     * @param array The array to be searched on.
     * @param value The value to search for.
     * @throws IllegalArgumentException
     * @return The index of the value, or -1 if not found.
     */
    public static int fullArraySearch(int[] array, int value) {

        Helper.validateArray(array);

        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }

        return -1;
    }
}
