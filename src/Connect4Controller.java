/**
 * Sean Schlaefli
 * Connect4Controller.java
 * Controller to communicate between the GUI and 
 * the game logic.
 * compiles
 * working/tested
 */

public class Connect4Controller{

    private Connect4Logic game;
    private Connect4GUI view;
    private final char RED = 'r';
    private final char YELLOW = 'y';

    
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
    

    public boolean verifyMove(int column) {
	return game.verifyMove(column);
    }


    public boolean getCurrentMove() {
	return game.getCurrentMove() == RED;
    }

    
    public int makeMove(int column){
	return game.makeMove(column);
    }


    public void switchTurns() {
	game.switchTurns();
    }
    

    public boolean isGameOver(){	    
	return game.gameOver();
    }

    
    public boolean isDraw() {
	return game.isDraw();
    }
    

    public void resetGame() {
	game.reset();
    }
}
