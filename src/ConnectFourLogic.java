/**
 * Sean Schlaefli
 * ConnectFourLogic.java
 * implements the ConnectFour game logic.
 * Board is implemented rows x columns.
 * compiles
 * working/tested
 */

import java.util.Random;

public class ConnectFourLogic {

    private final int DEFAULT_ROW = 6;
    private final int DEFAULT_COL = 7;
    private final int DEFAULT_WIN = 4;
    
    private final char EMPTY = 'e';
    private final char RED = 'r';
    private final char YELLOW = 'y';

    private char currentMove;
    private char[][] board;
    private int rows;
    private int columns;    
    private int winSize;

    
    public ConnectFourLogic(int rows, int columns, int winSize) {
	currentMove = setFirstMove();

	this.rows = (rows > 0) ? rows : DEFAULT_ROW;
	this.columns = (columns > 0) ? columns : DEFAULT_COL;
	this.winSize = (winSize > 0 && winSize <= columns && winSize <= rows) ? winSize : DEFAULT_WIN;

	board = new char[rows][columns];
	clear();
    }


    /**
     * Clear the board by setting all positions to 
     * empty.
     */
    private void clear() {
	for(int i = 0; i < rows; i++)
	    for(int j = 0; j < columns; j++)
		board[i][j] = EMPTY;
    }

    
    /**
     * Return the number of rows.
     * @return int
     */
    public int getRows() {
	return rows;
    }


