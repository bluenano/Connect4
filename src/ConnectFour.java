/**
 * Sean Schlaefli
 * ConnectFour.java
 * Launches the ConnectFour application.
 * compiles
 * working/tested
 */


import javafx.application.Application;
import javafx.stage.Stage;

public class ConnectFour extends Application{

    public static int boardSize;
    public static int winSize;
    
    public static void main(String [] args){
	if (args.length == 0) {
	    boardSize = 7;
	    winSize = 4;
	    launch(args);			    
	} else {
	    boardSize = Integer.parseInt(args[0]);
	    winSize = Integer.parseInt(args[1]);
	    launch(args);
	}	
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
	ConnectFourLogic g = new ConnectFourLogic(boardSize, winSize);
	ConnectFourGUI app = new ConnectFourGUI(new ConnectFourController(g));
	app.start(primaryStage);
    }
    
}
