/**
 * Connect4.java
 * Launches the Connect4 client application.
 * compiles
 * working/tested
 */


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.lang.Thread;
import java.lang.NumberFormatException;

public class Connect4 extends Application {


    /**
     * The main function
     * @param args the command line arguments passed to this program
     */
    public static void main(String [] args) {
        launch(args);
    }


    /**
     * Launch a local game of Connect4
     * @param stage the Stage object to display scenes
     */
    public void launchLocalGame(Stage stage) {
            Connect4Controller controller = new Connect4LocalController(
                                            new Connect4Logic());
            Connect4GUI app = new Connect4GUI(controller);
            try {
                app.start(stage);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
    }


    /**
     * Launch a networked game of Connect4
     * @param stage the Stage object to display scenes
     * @param server the address of the server to connect to
     * @param port the port to connect on
     * @param name the user name to connect as
     */
    public void launchNetworkGame(Stage stage, String server,
                                  int port, String name) {
        Connect4NetController controller = new Connect4NetController(server, port, name);
        Connect4GUI app = new Connect4GUI(controller);
        try {
            app.start(stage);
            Thread thread = new Thread(controller);
            thread.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Exiting the application...");
            System.exit(0);
        }
    }


    /**
     * Display a Scene object
     * @param stage the Stage object to display the scene on
     * @param scene the Scene object to display
     * @param title the title of the scene
     */
    public void showScene(Stage stage, Scene scene, String title) {
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }


    /**
     * Creates all of the menu objects and 
     * displays the main menu
     * @param stage the Stage object to display the main menu on
     */
    public void launchMainMenu(Stage stage) {

        Button local = new Button("Play Local");
        local.setOnMouseClicked(e -> {
            launchLocalGame(stage);
        });

        Button online = new Button("Play Online");
        online.setOnMouseClicked(e -> {
            Button connect = new Button("Connect");
            Button menu = new Button("Main Menu");
            TextField address = new TextField();
            TextField textPort = new TextField();
            TextField displayName = new TextField();

            connect.setOnMouseClicked(e2 -> {
                try {
                    String server = address.getText();
                    int port = Integer.parseInt(textPort.getText());
                    String name = displayName.getText();
					// TESTING CONVENIENCE
					// String server = "localhost";
                    // int port = 8902;
                    // String name = "blob";
                    if (!server.equals("")
                        &&
                        port >= 0 && port <= 65535
                        &&
                        !name.equals("")) {
                        launchNetworkGame(stage, server, port, name);
                    } else {                        
                        System.out.println("Enter valid network connection information");
                    }

                } catch (NumberFormatException ex) {
                    System.out.println(ex.getMessage());
                }
            });          

            menu.setOnMouseClicked(e3 -> {
                launchMainMenu(stage);
                                
            });

            showScene(stage, 
                      Components.createNetInfo(connect,
                                               menu,                                                         
                                               address,
                                               textPort,
                                               displayName),
                      "Network Connection Information");
            connect.requestFocus();
        });

        Button quit = new Button("Quit");
        quit.setOnMouseClicked(e -> System.exit(0));

        showScene(stage, 
                  Components.createMainMenu(local, online, quit),
                  "Connect4");        
    }


    /**
     * Starts the Connect 4 client
     * @param stage the Stage object to display scenes on
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        launchMainMenu(primaryStage);
    }


}
