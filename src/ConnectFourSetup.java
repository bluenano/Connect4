/**
 * 
 */

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ConnectFourSetup extends Application {

    private ConnectFourController controller;

    public ConnectFourSetup(ConnectFourController controller) {
	this.controller = controller;
    }

    
    public void start(Stage primary) throws Exception {
	Button start = new Button("Start Game");
    }
    
}

