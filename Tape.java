package turing;

/**
 * Class that implements a Tape for a Turing Machine. 
 * Made to be used with the turing package, as the TuringMachine class will utilize it to construct a tape, fill it, and move around it.
 * 
 * For UoPeople CS 1103 Assignment 3
 * 
 * @author Anonymous
 */
public class Tape {

    Cell currentCell;

    /**
     * Basic constructor for the class.
     * Initializes an empty cell with contents of an empty char ' '.
     */
    public Tape () {
        currentCell = new Cell();
        currentCell.content = ' ';
    }

    /**
     * Constructor to initialize the tape with an already defined cell.
     * 
     * @param cell Cell to be used as the current cell of the tape.
     */
    public Tape (Cell cell) {
        currentCell = cell;
    }

    /**
     * Constructor to initilize the tape with a specific content for the current cell.
     * 
     * @param content The content to be used on the initializing current cell.
     */
    public Tape (char content) {
        currentCell = new Cell();
        currentCell.content = content;
    }
    
    /**
     * Get the current cell.
     * 
     * @return the current cell of the tape.
     */
    public Cell getCurrentCell() {
        return currentCell;
    }

    /**
     * Get the content of the current cell.
     * 
     * @return the content of the current cell of the tape.
     */
    public char getContent() {
        return currentCell.content;
    }

    /**
     * Set the content for the current cell.
     * 
     * @param ch the content to be of on the current cell of the tape.
     */
    public void setContent(char ch) {
        currentCell.content = ch;
    }

    /**
     * Moves the tape to the left. If there is no cell on the left of the current one, creates a new one and then moves the entire tape.
     */
    public void moveLeft() {

        if (currentCell.prev == null) {
            Cell newCell = new Cell();
            newCell.content = ' ';
            newCell.next = currentCell;
            currentCell.prev = newCell;
            currentCell = newCell;  
        } else {
            currentCell = currentCell.prev;
        }
    }

    /**
     * Moves the tape to the right. If there is no cell on the right of the current one, creates a new one and then moves the entire tape.
     */
    public void moveRight() {

        if (currentCell.next == null) {
            Cell newCell = new Cell();
            newCell.content = ' ';
            newCell.prev = currentCell;
            currentCell.next = newCell;
            currentCell = newCell;  
        } else {
            currentCell = currentCell.next;
        }
    }

    /**
     * Gets the contents of the entire tape, as a String, in order of left to right.
     * 
     * @return the contents of the tape as a string.
     */
    public String getTapeContents() {

        Cell runner = currentCell;
        StringBuilder builder = new StringBuilder();

        while (runner.prev != null) {
            runner = runner.prev;
        }

        while (runner.next != null) {
            if ((runner.next == null && runner.content == ' ') || (runner.prev == null && runner.content == ' ')) {
                runner = runner.next;
                continue;
            }
            builder.append(runner.content);
            runner = runner.next;
        }

        return builder.toString();
    }
}
