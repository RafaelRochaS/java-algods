import java.util.Arrays;

/**
 * This class compares the performance of a custom-defined Selection Sort
 * algorithm with the Arrays.sort algorithm defined in the Java Arrays library.
 * The comparison is made with identical arrays, of sizes 1,000, 10,000 and
 * 100,000.
 * 
 * The output obtained is below:
 * 
 * -------- Sorting with array size 1000 -------- 
 * Defined selection sort with array size of 1000: 5 ms 
 * Java sort with array size of 1000: 1 ms
 * 
 * -------- Sorting with array size 10000 -------- 
 * Defined selection sort with array size of 10000: 60 ms 
 * Java sort with array size of 10000: 7 ms
 * 
 * -------- Sorting with array size 100000 -------- 
 * Defined selection sort with array size of 100000: 2592 ms 
 * Java sort with array size of 100000: 11 ms
 * 
 * @author Rafael Souza
 * 
 */
public class Benchmark {

    /**
     * Main function, that will execute the benchmark comparison.
     * 
     * @param args Command line arguments, not used in the method.
     */
    public static void main(String[] args) {

        //------ Sorting size 1000
        System.out.println("-------- Sorting with array size 1000 --------");

        int arraySize = 1000;

        int[] arr1 = new int[arraySize];
        int[] arr2 = new int[arraySize];

        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = (int)(Integer.MAX_VALUE * Math.random());
        }

        arr2 = arr1.clone();


        // Sort first array with defined selectionSort
        long startTime = System.currentTimeMillis();
        selectionSort(arr1);
        long runTime = System.currentTimeMillis() - startTime;

        System.out.printf("Defined selection sort with array size of 1000: %d ms%n", runTime);


        // Sort second array with Java sort
        startTime = System.currentTimeMillis();
        Arrays.sort(arr2);
        runTime = System.currentTimeMillis() - startTime;

        System.out.printf("Java sort with array size of 1000: %d ms%n", runTime);


        //------ Sorting size 10000
        System.out.println("\n-------- Sorting with array size 10000 --------");

        arraySize = 10000;

        arr1 = new int[arraySize];
        arr2 = new int[arraySize];

        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = (int)(Integer.MAX_VALUE * Math.random());
        }

        arr2 = arr1.clone();

        // Sort first array with defined selectionSort
        startTime = System.currentTimeMillis();
        selectionSort(arr1);
        runTime = System.currentTimeMillis() - startTime;

        System.out.printf("Defined selection sort with array size of 10000: %d ms%n", runTime);

        // Sort second array with Java sort
        startTime = System.currentTimeMillis();
        Arrays.sort(arr2);
        runTime = System.currentTimeMillis() - startTime;

        System.out.printf("Java sort with array size of 10000: %d ms%n", runTime);


        //------ Sorting size 100000
        System.out.println("\n-------- Sorting with array size 100000 --------");
        arraySize = 100000;

        arr1 = new int[arraySize];
        arr2 = new int[arraySize];

        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = (int)(Integer.MAX_VALUE * Math.random());
        }

        arr2 = arr1.clone();

        // Sort first array with defined selectionSort
        startTime = System.currentTimeMillis();
        selectionSort(arr1);
        runTime = System.currentTimeMillis() - startTime;

        System.out.printf("Defined selection sort with array size of 100000: %d ms%n", runTime);


        // Sort second array with Java sort
        startTime = System.currentTimeMillis();
        Arrays.sort(arr2);
        runTime = System.currentTimeMillis() - startTime;

        System.out.printf("Java sort with array size of 100000: %d ms%n", runTime);

    }

    private static void selectionSort(int[] array) {

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
}