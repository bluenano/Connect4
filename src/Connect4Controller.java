/**
 * Connect4Controller.java
 * abstract class for a controller 
*/

import java.lang.StringBuilder;
import javafx.scene.paint.Color;

public abstract class Connect4Controller {

    protected Connect4GUI view; 

    protected static final char RED = 'r';
    protected static final char YELLOW = 'y';
    protected static final int ROWS = 6;
    protected static final int COLUMNS = 7;

    public abstract void handleUserMove(int column);
    public abstract void resetGame();
    public abstract Color getPlayerColor();
    public abstract String getPlayer();


    /**
     * Initialize the view object
     * @param view the GUI object
     */
    public void attachView(Connect4GUI view) {
        this.view = view;
    }


    /**
     * Get the number of rows in the game
     * @return the number of rows in the game
     */
    public int getRows() {
        return ROWS;
    }


    /**
     * Get the number of columns in the game
     * @return the number of columns in the game
     */
    public int getColumns() {
        return COLUMNS;
    }


    /**
     * Get the color of a mark
     * @param mark the character to test {'r'|'y'}
     * @return the color of the mark 
     */    
    public Color getColor(char mark) {
        return (mark == RED) ? Color.RED : Color.YELLOW;
    }

    
    /**
     * Handle the event where a player wins
     */
    protected void handleGameOver(String result) {
        disableUserMoves();
        view.displayMessage(result);
    }


    /**
     * Disable GUI functionality when the game ends and
     * enable the play again functionality
     */
    protected void disableUserMoves() {
        view.disableColumns();
        view.togglePlayAgain(false);
        view.disableMoveIndicators();
    }

    /**
     * Generate the string used to display a move message in the GUI
     * @param player the name of the player who moved
     * @param col the position in the columns that the move occured at
     * @param row the position in the rows that the move occured at
     * @return the string to indicate a move
     */
    protected String generateMoveString(String player, int col, int row) {
        StringBuilder buildMove = new StringBuilder(player + " moved to column ");
        buildMove.append(Integer.toString(col+1));
        buildMove.append(", row ");
        buildMove.append(Integer.toString(row+1));
        buildMove.append(".");
        return buildMove.toString();
    }
}