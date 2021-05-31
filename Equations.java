/** 
 * Class to perform quadratic equations. The user must input the values for the terms A, B and C, and if the values are valid, the program will return the result.
 * If the values are invalid (e.g. A == 0 or discriminant < 0) the program notifies the user of the problem, and allows for more tries.
 * The program keeps going until the user wants to leave
 * 
 * @author Rafael Souza
 * 
 */
public class Equations {
    
    public static void main(String[] args) {
        
        int A;
        int B;
        int C;

        boolean exit;

        System.out.println("Equation solver program.\n");

        while (true) {
            
            System.out.println("\nType the value for A: ");
            A = TextIO.getlnInt();

            System.out.println("\nType the value for B: ");
            B = TextIO.getlnInt();

            System.out.println("\nType the value for C: ");
            C = TextIO.getlnInt();

            try {
                System.out.println("\nResult of the equation: " + root(A, B, C));
            } catch (IllegalArgumentException e) {
                System.err.println("\nError: " + e.getMessage());
            }

            System.out.println("\n\nAnother equation?");
            exit = TextIO.getlnBoolean();

            if (!exit) {
                break;
            }
        }
    }

    /**
     * Returns the larger of the two roots of the quadratic equation A*x*x + B*x + C
     * = 0, provided it has any roots. If A == 0 or if the discriminant, B*B -
     * 4*A*C, is negative, then an exception of type IllegalArgumentException is
     * thrown.
     */
    public static  double root(double A, double B, double C) throws IllegalArgumentException {
        if (A == 0) {
            throw new IllegalArgumentException("A can't be zero.");
        } else {
            double disc = B * B - 4 * A * C;
            if (disc < 0)
                throw new IllegalArgumentException("Discriminant < zero.");
            return (-B + Math.sqrt(disc)) / (2 * A);
        }
    }
}
