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

    public static int columns;
    public static int rows;
    public static int winSize;
    
    public static void main(String [] args){
	if (args.length == 0) {
	    columns = 7;
	    rows = 6;
	    winSize = 4;
	    launch(args);			    
	} else {
	    columns = Integer.parseInt(args[0]);
	    rows = Integer.parseInt(args[1]);
	    winSize = Integer.parseInt(args[2]);
	    launch(args);
	}	
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
	ConnectFourLogic g = new ConnectFourLogic(columns, rows, winSize);
	ConnectFourGUI app = new ConnectFourGUI(new ConnectFourController(g));
	app.start(primaryStage);
    }
    
}
