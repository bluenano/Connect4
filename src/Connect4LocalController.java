/**
 * Connect4LocalController.java
 * Controller to communicate between the GUI and
 * the game logic.
 * compiles
 * working/tested
 */


import javafx.scene.paint.Color;


public class Connect4LocalController extends Connect4Controller {

    private Connect4Logic game;

    private static final char RED = 'r';
    private static final char YELLOW = 'r';


    public Connect4LocalController(Connect4Logic game) {
        this.game = game;
    }


    public void handleUserMove(int column) {    
        if (game.verifyMove(column)) {
            sendMoveUpdates(column);
        }
    }


    private void sendMoveUpdates(int column) {
        int row = game.makeMove(column);
        view.addDisc(column, row, getPlayerColor());
        checkForGameOver(column, row);
    }


    private void checkForGameOver(int column, int row) {
        if (game.isWin()) {
            super.handleWin();
        } else if (game.isDraw()) {
            super.handleDraw();
        } else {
            handleNextMove(column, row);
        }
    }

 
    private void handleNextMove(int column, int row) {
        view.displayMove(getPlayer(), column, row, getPlayerColor());
        switchTurns();
    }


    private boolean getCurrentMove() {
        return game.getCurrentMove() == RED;
    }


    public void switchTurns() {
        game.switchTurns();
        view.setMoveIndicatorFill(getPlayerColor());
    }


    public void resetGame() {
        game.reset();
        view.setUIScene();
    }


    public String getPlayer() {
        return (getCurrentMove()) ? "Player Red" : "Player Yellow";
    }

    public Color getPlayerColor() {
        return (game.getCurrentMove() == RED) ? Color.RED : Color.YELLOW;
    }
    
}
