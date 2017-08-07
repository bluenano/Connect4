/**
 * Sean Schlaefli
 * Connect4Controller.java
 * Controller to communicate between the GUI and 
 * the game logic.
 * compiles
 * working/tested
 */

import javafx.scene.paint.Color;

public class Connect4Controller{

    
    private Connect4Logic game;
    private Connect4GUI view;
    private static final char RED = 'r';
    private static final char YELLOW = 'r';

    
    public Connect4Controller(Connect4Logic game){
	this.game = game;	
    }
    
    
    /**
     * Attach the GUI to the controller
     * @param ConnectFourGUI view
     */
    public void attachView(Connect4GUI view){
	this.view = view;
    }
    
    
    public int getRows(){
	return game.getRows();
    }

    
    public int getColumns(){
	return game.getColumns();
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
	    handleWin();
	} else if (game.isDraw()) {
	    handleDraw();
	} else {
	    handleNextMove(column, row);
	}
    }


    private void handleWin() {
	disableUserMoves();
	view.displayWin(getPlayer(), getPlayerColor());
    }

    private void handleDraw() {
	disableUserMoves();
	view.displayDraw();
    }

    
    private void handleNextMove(int column, int row) {	
	view.displayMove(getPlayer(), getPlayerColor(), column, row);
	switchTurns();		
    }


    private void disableUserMoves() {
	view.disableColumns();
	view.enableButtons();
	view.disableMoveIndicator();
    }


    private boolean getCurrentMove() {
	return game.getCurrentMove() == RED;
    }


    public String getPlayer() {
	return (game.getCurrentMove() == RED) ? "Player Red" : "Player Yellow";
    }


    public Color getPlayerColor() {
	return (game.getCurrentMove() == RED) ? Color.RED : Color.YELLOW;
    }

    
    public void switchTurns() {
	game.switchTurns();
	view.setMoveIndicatorFill(getPlayerColor());
    }   
    

    public void resetGame() {
	game.reset();
	view.setUIScene();
    }
    
}