    /**
     * Return the number of columns.
     * @return int
     */
    public int getColumns() {
	return columns;
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
	if(column < columns) {
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
	int position = 0;
	while(position < rows) {
	    if(board[position][column] == EMPTY)
	        result++;
	    position++;
	}
	return result;
    }


    /**
     * Determine if the game is a draw by 
     * checking the top row.
     * @return boolean
     */
    public boolean isDraw() {
	for(int i = 0; i < columns; i++) {
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
	for(int i = 0; i < rows; i++) {
	    int result = 0;
	    for(int j = 0; j < columns; j++) {
	if(board[i][j] == currentMove) {
		    result++;
		    if(result == winSize) return true;
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
	for(int i = 0; i < columns; i++) {
	    int result = 0;
	    for(int j = 0; j < rows; j++) {
		if(board[j][i] == currentMove) {		    
		    result++;
		    if(result == winSize) return true;   
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
	if(rows == columns) {
	    return checkDiagonalsCase1();
	} else if (rows > columns) {
	    return checkDiagonalsCase2();
	} else {
	    return checkDiagonalsCase3();
	}
    }


    /**
     * Check all diagonals of a square board.
     * @return boolean
     */
    private boolean checkDiagonalsCase1() {
	boolean result = false;
	for(int i = 0; i < rows; i++) {
	    result =
		checkDownRightDiagonal(i, 0, rows-1, columns-1-i)
		||
		checkUpRightDiagonal(i, 0, 0, i)
		||
		checkDownLeftDiagonal(i, rows-1, rows-1, i)
		||
		checkUpLeftDiagonal(i, columns-1, 0, columns-1-i);
	    if(result) return result;
	}
	return result;
    }


    /**
     * Check all diagonals of a board with more rows
     * than columns. This logic may be confusing, but
     * can be explained by analyzing different scenarios
     * based on the size of each dimension.
     * @return boolean
     */
    private boolean checkDiagonalsCase2() {
	boolean result = false;	
	for(int i = 0; i < rows; i++) {
	    // calculate all ending row, column combinations
	    int rowPos1 = (i+columns-1 > rows-1) ? rows-1 : i+columns-1;
	    int colPos1 = (i > columns-1) ? columns-i : columns-1;
	    int rowPos2 = (i > columns-1) ? i-columns-1 : 0;
	    int colPos2 = (i < columns-1) ? i : columns-1;
	    int rowPos3 = (i < columns-1) ? i+columns-1 : rows-1;
	    int colPos3 = (i > columns-1) ? columns-i+1 : columns-1;
	    int rowPos4 = (i > columns-1) ? i-columns+1 : 0;
	    int colPos4 = (i < columns-1) ? columns-1-i: 0;		

	    result =
		checkDownRightDiagonal(i, 0, rowPos1, colPos1)
		||
		checkUpRightDiagonal(i, 0, rowPos2, colPos2)
		||
	        checkDownLeftDiagonal(i, columns-1, rowPos3, colPos3)
		||
		checkUpLeftDiagonal(i, columns-1, rowPos4, colPos4);
	    
	    if(result) return result;
	}
	return result;
    }


    /**
     * Check all diagonals of a board with less rows
     * than columns.
     * @return boolean
     */
    private boolean checkDiagonalsCase3() {
	boolean result = false;
	for(int i = 0; i < columns; i++) {
	    // calculate all ending row, column combinations
	    int rowPos1 = (i < rows-1) ? rows-1 : rows-i;
	    int colPos1 = (i+rows-1 < columns-1) ? i+rows-1 : columns-1;
	    int rowPos2 = (i+rows-1 > columns-1) ? (i+rows-1)-(columns-1) : 0;
	    int colPos2 = (i+rows-1 < columns-1) ? i+rows-1 : columns-1;
	    int rowPos3 = (i < rows) ? i : rows-1 ;
	    int colPos3 = (i > rows-1) ? i-rows+1 : 0;
	    int rowPos4 = (i < rows-1) ? rows-1-i : 0;
	    int colPos4 = (i > rows-1) ? i-rows-1 : 0;
	    result =
		checkDownRightDiagonal(0, i, rowPos1, colPos1)
		||
		checkUpRightDiagonal(rows-1, i, rowPos2, colPos2)
		||
		checkDownLeftDiagonal(0, i, rowPos3, colPos3)
		||
		checkUpLeftDiagonal(rows-1, i, rowPos4, colPos4);
	    
	    
	    if(result) return true;
	}
	return result;
    }


    /**
     * Check a diagonal where startRow, startCol is above
     * and to the left of endRow, endCol.
     * precondition: startRow < endRow, startCol < endCol
     * @param startRow the row to start at
     * @param startCol the column to start at
     * @param endRow the row to end at
     * @param endCol the column to end at
     * @return boolean
     */
    private boolean checkDownRightDiagonal(int startRow, int startCol,
					   int endRow, int endCol) {
	int result = 0;
	while(startRow <= endRow && startCol <= endCol) {
	    if(board[startRow][startCol] == currentMove) {
		result++;
		if(result == winSize) return true;
	    } else {
		result = 0;
	    }
	    startRow++;
	    startCol++;
	}
	return false;
    }

    /**
     * Check a diagonal where startRow, startCol is below
     * and to the left of endRow, endCol.
     * precondition: startRow > endRow, startCol < endCol
     * @param startRow the row to start at
     * @param startCol the column to start at
     * @param endRow the row to end at
     * @param endCol the column to end at
     * @return boolean
     */
    private boolean checkUpRightDiagonal(int startRow, int startCol,
					 int endRow, int endCol) {

	int result = 0;
	while(startRow >= endRow && startCol <= endCol) {
	    if(board[startRow][startCol] == currentMove) {
		result++;
		if(result == winSize) return true;
	    } else {
		result = 0;
	    }
	    startRow--;
	    startCol++;
	}
	return false;
    }

    
    /**
     * Check a diagonal where startRow, startCol is above
     * and to the right of endRow, endCol.
     * precondition: startRow < endRow, startCol > endCol
     * @param startRow the row to start at
     * @param startCol the column to start at
     * @param endRow the row to end at
     * @param endCol the column to end at
     * @return boolean
     */
    private boolean checkDownLeftDiagonal(int startRow, int startCol,
					  int endRow, int endCol) {

	int result = 0;
	while(startRow <= endRow && startCol >= endCol) {
	    if(board[startRow][startCol] == currentMove) {
		result++;
		if(result == winSize) return true;
	    } else {
		result = 0;
	    }
	    startRow++;
	    startCol--;
	}
	return false;
    }

    
    /**
     * Check a diagonal where startRow, startCol is below
     * and to the right of endRow, endCol.
     * precondition: startRow > endRow, startCol > endCol
     * @param startRow the row to start at
     * @param startCol the column to start at
     * @param endRow the row to end at
     * @param endCol the column to end at
     * @return boolean
     */
    private boolean checkUpLeftDiagonal(int startRow, int startCol,
					int endRow, int endCol) {
	int result = 0;
	while(startRow >= endRow && startCol >= endCol) {
	    if(board[startRow][startCol] == currentMove) {
		result++;
		if(result == winSize) return true;
	    } else {
		result = 0;
	    }
	    startRow--;
	    startCol--;
	}
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
	for(int i = 0; i < rows; i++) {
	    for(int j = 0; j < columns; j++) {
		System.out.print(board[i][j] + " ");
		if(j == columns-1) System.out.print('\n');		    
	    }
	}
				 
    }


    // to string representation of the game board
    // could use string builder but don't see an issue
    // with allocating new references at each iteration
    // on such a small scale
    public String toString() {
	String result = "";
	for(int i = 0; i < rows; i++) {
	    for(int j = 0; j < columns; j++) {
		result += board[i][j] + " ";
		if(j == columns-1) result += "\n";
	    }
	}
	return result;
    }

    
    // testing
    public static void main(String[] args) {
	ConnectFourLogic g = new ConnectFourLogic(3, 4, 3);
	g.move(2);
	g.move(1);
	g.move(1);
	g.move(2);
	g.move(0);
	g.move(0);
	g.move(0);
    }
       
}
