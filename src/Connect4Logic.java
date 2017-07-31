/**
 * Sean Schlaefli
 * Connect4Logic.java
 * implements the Connect4 game logic.
 * Board is implemented rows x columns.
 * compiles
 * working/tested
 */

import java.lang.Integer;
import java.util.Random;

public class Connect4Logic {

    private final int ROWS = 6;
    private final int COLUMNS = 7;
    private final int WIN = 4;
    
    private final char EMPTY = 'e';
    private final char RED = 'r';
    private final char YELLOW = 'y';

    private char currentMove;
    private char[][] board;

    
    public Connect4Logic() {
	board = new char[ROWS][COLUMNS];
	reset();
    }


    /**
     * Clear the board by setting all positions to 
     * empty.
     */
    private void clear() {
	for(int i = 0; i < ROWS; i++)
	    for(int j = 0; j < COLUMNS; j++)
		board[i][j] = EMPTY;
    }


    public void reset() {
	currentMove = setFirstMove();
	clear();
    }

    
    /**
     * Return the number of rows.
     * @return int
     */
    public int getRows() {
	return ROWS;
    }


    /**
     * Return the number of columns.
     * @return int
     */
    public int getColumns() {
	return COLUMNS;
    }


    /**
     * Return the current move.
     * @return char
     */
    public char getCurrentMove() {
	return currentMove;
    }

    
    /**
     * Use the java psuedo random number generator to determine
     * which player goes first.
     * If the result is 0, red goes first.
     * If the result is 1, yellow goes first.
     * @return char
     */
    private char setFirstMove() {
	Random rand = new Random();
	int result = Math.abs(rand.nextInt()) % 2;
	return (result == 0) ? RED : YELLOW;
    }


    /**
     * Attempt to make a move at the position given by
     * @param column. If this is an invalid move, do nothing.     
     */
    public void move(int column) {
	if(column < COLUMNS) {
	    if(verifyMove(column)) {
		makeMove(column);
		System.out.println(toString());
		System.out.println("Checking if " + currentMove + " won the game...");		
		if(gameOver()) {		
		    System.out.println(currentMove + " wins.");
		} else { 
		    switchTurns();
		}
				
	    } else {
		System.out.println("invalid move");
	    }
	}
    }


    /**
     * Verify that there's a valid move available in the
     * given column.
     * @param int column the column to check
     */
    public boolean verifyMove(int column) {
	return board[0][column] == EMPTY;
    }

    
    /**
     * Make a move given a column. Return the position
     * in the rows 
     * @param int column
     * @return int 
     */
    public int makeMove(int column) {
	int result = findPosition(column);
	board[result][column] = currentMove;
	return result;
    }

    
    /**
     * Iterate over the column on the board and
     * find the appropriate position to make a
     * move.
     */
    private int findPosition(int column) {
	int result = -1;
	for (int i = 0; i < ROWS; i++) {
	    if (board[i][column] == EMPTY) {
	        result++;
	    } else {
		return result;
	    }
	}
	return result;
    }


    /**
     * Determine if the game is a draw by 
     * checking the top row.
     * @return boolean
     */
    public boolean isDraw() {
	for(int i = 0; i < COLUMNS; i++) {
	    if(board[0][i] == EMPTY)
		return false;
	}	
	return true;
    }

    
    /**
     * Determine if the game is over. 
     * @return boolean
     */
    public boolean gameOver() {
	return checkRows() || checkColumns() || checkDiagonals();
    }


    /**
     * Check the rows for a winner.
     * @return boolean
     */
    private boolean checkRows() {
	for(int i = 0; i < ROWS; i++) {
	    int count = 0;
	    for(int j = 0; j < COLUMNS; j++) {
		count = checkCount(count, i, j);
		if (count == WIN) return true;
	    }
	}
	return false;
    }
 

    /**
     * Check the columns for a winner.
     * @return boolean
     */
    private boolean checkColumns() {
	for(int i = 0; i < COLUMNS; i++) {
	    int count = 0;
	    for(int j = 0; j < ROWS; j++) {
		count = checkCount(count, j, i);
		if (count == WIN) return true;
	    }
	} 
	return false;
    }



    /**
     * Check the diagonals for a winner. 
     * @return boolean
     */
    private boolean checkDiagonals() {
	
	for (int i = 0; i <= ROWS-WIN; i++) {
	    if (checkRightDiagonal(i, 0)
		||
		checkLeftDiagonal(i, COLUMNS-1)) {

		return true;
	    }
	}


	for (int i = 1; i <= COLUMNS-WIN; i++) {
	    if (checkRightDiagonal(0, i)
		||
		checkLeftDiagonal(0, COLUMNS-i-1)) {

		return true;
	    }
	}	    
	return false;
    }


    /**
     * Check a diagonal that goes from left to right.
     * @return boolean
     */
    private boolean checkRightDiagonal(int row, int col) {				       
	int count = 0;
	while (row < ROWS && col < COLUMNS) {
	    count = checkCount(count, row, col);
	    if (count == WIN) return true;
	    row++;
	    col++;
	}
	return false;
    }


    /**
     * Check a diagonal that goes from right to left
     * @return boolean
     */
    private boolean checkLeftDiagonal(int row, int col) {	
	int count = 0;
	while (row < ROWS && col >= 0) {
	    count = checkCount(count, row, col);
	    if (count == WIN) return true;
	    row++;
	    col--;
	}
	return false;
    }

    
    /** 
     * Check the counter used by the win detection algorithm
     * return the new value of the counter after checking the
     * position given by
     * @param int row, int col
     * @return int
     */
    private int checkCount(int count, int row, int col) {
	if (board[row][col] == currentMove) {
	    return ++count;
	} else {
	    return 0;
	}
    }
    
    /**
     * After a move is made, switch currentMove to be 
     * the player that did not make the move.
     */
    public void switchTurns() {
	currentMove = (currentMove == RED) ? YELLOW : RED;
    }

    
    // print board for testing
    public void print() {
	for(int i = 0; i < ROWS; i++) {
	    for(int j = 0; j < COLUMNS; j++) {
		System.out.print(board[i][j] + " ");
		if(j == COLUMNS-1) System.out.print('\n');		    
	    }
	}
				 
    }


    // to string representation of the game board
    // could use string builder but don't see an issue
    // with allocating new references at each iteration
    // on such a small scale
    public String toString() {
	String result = "";
	for(int i = 0; i < ROWS; i++) {
	    for(int j = 0; j < COLUMNS; j++) {
		result += board[i][j] + " ";
		if(j == COLUMNS-1) result += "\n";
	    }
	}
	return result;
    }

    
    // testing
    public static void main(String[] args) {
	Connect4Logic g = new Connect4Logic();
	g.move(2);
	g.move(1);
	g.move(1);
	g.move(2);
	g.move(0);
	g.move(0);
	g.move(0);
    }
       
}
