/**
 * Connect4LocalController.java
 * Controller to communicate between the GUI and
 * the game logic in a local game.
 * compiles
 * working/tested
 */


import javafx.scene.paint.Color;


public class Connect4LocalController extends Connect4Controller {


    private Connect4Logic game;


    /**
     * Constructor for the controller used in a local game
     * @param game the game logic
     */
    public Connect4LocalController(Connect4Logic game) {
        this.game = game;
    }


    /**
     * Handle the event where a user moves
     * @param column the column position the user wants to move to
     */
    public void handleUserMove(int col) {    
        if (game.verifyMove(col)) {
            sendMoveUpdates(col);
        }
    }


    /**
     * Send updates to the GUI and the logic when
     * a player moves
     * @param column the column position the user is moving to
     */
    private void sendMoveUpdates(int col) {
        int row = game.makeMove(col);
        view.addDisc(col, row, getPlayerColor());
        checkForGameOver(col, row);
    }


    /**
     * Check if the game is over
     * @param column the column position of the most recent move
     * @param row the row position of the most recent move
     */
    private void checkForGameOver(int col, int row) {
        if (game.isWin()) {
            handleGameOver(getPlayer() + " won the game");
        } else if (game.isDraw()) {
            handleGameOver("The game ended in a draw");
        } else {
            handleNextMove(col, row);
        }
    }

 
    /**
     * Set the game up for the next move
     * @param column the column position of the most recent move
     * @param row the row position of the most recent move
     */
    private void handleNextMove(int col, int row) {
        String move = generateMoveString(getPlayer(), col, row);
        view.displayMessage(move);
        switchTurns();
    }


    /**
     * Update the logic and GUI after a user moves
     */
    public void switchTurns() {
        game.switchTurns();
        view.setMoveIndicatorFill(getPlayerColor());
    }


    /**
     * Reset the logic and GUI
     */
    public void resetGame() {
        game.reset();
        view.setUIScene();
    }


    /**
     * Get the player whose turn it is
     * @return the String representing the player
     */
    public String getPlayer() {
        return (game.getCurrentMove() == RED) ? "Player Red" : "Player Yellow";
    }


    /**
     * Get the color of the player whose turn it is
     * @return the Color of the player
     */
    public Color getPlayerColor() {
        return (game.getCurrentMove() == RED) ? Color.RED : Color.YELLOW;
    }


}
