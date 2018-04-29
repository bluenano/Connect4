/**
 * Connect4Controller.java
 * abstract class for a controller 
*/

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

    public void attachView(Connect4GUI view) {
        this.view = view;
    }

    public int getRows() {
        return ROWS;
    }

    public int getColumns() {
        return COLUMNS;
    }


    public Color getColor(char mark) {
        return (mark == RED) ? Color.RED : Color.YELLOW;
    }

   protected void handleWin() {
        disableUserMoves();
        view.displayWin(getPlayer(), getPlayerColor());
    }


    protected void handleDraw() {
        disableUserMoves();
        view.displayDraw();
    }


    private void disableUserMoves() {
        view.disableColumns();
        view.enablePlayAgain();
        view.disableMoveIndicator();
    }

}