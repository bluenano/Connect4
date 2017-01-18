/**
 * Sean Schlaefli
 * ConnectFourController.java
 * Controller to communicate between the GUI and 
 * the game logic.
 * compiles
 * working/tested
 */

public class ConnectFourController{

	private ConnectFourLogic game;
	private ConnectFourGUI view;
	
	public ConnectFourController(ConnectFourLogic game){
		this.game = game;
	}


	/**
	 * Attach the GUI to the view
	 * @param ConnectFourGUI view
	 */
	public void attachView(ConnectFourGUI view){
		this.view = view;
	}


	public int getRows(){
		return game.getRows();
	}

	public int getColumns(){
		return game.getColumns();
	}

    
	public boolean makeMove(int column){
		return game.move(column);
	}


	/**
	 * check the game logic for a winner, if true return to the GUI 
	 * to display the winner, otherwise update the color in the GUI
	 */
	public String isOver(){	    
	    return "";
	}

}
