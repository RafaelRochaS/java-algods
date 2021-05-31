import java.util.Random;

/**
 * Class with helper methods for the Algorithms Tester experiments.
 * 
 * @author Rafael Rocha
 */
public class Helper {
    
    /**
     * Validates if an array is empty or null. 
     * 
     * @param array Array to check
     * @throws IllegalArgumentException
     * @return True if array is valid, otherwise throws exception
     */
    public static boolean validateArray(int[] array) {
        
        if (array.length == 0 || (array == null)) {
            throw new IllegalArgumentException("Array is empty or null.");
        } else {
            return true;
        }
    }

    /**
     * Creates a random array based on size.
     * 
     * @param size The size of the created array.
     * @return The random array created.
     */
    public static int[] createRandomArray(int size) {

        int[] arr = new int[size];

        Random rand = new Random();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int)(Integer.MAX_VALUE * rand.nextInt());
        }

        return arr;
    }

}
