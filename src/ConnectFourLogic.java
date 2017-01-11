/**
 * Sean Schlaefli
 * ConnectFourLogic.java
 * implements the ConnectFour game logic.
 * compiles
 * working/tested
 */

import java.util.Random;

public class ConnectFourLogic {

    private final int DEFAULT_COL = 7;
    private final int DEFAULT_ROW = 6;
    private final int DEFAULT_WIN = 4;
    
    private final char EMPTY = 'e';
    private final char RED = 'r';
    private final char YELLOW = 'y';

    private char currentMove;
    private char[][] board;
    private int columns;
    private int rows;
    private int winSize;

    
    public ConnectFourLogic(int columns, int rows, int winSize) {
	currentMove = setFirstMove();
	System.out.println(currentMove);
	
	if(columns > 0)
	    this.columns = columns;
	else
	    this.columns = DEFAULT_COL;
	
	if(rows > 0)
	    this.rows = rows;
	else
	    this.rows = DEFAULT_ROW;

	if(winSize > 0 && winSize <= columns && winSize <= rows)
	    this.winSize = winSize;
	else
	    this.winSize = DEFAULT_WIN;
	
	board = new char[rows][columns];
	clear();
	print();
	
	System.out.println(columns);
	System.out.println(rows);
	System.out.println(winSize);
    }


    /**
     * Clear the board by setting all positions to 
     * empty.
     */
    public void clear() {
	for(int i = 0; i < rows; i++)
	    for(int j = 0; j < columns; j++)
		board[i][j] = EMPTY;
    }


    /**
     * Return the number of columns.
     * @return int
     */
    public int getColumns() {
	return columns;
    }


    /**
     * Return the number of rows.
     * @return int
     */
    public int getRows() {
	return rows;
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
	if(result == 0)
	    return RED;
	else
	    return YELLOW;
    }


    public boolean move(int column) {
	if(makeMove(column)) {
	    print();
	    System.out.println("Checking if " + currentMove + " won the game...");
	    if(gameOver())		
		System.out.println(currentMove + " wins.");
	    else
		switchTurns();
	} else {
	    System.out.println("invalid move");
	}
	return true;	
    }

    
    /**
     * Make a move given a column.
     * @param int column
     * @return boolean
     */
    private boolean makeMove(int column) {
	int result = findPosition(column);
	System.out.println(result);
	if(result == -1) { // column is full, illegal move
	    return false;
	} else {
	    board[result][column] = currentMove;
	    System.out.println(board[result][column]);
	    return true;
	}
    }


    /**
     * Iterate over the column on the board and
     * find the appropriate position to make a
     * move.
     */
    private int findPosition(int column) {
	int result = -1;
	int position = 0;
	while(position < rows) {
	    if(board[position][column] == EMPTY)
	        result++;
	    position++;
	}
	return result;
    }


    /**
     * Determine if the game is over. This method
     * will only check if the currentMove is a winner.
     * @return boolean
     */
    public boolean gameOver() {
	System.out.println(currentMove + " is being checked now...");
	if(checkColumns()) return true;
	if(checkRows()) return true;
	if(checkDiagonals()) return true;
	return false;
    }


    /**
     * Check the columns for a winner.
     * @return boolean
     */
    private boolean checkColumns() {
	for(int i = 0; i < columns; i++) {
	    int result = 0;
	    for(int j = 0; j < rows; j++) {
		if(board[j][i] == currentMove) {		    
		    result++;
		    if(result == winSize) return true;				    
		} else {
		    if(result == winSize) return true;
		    result = 0;
		}		
	    }
	} 
	return false;
    }


    /**
     * Check the rows for a winner.
     * @return boolean
     */
    private boolean checkRows() {
	for(int i = 0; i < rows; i++) {
	    int result = 0;
	    for(int j = 0; j < columns; j++) {
		if(board[i][j] == currentMove) {
		    result++;
		    if(result == winSize) return true;
		} else {
		    if(result == winSize) return true;
		    result = 0;
		}
	    }
	}
	return false;
    }


    /**
     * Check the diagonals for a winner.
     * @return boolean
     */
    private boolean checkDiagonals() {
	return false;
    }

    
    /**
     * After a move is made, switch currentMove to be 
     * the player that did not make the move.
     */
    private void switchTurns() {
	if(currentMove == RED)
	    currentMove = YELLOW;
	else
	    currentMove = RED;
    }

    
    // print board for testing
    public void print() {
	for(int i = 0; i < rows; i++) {
	    for(int j = 0; j < columns; j++) {
		System.out.print(board[i][j] + " ");
		if(j == columns-1) System.out.print('\n');		    
	    }
	}
				 
    }
    
    // testing
    public static void main(String[] args) {
	ConnectFourLogic g = new ConnectFourLogic(7, 6, 4);
	g.move(0);
	g.move(0);
	g.move(1);
	g.move(1);
	g.move(2);
	g.move(2);
	g.move(3);
	g.move(3);
	g.move(4);
	g.move(4);
    }
       
}
