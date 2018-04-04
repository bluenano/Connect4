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


    private void clear() {
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY;
            }
        }
    }


    public void reset() {
        currentMove = setFirstMove();
        clear();
    }


    public int getRows() {
        return ROWS;
    }


    public int getColumns() {
        return COLUMNS;
    }


    public char getCurrentMove() {
        return currentMove;
    }


    private char setFirstMove() {
        Random rand = new Random();
        int result = Math.abs(rand.nextInt()) % 2;
        return (result == 0) ? RED : YELLOW;
    }


    public boolean verifyMove(int column) {
        return board[0][column] == EMPTY;
    }


    public int makeMove(int column) {
        int result = findPosition(column);
        board[result][column] = currentMove;
        return result;
    }


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


    public boolean isDraw() {
        for(int i = 0; i < COLUMNS; i++) {
            if(board[0][i] == EMPTY) {
                return false;
            }
        }
        return true;
    }


    public boolean isWin() {
        return checkRows() || checkColumns() || checkDiagonals();
    }


    private boolean checkRows() {
        for(int i = 0; i < ROWS; i++) {
            int count = 0;
            for(int j = 0; j < COLUMNS; j++) {
                count = checkCount(count, i, j);
                if (count == WIN) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean checkColumns() {
        for(int i = 0; i < COLUMNS; i++) {
            int count = 0;
            for(int j = 0; j < ROWS; j++) {
                count = checkCount(count, j, i);
                if (count == WIN) {
                    return true;
                }
            }
        }
        return false;
    }


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


    private boolean checkRightDiagonal(int row, int col) {
        int count = 0;
        while (row < ROWS && col < COLUMNS) {
            count = checkCount(count, row, col);
            if (count == WIN) {
                return true;
            }
            row++;
            col++;
        }
        return false;
    }


  private boolean checkLeftDiagonal(int row, int col) {
        int count = 0;
        while (row < ROWS && col >= 0) {
            count = checkCount(count, row, col);
            if (count == WIN) {
                return true;
            }
            row++;
            col--;
        }
        return false;
    }


    private int checkCount(int count, int row, int col) {
        return (board[row][col] == currentMove) ? ++count : 0;
    }


    public void switchTurns() {
        currentMove = (currentMove == RED) ? YELLOW : RED;
    }


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
