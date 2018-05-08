/**
 * Connect4Logic.java
 * implements the Connect4 game logic.
 * Board is implemented rows x columns.
 * compiles
 * working/tested
 */


import java.util.Random;


public class Connect4Logic {


    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final int WIN = 4;

    private static final char EMPTY = 'e';
    private static final char RED = 'r';
    private static final char YELLOW = 'y';

    private char currentMove;
    private char[][] board;
	private int rematch_counter = 0;


    /**
     * Constructor used for the logic of Connect 4
     */
    public Connect4Logic() {
        board = new char[ROWS][COLUMNS];
        reset();
    }


    /**
     * Clear the game board
     */
    private void clear() {
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY;
            }
        }
    }


    /**
     * Set the first move and reset the game board
     */
    public void reset() {
        currentMove = setFirstMove();
        clear();
    }


    /**
     * Get the number of rows
     * @return the number of rows
     */
    public int getRows() {
        return ROWS;
    }


    /**
     * Get the number of columns
     * @return the number of columns
     */
    public int getColumns() {
        return COLUMNS;
    }


    /**
     * Get the mark of the player represented by currentMove
     * @return the character of the player represented by currentMove
     */
    public char getCurrentMove() {
        return currentMove;
    }


    /**
     * Get the rematch count
     * @return the rematch count
     */
	public int getRematchCount(){
		return rematch_counter;
	}


    /**
     * Use a random number generator to select the player 
     * who will go first
     * @return the character representing the player who 
     * will go first
     */
    private char setFirstMove() {
        Random rand = new Random();
        int result = Math.abs(rand.nextInt()) % 2;
        return (result == 0) ? RED : YELLOW;
    }


    /**
     * Verify that a move in a column is valid
     * @param col the column to check
     * @return true if the column contains a valid
     * position to move to, false otherwise
     */
    public boolean verifyMove(int col) {
        return board[0][col] == EMPTY;
    }


    /**
     * Place the marker associated with current move
     * in a column
     * @param col the column to place the mark
     * @return the row position that the mark was placed
     */
    public int makeMove(int col) {
        int result = findPosition(col);
        board[result][col] = currentMove;
        return result;
    }


    /**
     * Find the first available row position in a column
     * @param col the column to check
     * @return the first available row position
     */
    private int findPosition(int col) {
        int result = -1;
        for (int i = 0; i < ROWS; i++) {
            if (board[i][col] == EMPTY) {
                result++;
            } else {
                return result;
            }
        }
        return result;
    }


    /**
     * Check if the game ended in a draw
     * @return true if the game ended in a draw, false otherwise
     */
    public boolean isDraw() {
        for(int i = 0; i < COLUMNS; i++) {
            if(board[0][i] == EMPTY) {
                return false;
            }
        }
        return true;
    }


    /**
     * Check if the player associated with currentMove won the game
     * @return true if the player associated with currentMove won the game,
     * false otherwise
     */
    public boolean isWin() {
        return checkRows() || checkColumns() || checkDiagonals();
    }


    /**
     * Increment the rematch counter
     */
	public void incRematch(){
		rematch_counter += 1;
	}

	   
    /**
     * Reset the rematch counter
     */
	public void resetRematch(){
		rematch_counter = 0;
	}
	

    /**
     * Check all the rows in the game board for a winner
     * @return true if a winner is found, false otherwise
     */
    private boolean checkRows() {
        for(int i = 0; i < ROWS; i++) {
            int count = 0;
            for(int j = 0; j < COLUMNS; j++) {
                count = updateCount(count, i, j);
                if (count == WIN) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Check all the columns in the game board for a winner
     * @return true if a winner is found, false otherwise
     */
    private boolean checkColumns() {
        for(int i = 0; i < COLUMNS; i++) {
            int count = 0;
            for(int j = 0; j < ROWS; j++) {
                count = updateCount(count, j, i);
                if (count == WIN) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Check all the diagonals in the game board for a winner
     * @return true if a winner is found, false otherwise
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
     * Check a right diagonal
     * @param row the starting row position
     * @param col the starting column position
     * @return true if the diagonal contains a winner, false otherwise
     */
    private boolean checkRightDiagonal(int row, int col) {
        int count = 0;
        while (row < ROWS && col < COLUMNS) {
            count = updateCount(count, row, col);
            if (count == WIN) {
                return true;
            }
            row++;
            col++;
        }
        return false;
    }


    /**
     * Check a left diagonal
     * @param row the starting row position
     * @param col the starting column position
     * @return true if the diagonal contains a winner, false otherwise
     */
    private boolean checkLeftDiagonal(int row, int col) {
        int count = 0;
        while (row < ROWS && col >= 0) {
            count = updateCount(count, row, col);
            if (count == WIN) {
                return true;
            }
            row++;
            col--;
        }
        return false;
    }


    /**
     * Update the count by checking the value of the location
     * given by row and column
     * @param count the current count
     * @param row the row position to check
     * @param col the column position to check
     * @return the updated count
     */
    private int updateCount(int count, int row, int col) {
        return (board[row][col] == currentMove) ? ++count : 0;
    }


    /**
     * Switch turns 
     */
    public void switchTurns() {
        currentMove = (currentMove == RED) ? YELLOW : RED;
    }


    /**
     * Print the game board
     */
    public void print() {
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                System.out.print(board[i][j] + " ");
                if(j == COLUMNS-1) {
                    System.out.print('\n');
                }
            }
        }

    }


    /**
     * Return the string representation of the game board
     * @return the string representing the game board
     */
    public String toString() {
        String result = "";
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                result += board[i][j] + " ";
                if(j == COLUMNS-1) {
                    result += "\n";
                }
            }
        }
        return result;
    }


}
