/**
 * Sean Schlaefli
 * ConnectFour.java
 * Launches the ConnectFour application.
 * compiles
 * working/tested
 */


import javafx.application.Application;
import javafx.stage.Stage;

public class Connect4 extends Application{

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
	Connect4Logic game = new Connect4Logic(rows, columns, winSize);
	Connect4Controller controller = new Connect4Controller(game);
        Connect4Setup setup = new Connect4Setup(controller);
	setup.start(primaryStage);
	Connect4GUI app = new Connect4GUI(controller);
	app.start(primaryStage);
    }
    
}
