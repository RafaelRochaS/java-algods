import java.util.Arrays;

public class AlgorithmsTester {
    
    public static void main(String[] args) {
        
        final int ARR_SIZE_1 = 1000;
        final int ARR_SIZE_2 = 10000;
        final int ARR_SIZE_3 = 100000;

        int[] array1 = new int[ARR_SIZE_1];
        int[] array2 = new int[ARR_SIZE_2];
        int[] array3 = new int[ARR_SIZE_3];

        Arrays.setAll(array1, index -> 1 + index);
        Arrays.setAll(array2, index -> 1 + index);
        Arrays.setAll(array3, index -> 1 + index);

        compareSearch(array1, 876);
        compareSearch(array2, 1576);
        compareSearch(array3, 27631);
        compareSearch(array1, 987555);
        compareSearch(array2, 987555);
        compareSearch(array3, 987555);



    }

    static void compareSearch(int[] arr, int value) {

        System.out.println("\n------- Testing Search algorithms with array size: " + arr.length);
        System.out.println("\nThe value we want to find is: " + value);
        System.out.println("Testing with regular full array search...");

        long startTime = System.currentTimeMillis();
        int result = Search.fullArraySearch(arr, value);
        long runTime = System.currentTimeMillis() - startTime;

        if (result == -1) {
            System.out.println("Failed to find the value");
        } else {
            System.out.println("Value found at position: " + result);
        }

        System.out.printf("Runtime with array length %d: %d ms%n", arr.length, runTime);

        System.out.println("Testing with binary search...");

        startTime = System.currentTimeMillis();
        result = Search.binarySearch(arr, 0, arr.length - 1, value);
        runTime = System.currentTimeMillis() - startTime;

        if (result == -1) {
            System.out.println("Failed to find the value");
        } else {
            System.out.println("Value found at position: " + result);
        }

        System.out.printf("Runtime with array length %d: %d ms%n", arr.length, runTime);

    }
}
