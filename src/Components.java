/**
 * Components.java
 * creates Scene objects that are used at launch
 * and to specify network connection information
 * prior to playing an online game
 */


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class Components {

    private static final int SIZE = 100;
    private static final int SPACING = 30;

    /**
     * Creates the objects used in the launch scene and returns 
     * a new Scnene object to be displayed at launch
     * @param local the button that starts a local game
     * @param online the button that displays the net info scene
     * @param quit the button that exits the application
     * @return the scene to be displayed at launch
     */
    public static Scene createMainMenu(Button local, Button online, Button quit) {
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



    /**
     * Create the objects used in the network information scene and
     * returns a new Scene object to be displayed after a user clicks
     * the button that displays this scene
     * @param connect the button that will display the game scene after
     * connecting to the server specified by the fields in this scene
     * @param menu the button that will display the main menu
     * @param server the text field that expects a server address
     * @param port the text field that expects a port that the server
     * is listening on
     * @param name the text field that expects a display name 
     * @return the scene to be displayed 
     *
     */
    public static Scene createNetInfo(Button connect, Button menu, 
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