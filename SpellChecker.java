import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;
import javax.swing.JFileChooser;

/**
 * Class to spellcheck the text in a file, comparing with the dictionary in the hardcoded "words.txt" file.
 * The class asks for the user to choose a file to check, or it can be altered to use a hardcoded one, for faster iteration.
 * If the words.txt file is not provided in the same directory as the class file, the program will fail.
 */
public class SpellChecker {

    public static void main(String[] args) {
        
        HashSet<String> hSet = new HashSet<>();
        HashSet<String> hSetInput = new HashSet<>();

        try (Scanner filein = new Scanner(new File("./words.txt"))) {
            while (filein.hasNextLine()) {
                hSet.add(filein.nextLine().toLowerCase());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        //try (Scanner userFile = new Scanner(new File("./test.txt"))) {    / Use this line to use directly the test.txt file
        try (Scanner userFile = new Scanner(getInputFileNameFromUser())) {
            userFile.useDelimiter("[^a-zA-Z]+");
            while(userFile.hasNextLine()){ 
                hSetInput.add(userFile.nextLine().toLowerCase());
            }
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.toString());
            return;
        }

        System.out.println("\nPossibly misspelled words:\n");

        TreeSet<String> corr = new TreeSet<>();
        for (String str : hSetInput) {
            if (!hSet.contains(str)) {
                System.out.print(str + ": ");
                corr = corrections(str, hSet);
                if (corr.isEmpty()) {
                    System.out.print("(no suggestions)");
                } else {
                    System.out.print(corr.toString().replace("[", "").replace("]", ""));
                }
                System.out.println("");
            }
        }

        System.out.println("");
    }

    /**
     * Lets the user select an input file using a standard file selection dialog
     * box. If the user cancels the dialog without selecting a file, the return
     * value is null.
     */
    static File getInputFileNameFromUser() {

        JFileChooser fileDialog = new JFileChooser();

        fileDialog.setDialogTitle("Select File for Input");

        int option = fileDialog.showOpenDialog(null);

        if (option != JFileChooser.APPROVE_OPTION)
            return null;
        else
            return fileDialog.getSelectedFile();
    }


    /**
     * Identifies possible corrections on the string provided, utilizing the following rules:
     *  - Delete one character
     *  - Change any letter to any other letter
     *  - Insert any letter
     *  - Swap two letters
     *  - Insert a space
     * @param badWord Possibly misspelled word. It is assumed not to be in the dictionary.
     * @param dictionary HashSet to use for comparison. Any word in the set is considered a valid word.
     * @return A TreeSet containing all the possible corrections. The set will be empty if no correction is found.
     */
    static TreeSet<String> corrections(String badWord, HashSet<String> dictionary) {

        TreeSet<String> corr = new TreeSet<>();
        StringBuilder bad = new StringBuilder(badWord);
        String testStr;
        String testStr2;

        // Delete one character from the input string and see if it is the dictionary
        for (int i = 0; i < badWord.length(); i++) {
            testStr = bad.deleteCharAt(i).toString();
            if (dictionary.contains(testStr)) {
                corr.add(testStr);
            }
            bad = new StringBuilder(badWord);
        }   

        // Change any letter to any other letter
        for (int i = 0; i < badWord.length(); i++) {
            for (char ch = 'a'; ch <= 'z'; ch++){   
                testStr = badWord.replace(badWord.charAt(i), ch);
                if (dictionary.contains(testStr)) {
                    corr.add(testStr);
                }
            }
        } 

        // Insert any letter at any point
        for (int i = 0; i <= badWord.length(); i++) {
            for (char ch = 'a'; ch <= 'z'; ch++){   
                testStr = badWord.substring(0, i) + ch + badWord.substring(i, badWord.length());
                if (dictionary.contains(testStr)) {
                    corr.add(testStr);
                }
            }
        }

        // Swap any two neighboring characters
        for (int i = 1; i < badWord.length(); i++) {
            testStr = badWord.substring(0, i-1) + badWord.charAt(i) + badWord.charAt(i-1) + badWord.substring(i+1, badWord.length());
            if (dictionary.contains(testStr)) {
                corr.add(testStr);
            }
        }

        // Insert a space at any point in the misspelled word (and check that both of the words that are produced are in the dictionary)
        for (int i = 1; i < badWord.length(); i++) {
            testStr = badWord.substring(0, i);
            testStr2 = badWord.substring(i, badWord.length());
            if (dictionary.contains(testStr) && dictionary.contains(testStr2)){
                corr.add(testStr + " " + testStr2);
            }
        }

        return corr;
    }
}