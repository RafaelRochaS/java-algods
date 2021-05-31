import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This program lists the files in a directory specified by
 * the user.  The user is asked to type in a directory name.
 * If the name entered by the user is not a directory, a
 * message is printed and the program ends.
 */
public class DirectoryList {


   public static void main(String[] args) {

      String directoryName;  // Directory name entered by the user.
      File directory;        // File object referring to the directory.
      Scanner scanner;       // For reading a line of input from the user.

      scanner = new Scanner(System.in);  // scanner reads from standard input.

      System.out.print("Enter a directory name: ");
      directoryName = scanner.nextLine().trim();
      directory = new File(directoryName);

      listFiles(directory);

      scanner.close();

   } // end main()

   static void listFiles(File directory) {

      String[] files;        // Array of file names in the directory.
      ArrayList<File> directories = new ArrayList<>();

      if (!directory.isDirectory()) {
         if (!directory.exists())
            System.out.println("There is no such directory!");
         else
            System.out.println("That file is not a directory.");
      }
      else {
         files = directory.list();
         System.out.println("Files in directory \"" + directory + "\":");
         for (int i = 0; i < files.length; i++) {
            File file = new File(directory, files[i]);
            if (file.isDirectory()) {
               directories.add(file);
            }
            System.out.println("   " + files[i]);
         }
         for (File f : directories) {
            listFiles(f);
         }
      }
   }

} // end class DirectoryList
