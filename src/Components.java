/**
 * Components.java
*/

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Components {

    private static final int SIZE = 100;
    private static final int SPACING = 30;


    public static Scene createLaunchScene(Button local, Button online, Button quit) {
        VBox box = new VBox();
        box.setPrefWidth(SIZE*2);
        local.setMinWidth(SIZE*2);
        online.setMinWidth(SIZE*2);
        quit.setMinWidth(SIZE*2);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(SPACING);
        box.getChildren().addAll(local, online, quit);
        return new Scene(box, SIZE*3, SIZE*3);
    }


    public static Scene createNetInfoScene(Button connect, Button menu, 
                                           TextField server, TextField port,
                                           TextField name) {
        VBox box = new VBox();
        box.setPrefWidth(SIZE*2);
        connect.setMinWidth(SIZE*2);
        menu.setMinWidth(SIZE*2);
        server.setMaxWidth(SIZE*2);
        port.setMaxWidth(SIZE*2);
        name.setMaxWidth(SIZE*2);        
        server.setPromptText("Enter Server Address");
        port.setPromptText("Enter Port Number");
        name.setPromptText("Enter Display Name");
        box.setAlignment(Pos.CENTER);
        box.setSpacing(SPACING);
        box.getChildren().addAll(server, port, name, connect, menu);
        return new Scene(box, SIZE*3, SIZE*3);
    }
    
}