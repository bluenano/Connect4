/**
 * 
 */

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class Connect4Setup extends Application {

    private Connect4Controller controller;

    public Connect4Setup(Connect4Controller controller) {
	this.controller = controller;
    }

    
    public void start(Stage primary) throws Exception {
	Button start = new Button("Start Game");
    }
    
}

