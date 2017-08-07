/**
 * Sean Schlaefli
 * Connect4.java
 * Launches the Connect4 application.
 * compiles
 * working/tested
 */


import javafx.application.Application;
import javafx.stage.Stage;

public class Connect4 extends Application{
    
    public static void main(String [] args){
	launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
	Connect4Logic game = new Connect4Logic();
	Connect4Controller controller = new Connect4Controller(game);
	Connect4GUI app = new Connect4GUI(controller);
	app.start(primaryStage);
    }
    
}
