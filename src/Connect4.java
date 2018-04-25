/**
 * Connect4.java
 * Launches the Connect4 application.
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

public class Connect4 extends Application {


    public static void main(String [] args) {
        launch(args);
    }


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


    public void launchNetworkGame(Stage stage, String server,
                                  int port, String name) {
        Connect4NetController controller = new Connect4NetController(server);
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


    public void showLaunchScene(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setTitle("Connect4");
        stage.show();
    }


    public void showNetInfoScene(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setTitle("Network Connection Information");
        stage.show();
    }


    public void launchScene(Stage stage) {

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
                String server = address.getText();
                int port = Integer.parseInt(textPort.getText());
                String name = displayName.getText();
                launchNetworkGame(stage, server, port, name);
            });          

            menu.setOnMouseClicked(e3 -> {
                launchScene(stage);
                                
            });

            showNetInfoScene(stage, 
                               Components.createNetInfoScene(connect,
                                                             menu,                                                         
                                                             address,
                                                             textPort,
                                                             displayName));
            connect.requestFocus();
        });

        Button quit = new Button("Quit");
        quit.setOnMouseClicked(e -> System.exit(0));

        showLaunchScene(stage, 
                        Components.createLaunchScene(local, online, quit));        
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        launchScene(primaryStage);
    }


}
