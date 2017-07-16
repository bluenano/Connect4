/**
 * Sean Schlaefli
 * ConnectFourLogic.java
 * implements the ConnectFour game logic.
 * Board is implemented rows x columns.
 * compiles
 * working/tested
 */

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
	currentMove = setFirstMove();
	board = new char[ROWS][COLUMNS];
	clear();
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
     * Use the java random number generator to determine
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
		if(gameOver())		
		    System.out.println(currentMove + " wins.");
		else 
		    switchTurns();
				
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
     * Make a move given a column.    
     * @param int column
     * @return boolean
     */
    public void makeMove(int column) {
	if(verifyMove(column)) {
	    int result = findPosition(column);
	    board[result][column] = currentMove;
	}
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
     * Determine if the game is over. This method
     * will only check if the currentMove is a winner.
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
	    int result = 0;
	    for(int j = 0; j < COLUMNS; j++) {
		if(board[i][j] == currentMove) {
		    result++;
		    if(result == WIN) return true;
		} else {
		    result = 0;
		}
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
	    int result = 0;
	    for(int j = 0; j < ROWS; j++) {
		if(board[j][i] == currentMove) {		    
		    result++;
		    if(result == WIN) return true;   
		} else {
		    result = 0;
		}		
	    }
	} 
	return false;
    }



    /**
     * Check the diagonals for a winner. The
     * algorithm will be different depending on
     * the dimensions of the board.
     * @return boolean
     */
    private boolean checkDiagonals() {
	return false;
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
